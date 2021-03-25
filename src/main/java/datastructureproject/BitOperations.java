package datastructureproject;

import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

/**
 * Provides some bitboard operations to solve the possible moves.
 */
public class BitOperations {

    //Copied from bhlangonijr's chesslib.
    final static long[] rankBB = {
        0x00000000000000FFL, 0x000000000000FF00L, 0x0000000000FF0000L, 0x00000000FF000000L,
        0x000000FF00000000L, 0x0000FF0000000000L, 0x00FF000000000000L, 0xFF00000000000000L
    };
    final static long[] fileBB = {
        0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
        0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
    };

    private static final long[] knightAttacks = new long[64];
    private static final long[] rookRays = new long[64];
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

            whitePawnAttacks[x] |= ((currentBB & 0x7f7f7f7f7f7f7fL) << 9)
                    | ((currentBB & 0xfefefefefefefeL) << 7);

            whitePawnMoves[x] |= ((currentBB & 0xffffffffffffffL) << 8)
                    | ((currentBB & 0xff00L) << 16);

            blackPawnAttacks[x] |= ((currentBB & 0xfefefefefefefe00L) >> 9)
                    | ((currentBB & 0x7f7f7f7f7f7f7f00L) >> 7);

            blackPawnMoves[x] |= ((currentBB & 0xffffffffffffff00L) >> 8)
                    | ((currentBB & 0xff000000000000L) >> 16);
        }
        blackPawnAttacks[63] = 0x0040000000000000L;
        blackPawnMoves[63] = 0x0080000000000000L;//bug again 
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
            currentBB = current.getBitboard();                   //Masks:
            attacks |= ((currentBB & 0x00007F7F7F7F7F7FL) << 17) //All but ranks 7,8 and file H
                    | ((currentBB & 0x003F3F3F3F3F3F3FL) << 10) //All but rank 8 and files G,H
                    | ((currentBB & 0x3F3F3F3F3F3F3F00L) >> 6) //ETC.
                    | ((currentBB & 0x7F7F7F7F7F7F0000L) >> 15)
                    | ((currentBB & 0x0000FEFEFEFEFEFEL) << 15)
                    | ((currentBB & 0x00FCFCFCFCFCFCFCL) << 6)
                    | ((currentBB & 0xFCFCFCFCFCFCFC00L) >> 10)
                    | ((currentBB & 0xFEFEFEFEFEFE0000L) >> 17);

            knightAttacks[x] = attacks;

        }
        knightAttacks[63] = 0x0020400000000000L; //Weird bug (had to hardcode this)
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

            attacks |= ((currentBB & 0xffffffffffffff00L) >> 8) //All but ranks 7,8 and file H
                    | ((currentBB & 0xffffffffffffffL) << 8) //All but rank 8 and files G,H
                    | ((currentBB & 0xfefefefefefefefeL) >> 1) //ETC.
                    | ((currentBB & 0x7f7f7f7f7f7f7f7fL) << 1)
                    | ((currentBB & 0xfefefefefefefe00L) >> 9)
                    | ((currentBB & 0x7f7f7f7f7f7f7fL) << 9)
                    | ((currentBB & 0x7f7f7f7f7f7f7f00L) >> 7)
                    | ((currentBB & 0xfefefefefefefeL) << 7);

            adjacentSquares[x] = attacks;
        }
        adjacentSquares[63] = 0x40c0000000000000L;
    }

    /**
     * Generates the bishop rays to different directions using some utility functions.
     */
    static {
        Square current;
        for (int x = 0; x <= 63; x++) {

            current = Square.squareAt(x);
            bishopRays[0][x] = getBishopRaysSENW(current, "NW");
            bishopRays[1][x] = getBishopRaysNESW(current, "NE");
            bishopRays[2][x] = getBishopRaysSENW(current, "SE");
            bishopRays[3][x] = getBishopRaysNESW(current, "SW");
        }
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

    public static long getKnightMoves(Square square, long ownPieces) {
        return knightAttacks[square.ordinal()] & ownPieces;
    }

    /**
     * Returns available pawn moves (not captures). If no pieces on the way, the
     * moves are returned after an AND operation with the possible moves and the
     * unoccupied squares.
     *
     * @param square
     * @param side
     * @param allPieces
     * @return
     */
    public static long getPawnMoves(Square square, Side side, long allPieces) {
        long pawnMoves;
        long occupied = allPieces;
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
     * Return normal pawn attacks. No 'en passant' atm.
     *
     * @param square
     * @param enemyPieces
     * @param side
     * @return
     */
    public static long getPawnCaptures(Square square, long enemyPieces, Side side) {
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
     * forward/reverse scan to figure calculate bishops possible attack squares.
     *
     * @param square
     * @param occupied
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
        return (getBishopMoves(square, occupied)|getRookMoves(square, occupied));
    }

    /**
     * Need to clean up a little Calculates the diagonals from the defined
     * square. Handles NE and SW rays. (North-East, South-West) Used for
     * generating a pre-calculated array for the bishops 'rays'.
     *
     * @param square
     * @param way
     * @return
     */
    public static long getBishopRaysNESW(Square square, String way) {
        long bishopAttacks = 0L;
        long nextSquare;
        Square tempSquare = square;

        while (!((way.equals("NE")) ? tempSquare.getFile().equals(File.FILE_H) | tempSquare.getRank().equals(Rank.RANK_8) : tempSquare.getFile().equals(File.FILE_A) | tempSquare.getRank().equals(Rank.RANK_1))) {
            nextSquare = (way.equals("NE")) ? tempSquare.getBitboard() << 9 : tempSquare.getBitboard() >> 9;
            bishopAttacks |= nextSquare;

            tempSquare = Square.squareAt(bitScanForward(nextSquare));
        }
        return bishopAttacks;

    }

    /**
     * Calculates the diagonals from the defined square. Handles SE and NW rays
     * (South-East, North-West). Used for generating a pre-calculated array for
     * the bishops 'rays'.
     *
     * @param square
     * @param way
     * @return
     */
    public static long getBishopRaysSENW(Square square, String way) {
        long bishopAttacks = 0L;
        long nextSquare;
        Square tempSquare = square;

        while (!((way.equals("NW")) ? tempSquare.getFile().equals(File.FILE_A) | tempSquare.getRank().equals(Rank.RANK_8) : tempSquare.getFile().equals(File.FILE_H) | tempSquare.getRank().equals(Rank.RANK_1))) {
            nextSquare = (way.equals("NW")) ? tempSquare.getBitboard() << 7 : tempSquare.getBitboard() >> 7;
            bishopAttacks |= nextSquare;

            tempSquare = Square.squareAt(bitScanForward(nextSquare));
        }
        return bishopAttacks;
    }

    /**
     * Needs rework.
     *
     * @param square
     * @param occupied
     * @return
     */
    public static long getRookMoves(Square square, long occupied) {

        long rookAttacks = getRookFileAttacks(square, occupied, "RIGHT")
                | getRookFileAttacks(square, occupied, "LEFT")
                | getRookRankAttacks(square, occupied, "UP")
                | getRookRankAttacks(square, occupied, "DOWN");

        return rookAttacks;
    }

    public static long getRookFileAttacks(Square square, long occupied, String way) {
        long rookAttacks = 0L;
        long nextSquare;
        Square tempSquare = square;

        File borderFile = (way.equals("RIGHT")) ? File.FILE_H : File.FILE_A;

        while (!tempSquare.getFile().equals(borderFile)) {
            nextSquare = (borderFile.equals(File.FILE_H)) ? tempSquare.getBitboard() << 1 : tempSquare.getBitboard() >> 1;
            long nextPiece = nextSquare & occupied;
            if (nextPiece != 0L) {
                rookAttacks |= nextSquare;
                break;
            }
            rookAttacks |= nextSquare;

            tempSquare = Square.squareAt(bitScanForward(nextSquare));
        }
        return rookAttacks;

    }

    public static long getRookRankAttacks(Square square, long occupied, String way) {
        long rookAttacks = 0L;
        long nextSquare;
        Square tempSquare = square;

        Rank borderRank = (way.equals("UP")) ? Rank.RANK_8 : Rank.RANK_1;

        while (!tempSquare.getRank().equals(borderRank)) {
            nextSquare = (borderRank.equals(Rank.RANK_8)) ? tempSquare.getBitboard() << 8 : tempSquare.getBitboard() >> 8;
            long nextPiece = nextSquare & occupied;
            if (nextPiece != 0L) {
                rookAttacks |= nextSquare;
                break;
            }
            rookAttacks |= nextSquare;

            tempSquare = Square.squareAt(bitScanForward(nextSquare));
        }
        return rookAttacks;

    }

    public static long getKingMoves(Square square, long ownPieces) {
        return adjacentSquares[square.ordinal()] & ~ownPieces;
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
     * Utilizes javas own {@link Long.numberOfTrailingZeros}
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
     * Utilizes javas own {@link Long.numberOfLeadingZeros}
     *
     * @param bitboard
     * @return index of the first one-bit (leftmost = 'highest' index in
     * bitboard)
     */
    public static int bitScanReverse(long bitboard) {
        return 63 - Long.numberOfLeadingZeros(bitboard);
    }
}
