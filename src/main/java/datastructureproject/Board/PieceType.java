package datastructureproject.Board;

/**
 *
 * @author artkoski
 */
public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public static PieceType fromValue(String v) {
        return valueOf(v);
    }
}
