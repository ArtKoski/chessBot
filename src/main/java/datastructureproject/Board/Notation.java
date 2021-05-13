package datastructureproject.Board;

import java.util.HashMap;

/**
 *
 * @author artkoski
 */
public class Notation {

    static final HashMap<Piece, String> notations;

    static {
        notations = new HashMap<>();
        notations.put(Piece.WHITE_PAWN, "P");
        notations.put(Piece.WHITE_KNIGHT, "N");
        notations.put(Piece.WHITE_BISHOP, "B");
        notations.put(Piece.WHITE_ROOK, "R");
        notations.put(Piece.WHITE_QUEEN, "Q");
        notations.put(Piece.WHITE_KING, "K");
        notations.put(Piece.BLACK_PAWN, "p");
        notations.put(Piece.BLACK_KNIGHT, "n");
        notations.put(Piece.BLACK_BISHOP, "b");
        notations.put(Piece.BLACK_ROOK, "r");
        notations.put(Piece.BLACK_QUEEN, "q");
        notations.put(Piece.BLACK_KING, "k");
    }

    public static String getPieceNotation(Piece piece) {
        return notations.get(piece);
    }
}
