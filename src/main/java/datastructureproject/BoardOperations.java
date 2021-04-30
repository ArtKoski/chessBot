package datastructureproject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import static datastructureproject.BitOperations.getKingSquare;
import static datastructureproject.BitOperations.squareAttackedBy;
import java.util.HashMap;

/**
 * Board related methods. Often relies on chesslibs methods. 
 * The class is a little messy.
 *
 * @author artkoski
 */
public class BoardOperations {

    static final HashMap<String, Integer> values = new HashMap<>();

    static {
        values.put("PAWN", 100);
        values.put("KNIGHT", 300);
        values.put("BISHOP", 300);
        values.put("ROOK", 500);
        values.put("QUEEN", 900);
        values.put("NONE", 0);
        values.put("KING", 10000);

    }

    /**
     * Used for realizing a move on a temporary board to find out if it is legal
     * or not.
     *
     * @param move - move to be realized
     * @param b - current board
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

    /**
     * Used to filter illegal moves. If a move is illegal, returns false.
     *
     * @param move - current move
     * @param b - current board
     * @return true if move is legal, otherwise false
     */
    public static boolean isMoveLegal(Move move, Board b) {
        Board tempBoard = b.clone();
        pseudoMove(move, tempBoard);
        Square kingSquare = getKingSquare(tempBoard, tempBoard.getSideToMove());
        boolean isKingMove = (move.getTo() == kingSquare);

        //System.out.println("move is " + move + " and board: " + tempBoard.getBitboard());
        return !isKingAttacked(tempBoard);
        //return !isKingChecked(tempBoard, kingSquare, isKingMove);
    }

    public static boolean isEnemyKingCheckedAfterMove(Move move, Board b) {
        Board tempBoard = b.clone();

        pseudoMove(move, tempBoard);
        tempBoard.setSideToMove(b.getSideToMove().flip());

        return isKingAttacked(tempBoard);
    }

    //Some experimental stuff
    public static boolean isEnemyKingCheckedAfterMoveTEST(Move move, Board b) {
        Board tempBoard = b.clone();

        pseudoMove(move, tempBoard);
        tempBoard.setSideToMove(b.getSideToMove().flip());

        return (isKingAttacked(tempBoard) && (squareAttackedBy(b.getSideToMove().flip(), b, move.getTo()) == 0L));

    }

    public static boolean compareCurrentSquareToDestination(Move move, Board b) {
        Square squareCurrent = move.getFrom();
        Square squareDestination = move.getTo();

        Piece piece1 = b.getPiece(squareCurrent);
        Piece piece2 = b.getPiece(squareDestination);

        if (piece2.value().equals("NONE")) {
            return false;
        }

        return (values.getOrDefault(piece1.getPieceType().toString(), 0) < values.getOrDefault(piece2.getPieceType().toString(), 0));

    }

    public static boolean isMoveCapture(Move move, Board b) {
        return (move.getTo().getBitboard() & b.getBitboard()) != 0L;

    }

    public static boolean isCheckMate(Board b) {
        MovesGenerator generator = new MovesGenerator();
        return generator.generateLegalMoves(b, false).isEmpty() && (isKingAttacked(b));
    }

    /**
     * b.* methods are not my own implementations.
     *
     * @param b - current board
     * @return whether the game is in terminal state or not
     */
    public static boolean isGameOver(Board b) {
        return (isCheckMate(b) /*| b.isStaleMate() | b.isDraw() */ || b.isInsufficientMaterial());
    }

    public static boolean isKingAttacked(Board b) {
        return squareAttackedBy(b.getSideToMove().flip(), b, getKingSquare(b, b.getSideToMove())) != 0;
    }

    /**
     * Not sure what all needs to be calculated here. Will need to come back to
     * this.
     *
     * @param b - current board
     * @param square - kings square
     * @param isKingMove - true if current move is a king move.
     * @return
     */
    public static boolean isKingChecked(Board b, Square square, boolean isKingMove) {

        Side enemySide = b.getSideToMove().flip();
        long allPieces = b.getBitboard();
        long ownPieces = b.getBitboard(b.getSideToMove());

        long enemyRooks = b.getBitboard(Piece.make(enemySide, PieceType.ROOK));
        long enemyBishops = b.getBitboard(Piece.make(enemySide, PieceType.BISHOP));
        long enemyQueens = b.getBitboard(Piece.make(enemySide, PieceType.QUEEN));

        if (isKingMove) {
            return isKingAttacked(b);
        }
        System.out.println("asd");
        if ((enemyRooks & BitOperations.getRookMoves(square, allPieces, ownPieces) | (enemyQueens & BitOperations.getQueenMoves(square, allPieces, ownPieces))) != 0L) {  //Open file/rank between rook and king
            return true;
        }
        System.out.println("asd");
        if ((enemyBishops & BitOperations.getBishopMoves(square, allPieces, ownPieces) | (enemyQueens & BitOperations.getQueenMoves(square, allPieces, ownPieces))) != 0L) {  //Open diagonal between bishop and king
            return true;
        }

        /*if ((enemyQueens & BitOperations.getQueenMoves(square, allPieces, ownPieces)) != 0L) {  //Open file/rank/diagonal between queen and king
            return true;

        }*/
        return false;
    }

}
