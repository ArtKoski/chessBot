package datastructureproject.Board;

import datastructureproject.lists.LinkedList;

/**
 * My own implementation of the (bit)board which is mostly based on chessLibs
 * board. There are bitboards for each piece and each side. The pieces also have
 * their own list to update their positions on the board. Does not check for
 * legality of moves ever.
 *
 * @author artkoski
 */
public class Board implements Cloneable {

    private Side sideToMove;
    /**
     * Contains bitboards for each piece. Indexes are same as Piece.ordinals.
     */
    private long[] bitboard;

    /**
     * Contains bitboards for each side. Index 0 is white, 1 is black.
     */
    private long[] bitboardSide;

    /**
     * Index refers to the square which the piece is on. So if 'pieces[0] =
     * Piece.WHITE_ROOK', that means there is a white rook on square A1.
     */
    private final Piece[] pieces;
    private LinkedList<Move> history;
    private LinkedList<Piece> pieceHistory;
    private LinkedList<Piece> targetPieceHistory;

    /**
     * Initializes the board to the starting position.
     */
    public Board() {
        pieces = new Piece[64];
        history = new LinkedList<>();
        pieceHistory = new LinkedList<>();
        targetPieceHistory = new LinkedList<>();

        //
        bitboard = new long[12];
        bitboardSide = new long[2];

        bitboard[0] = 0xff00L;
        bitboard[1] = 0x42L;
        bitboard[2] = 0x24L;
        bitboard[3] = 0x81L;
        bitboard[4] = 0x8L;
        bitboard[5] = 0x10L;

        bitboard[6] = 0xff000000000000L;
        bitboard[7] = 0x4200000000000000L;
        bitboard[8] = 0x2400000000000000L;
        bitboard[9] = 0x8100000000000000L;
        bitboard[10] = 0x800000000000000L;
        bitboard[11] = 0x1000000000000000L;

        for (int i = 0; i < 12; i++) {
            if (i < 6) {
                bitboardSide[0] |= bitboard[i];
            } else {
                bitboardSide[1] |= bitboard[i];
            }
        }
        //

        pieces[0] = Piece.makePiece(PieceType.ROOK, Side.WHITE);
        pieces[7] = Piece.makePiece(PieceType.ROOK, Side.WHITE);
        pieces[1] = Piece.makePiece(PieceType.KNIGHT, Side.WHITE);
        pieces[6] = Piece.makePiece(PieceType.KNIGHT, Side.WHITE);
        pieces[2] = Piece.makePiece(PieceType.BISHOP, Side.WHITE);
        pieces[5] = Piece.makePiece(PieceType.BISHOP, Side.WHITE);
        pieces[3] = Piece.makePiece(PieceType.QUEEN, Side.WHITE);
        pieces[4] = Piece.makePiece(PieceType.KING, Side.WHITE);
        for (int i = 8; i <= 15; i++) {
            pieces[i] = Piece.makePiece(PieceType.PAWN, Side.WHITE);
        }
        pieces[56] = Piece.makePiece(PieceType.ROOK, Side.BLACK);
        pieces[63] = Piece.makePiece(PieceType.ROOK, Side.BLACK);
        pieces[57] = Piece.makePiece(PieceType.KNIGHT, Side.BLACK);
        pieces[62] = Piece.makePiece(PieceType.KNIGHT, Side.BLACK);
        pieces[58] = Piece.makePiece(PieceType.BISHOP, Side.BLACK);
        pieces[61] = Piece.makePiece(PieceType.BISHOP, Side.BLACK);
        pieces[59] = Piece.makePiece(PieceType.QUEEN, Side.BLACK);
        pieces[60] = Piece.makePiece(PieceType.KING, Side.BLACK);
        for (int i = 48; i <= 55; i++) {
            pieces[i] = Piece.makePiece(PieceType.PAWN, Side.BLACK);
        }
        for (int i = 16; i <= 47; i++) {
            pieces[i] = Piece.makeNone();
        }

        setSideToMove(Side.WHITE);
    }

    public long getBitBoard() {
        return (bitboardSide[0] | bitboardSide[1]);
    }

    public long getBitboard(Side side, PieceType type) {
        return bitboard[Piece.makePiece(type, side).ordinal()];
    }

    public long getBitboard(Side side) {
        return side.equals(Side.WHITE) ? bitboardSide[0] : bitboardSide[1];
    }

    public long getBitboard(Piece piece) {
        return bitboard[piece.ordinal()];

    }

    public Piece getPiece(Square square) {
        return pieces[square.ordinal()];

    }

    public void setSideToMove(Side side) {
        this.sideToMove = side;
    }

    public Side getSideToMove() {
        return this.sideToMove;
    }

    /**
     * Makes a move on the board. Assumes that move is legal.
     *
     * @param move - the move to be realized
     */
    public void doMove(Move move) {
        history.addFirst(move);
        pieceHistory.addFirst(pieces[move.getFrom().ordinal()]);
        targetPieceHistory.addFirst(pieces[move.getTo().ordinal()]);

        Piece pieceTo = getPiece(move.getTo());
        Piece pieceFrom = getPiece(move.getFrom());
        updateBitBoards(move, pieceFrom, pieceTo);

        pieces[move.getTo().ordinal()] = pieces[move.getFrom().ordinal()];
        pieces[move.getFrom().ordinal()] = Piece.NONE;
        setSideToMove(getSideToMove().flip());
    }

    /**
     * Undos a move on the board.
     */
    public void undoMove() {
        Move lastMove = (Move) history.pollFirst();
        if (lastMove != null) {

            Piece pieceFrom = (Piece) pieceHistory.pollFirst();
            Piece pieceTo = (Piece) targetPieceHistory.pollFirst();
            updateBitBoards(lastMove, pieceFrom, pieceTo);

            pieces[lastMove.getFrom().ordinal()] = pieceFrom;
            pieces[lastMove.getTo().ordinal()] = pieceTo;

            setSideToMove(getSideToMove().flip());

        } else {
            System.out.println("Nothing to undo..");
        }
    }

    /**
     * Updates the bitboard with XOR -operations.
     *
     * @param move
     * @param from
     * @param to
     */
    private void updateBitBoards(Move move, Piece from, Piece to) {
        long moveBB = (move.getFrom().getSquareBB() | move.getTo().getSquareBB());
        Piece pieceFrom = from;
        Piece pieceTo = to;

        bitboard[pieceFrom.ordinal()] ^= moveBB;
        if (pieceFrom.getPieceSide() == Side.WHITE) {
            bitboardSide[0] ^= moveBB;
        } else {
            bitboardSide[1] ^= moveBB;
        }

        if (pieceTo != Piece.NONE) {
            long targetBB = move.getTo().getSquareBB();
            bitboard[pieceTo.ordinal()] ^= targetBB;

            if (pieceTo.getPieceSide() == Side.WHITE) {
                bitboardSide[0] ^= targetBB;
            } else {
                bitboardSide[1] ^= targetBB;
            }
        }
    }

    /**
     * Set a piece on a specific square. No checking for legality etc.
     *
     * @param piece - piece to set
     * @param square - square to set the piece on
     */
    public void setPiece(Piece piece, Square square) {
        pieces[square.ordinal()] = piece;
        long squareBB = square.getSquareBB();

        bitboard[piece.ordinal()] |= squareBB;
        if (piece.getPieceSide() == Side.WHITE) {
            bitboardSide[0] |= squareBB;
        } else {
            bitboardSide[1] |= squareBB;
        }
    }

    /**
     * Unset a piece on a specific square. Assumes that the piece exists before
     * removing.
     *
     * @param piece - piece to unset
     * @param square - square to unset the piece on
     */
    public void unsetPiece(Piece piece, Square square) {
        pieces[square.ordinal()] = Piece.makeNone();
        long squareBB = square.getSquareBB();

        bitboard[piece.ordinal()] ^= squareBB;
        if (piece.getPieceSide() == Side.WHITE) {
            bitboardSide[0] ^= squareBB;
        } else {
            bitboardSide[1] ^= squareBB;
        }

    }

    @Override
    public Board clone() {
        Board copy = new Board();
        copy.setBitBoard(bitboard, bitboardSide, pieces);
        copy.setSideToMove(this.getSideToMove());
        return copy;
    }

    private void setBitBoard(long[] bb, long[] bbSide, Piece[] pieces) {
        for (int i = 0; i < 12; i++) {
            this.bitboard[i] = bb[i];
        }
        this.bitboardSide[0] = bbSide[0];
        this.bitboardSide[1] = bbSide[1];
        for (int i = 0; i <= 63; i++) {
            this.pieces[i] = pieces[i];
        }
    }

    public void clear() {
        for (int i = 0; i < 12; i++) {
            bitboard[i] = 0x0L;
        }
        bitboardSide[0] = 0x0L;
        bitboardSide[1] = 0x0L;

        for (int i = 0; i <= 63; i++) {
            pieces[i] = Piece.makeNone();
        }
        setSideToMove(Side.WHITE);

    }

    public boolean equals(Board t) {
        return (this.getBitBoard() == t.getBitBoard() && this.getSideToMove() == t.getSideToMove());
    }

}
