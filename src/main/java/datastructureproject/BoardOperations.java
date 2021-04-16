package datastructureproject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import static datastructureproject.BitOperations.getKingSquare;
import static datastructureproject.BitOperations.squareAttackedBy;

/**
 * Board related methods. Often relies on chesslibs methods.
 *
 * @author artkoski
 */
public class BoardOperations {

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

        return !isKingAttacked(tempBoard);
        // return !isKingChecked(tempBoard, kingSquare, isKingMove);
    }

    public static boolean isEnemyKingCheckedAfterMove(Move move, Board b) {
        Board tempBoard = b.clone();

        pseudoMove(move, tempBoard);
        tempBoard.setSideToMove(b.getSideToMove().flip());

        return isKingAttacked(tempBoard);
    }

    public static boolean isMoveCapture(Move move, Board b) {
        return (move.getTo().getBitboard() & b.getBitboard()) != 0L;

    }

    //Temporarily not own implementation in use.
    public static boolean isCheckMate(Board b) {
        return b.isMated();
        /*    MovesGenerator generator = new MovesGenerator();
        return generator.generateLegalMoves(b).isEmpty() && isKingAttacked(b);
         */
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

}
