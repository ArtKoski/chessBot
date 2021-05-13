package datastructureproject;

import datastructureproject.Board.*;
import static datastructureproject.BitOperations.getKingSquare;
import static datastructureproject.BitOperations.squareAttackedBy;

/**
 * Board related methods.
 *
 * @author artkoski
 */
public class BoardOperations {

    /**
     * Used to filter illegal moves. If a move is illegal, returns false.
     *
     * @param move - current move
     * @param b - current board
     * @return true if move is legal, otherwise false
     */
    public static boolean isMoveLegal(Move move, Board b) {
        Board tempBoard = b.clone();

        tempBoard.doMove(move);

        return !isKingAttacked(tempBoard);
    }

    public static boolean isEnemyKingCheckedAfterMove(Move move, Board b) {
        Board tempBoard = b.clone();

        tempBoard.doMove(move);
        tempBoard.setSideToMove(b.getSideToMove().flip());

        return isKingAttacked(tempBoard);
    }

    public static boolean isMoveCapture(Move move, Board b) {
        return (move.getTo().getSquareBB() & b.getBitBoard()) != 0L;

    }

    public static boolean isCheckMate(Board b) {
        MovesGenerator generator = new MovesGenerator();
        Board temp = b.clone();
        temp.setSideToMove(b.getSideToMove().flip());
        return generator.generateLegalMoves(b, false).isEmpty() && ((isKingAttacked(temp) || (b.getBitboard(Piece.makePiece(PieceType.KING, temp.getSideToMove()))) == 0L));
    }

    /**
     * Only checks for checkmate; so doesn't understand stalemates/insufficient
     * material/repetition draws.
     *
     * @param b - current board
     * @return whether the game is in terminal state or not
     */
    public static boolean isGameOver(Board b) {
        return (isCheckMate(b));
    }

    public static boolean isKingAttacked(Board b) {
        return squareAttackedBy(b.getSideToMove(), b, getKingSquare(b, b.getSideToMove().flip())) != 0;
    }

}
