package datastructureproject.Board;

import java.util.HashMap;

/**
 *
 * @author artkoski
 */
public enum Piece {
    WHITE_PAWN,
    WHITE_KNIGHT,
    WHITE_BISHOP,
    WHITE_ROOK,
    WHITE_QUEEN,
    WHITE_KING,
    BLACK_PAWN,
    BLACK_KNIGHT,
    BLACK_BISHOP,
    BLACK_ROOK,
    BLACK_QUEEN,
    BLACK_KING,
    NONE;

    private static final Piece[] newPiece = {
        WHITE_PAWN,
        WHITE_KNIGHT,
        WHITE_BISHOP,
        WHITE_ROOK,
        WHITE_QUEEN,
        WHITE_KING,
        BLACK_PAWN,
        BLACK_KNIGHT,
        BLACK_BISHOP,
        BLACK_ROOK,
        BLACK_QUEEN,
        BLACK_KING,
        NONE};

    static final HashMap<Piece, PieceType> pieceTypes;
    static final HashMap<Piece, Side> pieceSides;

    static {
        pieceTypes = new HashMap<>();
        pieceTypes.put(WHITE_PAWN, PieceType.PAWN);
        pieceTypes.put(WHITE_KNIGHT, PieceType.KNIGHT);
        pieceTypes.put(WHITE_BISHOP, PieceType.BISHOP);
        pieceTypes.put(WHITE_ROOK, PieceType.ROOK);
        pieceTypes.put(WHITE_QUEEN, PieceType.QUEEN);
        pieceTypes.put(WHITE_KING, PieceType.KING);
        pieceTypes.put(BLACK_PAWN, PieceType.PAWN);
        pieceTypes.put(BLACK_KNIGHT, PieceType.KNIGHT);
        pieceTypes.put(BLACK_BISHOP, PieceType.BISHOP);
        pieceTypes.put(BLACK_ROOK, PieceType.ROOK);
        pieceTypes.put(BLACK_QUEEN, PieceType.QUEEN);
        pieceTypes.put(BLACK_KING, PieceType.KING);
    }

    public PieceType getPieceType() {
        return pieceTypes.get(this);
    }

    static {
        pieceSides = new HashMap<>();
        pieceSides.put(WHITE_PAWN, Side.WHITE);
        pieceSides.put(WHITE_KNIGHT, Side.WHITE);
        pieceSides.put(WHITE_BISHOP, Side.WHITE);
        pieceSides.put(WHITE_ROOK, Side.WHITE);
        pieceSides.put(WHITE_QUEEN, Side.WHITE);
        pieceSides.put(WHITE_KING, Side.WHITE);
        pieceSides.put(BLACK_PAWN, Side.BLACK);
        pieceSides.put(BLACK_KNIGHT, Side.BLACK);
        pieceSides.put(BLACK_BISHOP, Side.BLACK);
        pieceSides.put(BLACK_ROOK, Side.BLACK);
        pieceSides.put(BLACK_QUEEN, Side.BLACK);
        pieceSides.put(BLACK_KING, Side.BLACK);
    }

    public Side getPieceSide() {
        return pieceSides.get(this);
    }

    public String value() {
        return name();
    }

    public static Piece makePiece(PieceType pieceType, Side side) {
        return side.equals(Side.WHITE) ? newPiece[pieceType.ordinal()] : newPiece[pieceType.ordinal() + 6];
    }

    public static Piece makeNone() {
        return newPiece[12];
    }

    public static Piece fromValue(String v) {
        return valueOf(v);
    }
}
