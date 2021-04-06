package datastructureproject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides some bitboard operations to solve the possible moves.
 */
public class BitOperations {

    //Each rank and file as bitboards
    final static long[] rank = {
        0x00000000000000FFL, 0x000000000000FF00L, 0x0000000000FF0000L, 0x00000000FF000000L,
        0x000000FF00000000L, 0x0000FF0000000000L, 0x00FF000000000000L, 0xFF00000000000000L
    };
    final static long[] file = {
        0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
        0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };

    private static final long[] knightAttacks = new long[64];

    /**
     * [0,1,2,3] = N, E, S W
     */
    private static final long[][] rookRays = new long[4][64];

    /**
     * [0,1,2,3] = NW, NE, SE, SW
     */
    private static final long[][] bishopRays = new long[4][64];

    final static long[] whitePawnMoves = new long[64];

    final static long[] blackPawnMoves = new long[64];

    final static long[] whitePawnAttacks = new long[64];

    final static long[] blackPawnAttacks = new long[64];

    final static long[] adjacentSquares = new long[64];

    static {
        Square current;
        long currentBB;
        for (int x = 0; x <= 63; x++) {
            current = Square.squareAt(x);
            currentBB = current.getBitboard();

            whitePawnAttacks[x] |= ((currentBB & ~(rank[7] | file[7])) << 9)
                    | ((currentBB & ~(rank[7] | file[0])) << 7);

            whitePawnMoves[x] |= ((currentBB & ~rank[7]) << 8)
                    | ((currentBB & rank[1]) << 16);

            blackPawnAttacks[x] |= ((currentBB & ~(rank[0] | file[0])) >> 9)
                    | ((currentBB & ~(rank[0] | file[7])) >> 7);

            blackPawnMoves[x] |= ((currentBB & ~rank[0]) >> 8)
                    | ((currentBB & rank[6]) >> 16);
        }
        blackPawnAttacks[63] = 0x0040000000000000L;
        blackPawnMoves[63] = 0x0080000000000000L;   //sq 64 weirdness 
    }

    /**
     * Generates possible knight moves.
     */
    static {
        Square current;
        long currentBB;
        long attacks;
        for (int x = 0; x <= 62; x++) {
            attacks = 0L;
            current = Square.squareAt(x);
            currentBB = current.getBitboard();                              //Masks:
            attacks |= ((currentBB & ~(rank[6] | rank[7] | file[7])) << 17) //All but ranks 7,8 and file H
                    | ((currentBB & ~(rank[7] | file[6] | file[7])) << 10) //All but rank 8 and files G,H
                    | ((currentBB & ~(rank[0] | file[6] | file[7])) >> 6) //ETC.
                    | ((currentBB & ~(rank[0] | rank[1] | file[7])) >> 15)
                    | ((currentBB & ~(rank[6] | rank[7] | file[0])) << 15)
                    | ((currentBB & ~(rank[7] | file[0] | file[1])) << 6)
                    | ((currentBB & ~(rank[0] | file[0] | file[1])) >> 10)
                    | ((currentBB & ~(rank[0] | rank[1] | file[0])) >> 17);

            knightAttacks[x] = attacks;

        }
        knightAttacks[63] = 0x0020400000000000L;
    }

    /**
     * Generates adjacent squares for every square
     */
    static {
        Square current;
        long currentBB;
        long attacks;
        for (int x = 0; x <= 62; x++) {
            attacks = 0L;
            current = Square.squareAt(x);
            currentBB = current.getBitboard();

            attacks |= ((currentBB & ~rank[0]) >> 8)
                    | ((currentBB & ~rank[7]) << 8)
                    | ((currentBB & ~file[0]) >> 1)
                    | ((currentBB & ~file[7]) << 1)
                    | ((currentBB & ~(rank[0] | file[0])) >> 9)
                    | ((currentBB & ~(rank[7] | file[7])) << 9)
                    | ((currentBB & ~(rank[0] | file[7])) >> 7)
                    | ((currentBB & ~(rank[7] | file[0])) << 7);

            adjacentSquares[x] = attacks;
        }
        adjacentSquares[63] = 0x40c0000000000000L; //something weird with square 64 ..
    }

    /**
     * Generates the bishop rays to different directions using some utility
     * functions.
     */
    static {
        long current;
        for (int x = 0; x <= 62; x++) {

            current = Square.squareAt(x).getBitboard();

            bishopRays[0][x] = getBishopRaysSENW(current, "NW");
            bishopRays[1][x] = getBishopRaysNESW(current, "NE");
            bishopRays[2][x] = getBishopRaysSENW(current, "SE");
            bishopRays[3][x] = getBishopRaysNESW(current, "SW");
        }
        bishopRays[0][63] = 0L;
        bishopRays[1][63] = 0L;
        bishopRays[2][63] = 0L;
        bishopRays[3][63] = 0x40201008040201L;
    }

    /**
     * Generates the rook rays to different directions using some utility
     * functions.
     */
    static {
        long current;
        for (int x = 0; x <= 62; x++) {

            current = Square.squareAt(x).getBitboard();
            rookRays[0][x] = getRookFileRays(current, "NORTH");
            rookRays[1][x] = getRookRankRays(current, "EAST");
            rookRays[2][x] = getRookFileRays(current, "SOUTH");
            rookRays[3][x] = getRookRankRays(current, "WEST");
        }

        rookRays[0][63] = 0L;
        rookRays[1][63] = 0L;
        rookRays[2][63] = 0x80808080808080L;
        rookRays[3][63] = 0x7f00000000000000L;
    }

    public static long[] getKnightAttacks() {
        return knightAttacks;
    }

    public static long[] getAdjacentSquares() {
        return adjacentSquares;
    }

    public static long[][] getBishopRays() {
        return bishopRays;
    }

    public static long[][] getRookRays() {
        return rookRays;
    }

    public static long[] getWhitePawnMoves() {
        return whitePawnMoves;
    }

    public static long[] getBlackPawnMoves() {
        return blackPawnMoves;
    }

    public static long[] getBlackPawnAttacks() {
        return blackPawnAttacks;
    }

    public static long[] getWhitePawnAttacks() {
        return whitePawnAttacks;
    }

    public static long[] getPawnAttacks(Side side) {
        return (String.valueOf(side).equals("WHITE")) ? whitePawnAttacks : blackPawnAttacks;
    }

    public static long getKnightMoves(Square square, long ownPieces) {
        return knightAttacks[square.ordinal()] & ~ownPieces;
    }

    /**
     * Returns available pawn moves (not captures). If no pieces on the way, the
     * moves are returned after an AND operation with the possible moves and the
     * unoccupied squares.
     *
     * @param square of the pawn in question
     * @param side of the pawn in question
     * @param occupied = bitboard representing all pieces (=all occupied
     * squares)
     * @return
     */
    public static long getPawnMoves(Square square, Side side, long occupied) {
        long pawnMoves;
        if (side.equals(Side.WHITE)) {
            if (square.getRank().equals(Rank.RANK_2)) {
                long squareInFront = square.getBitboard() << 8;     //Bit shift to get the square in front of pawn
                long pieceInFront = squareInFront & occupied;       //AND op. to see if that square has any pieces on it
                if (pieceInFront != 0L) {                           //If 1, then there is a piece blocking
                    return 0L;
                }
            }
            pawnMoves = whitePawnMoves[square.ordinal()] & ~occupied; //available moves have to be both natural and unoccupied
        } else {
            if (square.getRank().equals(Rank.RANK_7)) {
                long squareInFront = square.getBitboard() >> 8;
                long pieceInFront = squareInFront & occupied;
                if (pieceInFront != 0L) {
                    return 0L;

                }
            }

            pawnMoves = blackPawnMoves[square.ordinal()] & ~occupied;
        }

        return pawnMoves;
    }

    /**
     * Return normal (diagonal) pawn captures. No 'en passant' atm.
     *
     * @param square of the pawn in question
     * @param enemyPieces bitboard representing enemy pieces squares
     * @param side of the pawn in question
     * @return bitboard of pseudolegal pawn attacks
     */
    public static long getPawnCaptures(Square square, Side side, long enemyPieces) {
        long pawnAttacks;
        if (side.equals(Side.WHITE)) { //Tämän saa ? avulla nätisti
            pawnAttacks = whitePawnAttacks[square.ordinal()] & enemyPieces;
        } else {
            pawnAttacks = blackPawnAttacks[square.ordinal()] & enemyPieces;
        }

        return pawnAttacks;
    }

    /**
     * Goes through all the directions (NE,SW,NW,SE) and uses masks and
     * forward/reverse scan to figure out bishops possible attack squares.
     *
     * @param square square of the bishop
     * @param occupied bitboard representing occupied squares
     * @return
     */
    public static long getBishopMoves(Square square, long occupied) {
        long bishopAttacks = 0L;
        int occupierIndex;
        for (int i = 0; i < 4; i++) {
            bishopAttacks |= bishopRays[i][square.ordinal()];
            if ((bishopRays[i][square.ordinal()] & occupied) != 0L) {
                if (i < 2) {
                    occupierIndex = bitScanForward((bishopRays[i][square.ordinal()] & occupied));
                } else {
                    occupierIndex = bitScanReverse((bishopRays[i][square.ordinal()] & occupied));
                }
                bishopAttacks &= ~bishopRays[i][occupierIndex];                 //Leave out squares after the blocker

            }
        }
        return bishopAttacks;
    }

    public static long getQueenMoves(Square square, long occupied) {
        return (getBishopMoves(square, occupied) | getRookMoves(square, occupied));
    }

    /**
     * Need to clean up a little | Calculates the diagonals from the defined
     * square. Handles NE and SW rays. (North-East, South-West) Used for
     * generating a pre-calculated array for the bishops 'rays'.
     *
     * @param square of the bishop
     * @param way (expected values: (NE | SW))
     * @return
     */
    public static long getBishopRaysNESW(long square, String way) {
        long bishopAttacks = 0L;
        long nextSquare;

        while ((way.equals("NE")) ? (square & (file[7] | rank[7])) == 0 : (square & (file[0] | rank[0])) == 0) {
            nextSquare = (way.equals("NE")) ? square << 9 : square >> 9;
            bishopAttacks |= nextSquare;

            square = nextSquare;
        }
        return bishopAttacks;

    }

    /**
     * Calculates the diagonals from the defined square. Handles SE and NW rays
     * (South-East, North-West). Used for generating a pre-calculated array for
     * the bishops 'rays'.
     *
     * @param square of the bishop
     * @param way (expected values: (NW | SE))
     * @return
     */
    public static long getBishopRaysSENW(long square, String way) {
        long bishopAttacks = 0L;
        long nextSquare;

        while ((way.equals("NW")) ? (square & (file[0] | rank[7])) == 0 : (square & (file[7] | rank[0])) == 0) {
            nextSquare = (way.equals("NW")) ? square << 7 : square >> 7;
            bishopAttacks |= nextSquare;

            square = nextSquare;
        }
        return bishopAttacks;
    }

    /**
     * Needs rework.
     *
     * @param square of the rook
     * @param occupied - bitboard representation of occupied squares
     * @return
     */
    public static long getRookMoves(Square square, long occupied) {
        long rookAttacks = 0L;
        int occupierIndex;
        for (int i = 0; i < 4; i++) {
            rookAttacks |= rookRays[i][square.ordinal()];
            if ((rookRays[i][square.ordinal()] & occupied) != 0L) {
                if (i < 2) {
                    occupierIndex = bitScanForward((rookRays[i][square.ordinal()] & occupied));
                } else {
                    occupierIndex = bitScanReverse((rookRays[i][square.ordinal()] & occupied));
                }
                rookAttacks &= ~rookRays[i][occupierIndex];

            }
        }
        return rookAttacks;

    }

    /**
     * Utility function to generate rook rays in each square.
     *
     * @param square of the rook
     * @param way (expected values: (E | W))
     * @return East or West rays
     */
    public static long getRookRankRays(long square, String way) {
        long rays = 0L;
        long nextSquare;

        long borderFile = (way.equals("EAST")) ? file[7] : file[0];

        while ((square & borderFile) == 0) {
            nextSquare = (way.equals("EAST")) ? square << 1 : square >> 1;
            rays |= nextSquare;

            square = nextSquare;
        }
        return rays;

    }

    /**
     * Utility function to generate rook rays in each square.
     *
     * @param square of the rook
     * @param way (expected values: (N | S))
     * @return North or South rays
     */
    public static long getRookFileRays(long square, String way) {
        long rays = 0L;
        long nextSquare;

        long borderRank = (way.equals("NORTH")) ? rank[7] : rank[0];

        while ((square & borderRank) == 0) {
            nextSquare = (way.equals("NORTH")) ? square << 8 : square >> 8;
            rays |= nextSquare;

            square = nextSquare;
        }
        return rays;

    }

    /**
     * Same logic as knight: an AND operation between kings possible moves
     * (=adjacent squares to king) and own pieces.
     *
     * @param square of the king
     * @param ownPieces bitboard representation of own pieces
     * @return kings pseudolegal moves as bitboard
     */
    public static long getKingMoves(Square square, long ownPieces) {
        return adjacentSquares[square.ordinal()] & ~ownPieces;
    }

    public static Square getKingSquare(Board b, Side side) {
        long king = b.getBitboard(Piece.make(side, PieceType.KING));
        int kingIndex = bitScanForward(king);
        return Square.squareAt(kingIndex);
    }

    /**
     * Checks for all possible attacks to a square from the enemy pieces. First,
     * from the kings point of view, checks for pawn attacks (close diagonals)
     * for enemy pawns with an AND operation. If no enemy pawns detected, then
     * 'attacks' stays 0L. Logic is same with all other pieces.
     *
     * @param enemySide - white or black
     * @param b - current board (can be in illegal state)
     * @param square - current square
     * @return
     */
    public static long squareAttackedBy(Side enemySide, Board b, Square square) {
        long attacks = 0L;
        attacks |= (BitOperations.getPawnAttacks(enemySide.flip())[square.ordinal()]
                & b.getBitboard(Piece.make(enemySide, PieceType.PAWN)));
        attacks |= (BitOperations.getKnightAttacks()[square.ordinal()]
                & b.getBitboard(Piece.make(enemySide, PieceType.KNIGHT)));
        attacks |= (BitOperations.getAdjacentSquares()[square.ordinal()]
                & b.getBitboard(Piece.make(enemySide, PieceType.KING)));
        attacks |= (BitOperations.getBishopMoves(square, b.getBitboard())
                & (b.getBitboard(Piece.make(enemySide, PieceType.BISHOP))
                | b.getBitboard(Piece.make(enemySide, PieceType.QUEEN))));
        attacks |= (BitOperations.getRookMoves(square, b.getBitboard())
                & (b.getBitboard(Piece.make(enemySide, PieceType.ROOK))
                | b.getBitboard(Piece.make(enemySide, PieceType.QUEEN))));

        return attacks;
    }

    /**
     * Used for realizing a move on a temporary board to find out if it is legal
     * or not.
     *
     * @param move
     * @param b
     */
    public static void pseudoMove(Move move, Board b) {
        Square from = move.getFrom();
        Square to = move.getTo();
        if (!b.getPiece(to).value().equals("NONE")) {
            b.unsetPiece(b.getPiece(to), to);
        }
        b.setPiece(b.getPiece(from), to);
        b.unsetPiece(b.getPiece(from), from);

    }

    public static boolean isMoveLegal(Move move, Board b) {
        Board tempBoard = b.clone();

        pseudoMove(move, tempBoard);
        Square kingSquare = getKingSquare(tempBoard, tempBoard.getSideToMove());
        boolean isKingMove = (move.getTo() == kingSquare);

        return !BitOperations.isKingChecked(tempBoard, kingSquare, isKingMove);
    }

    public static boolean isCheckMate(Board b) {
        MovesGenerator generator = new MovesGenerator();
        return generator.GenerateLegalMoves(b).isEmpty() && isKingAttacked(b);
    }

    public static boolean isKingAttacked(Board b) {
        return squareAttackedBy(b.getSideToMove().flip(), b, getKingSquare(b, b.getSideToMove())) != 0;
    }

    /**
     * If the king moved, then checks for all threats. Otherwise checks only for
     * file and diagonal attacks between the king and enemy
     * rooks/bishops/queens.
     *
     * @param b - current board
     * @param square - kings square
     * @param isKingMove - true if current move is a king move.
     * @return
     */
    public static boolean isKingChecked(Board b, Square square, boolean isKingMove) {

        Side enemySide = b.getSideToMove().flip();
        long allPieces = b.getBitboard();

        long enemyRooks = b.getBitboard(Piece.make(enemySide, PieceType.ROOK));
        long enemyBishops = b.getBitboard(Piece.make(enemySide, PieceType.BISHOP));
        long enemyQueens = b.getBitboard(Piece.make(enemySide, PieceType.QUEEN));

        /*
        if (isKingMove) {
            if (squareAttackedBy(enemySide, b, square) != 0) {
                return true;
            }
        }*/
        if (isKingAttacked(b)) {
            return true;
        }

        /*
        if ((enemyRooks & getRookMoves(square, allPieces)) != 0L) {  //Open file/rank between rook and king
            return true;
        }

        if ((enemyBishops & getBishopMoves(square, allPieces)) != 0L) {  //Open diagonal between bishop and king
            return true;
        }

        if ((enemyQueens & getQueenMoves(square, allPieces)) != 0L) {  //Open file/rank/diagonal between queen and king
            return true;
        }*/
        return false;
    }

    /**
     * AND operation between current bitboard and one decimal smaller bitboard.
     * Results in removing the rightmost ('least significant') bit. Used for
     * iterating through pieces on a bitboard.
     *
     * @param bitboard
     * @return
     */
    public static long removeLSB(long bitboard) {
        return bitboard & (bitboard - 1);
    }

    /**
     * Returns number of zero bits following the rightmost one-bit, which is the
     * index of the bit.
     *
     * Utilizes javas own Long.numberOfTrailingZeros
     *
     * @param bitboard
     * @return index of the first one-bit (rightmost = 'lowest' index in
     * bitboard)
     */
    public static int bitScanForward(long bitboard) {
        return Long.numberOfTrailingZeros(bitboard);
    }

    /**
     * Number of zero bits following the leftmost one-bit is reduced from 63 to
     * obtain the index of the 'highest' number in bitboard
     *
     * Utilizes javas own Long.numberOfLeadingZeros
     *
     * @param bitboard
     * @return index of the first one-bit (leftmost = 'highest' index in
     * bitboard)
     */
    public static int bitScanReverse(long bitboard) {
        return 63 - Long.numberOfLeadingZeros(bitboard);
    }
}
