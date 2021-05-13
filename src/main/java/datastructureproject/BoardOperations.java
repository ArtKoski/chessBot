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
        Piece movingPiece = b.getPiece(move.getFrom());

        tempBoard.doMove(move);
        Square kingSquare = getKingSquare(tempBoard, tempBoard.getSideToMove().flip());
        if (kingSquare == Square.NONE) {
            return true;
        }

        boolean isKingMove = ((movingPiece == Piece.BLACK_KING) || (movingPiece == Piece.WHITE_KING));

        boolean kingAttacked = isKingAttacked(tempBoard);
        return !kingAttacked;
        //   return !isKingChecked(tempBoard, kingSquare, isKingMove);
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

        Side enemySide = b.getSideToMove();
        long allPieces = b.getBitBoard();
        long ownPieces = b.getBitboard(b.getSideToMove().flip());

        long enemyRooks = b.getBitboard(Piece.makePiece(PieceType.ROOK, enemySide));
        long enemyBishops = b.getBitboard(Piece.makePiece(PieceType.BISHOP, enemySide));
        long enemyQueens = b.getBitboard(Piece.makePiece(PieceType.QUEEN, enemySide));

        if (isKingMove) {
            return isKingAttacked(b);
        }
        if ((enemyRooks & BitOperations.getRookMoves(square, allPieces, ~ownPieces) | (enemyQueens & BitOperations.getQueenMoves(square, allPieces, ~ownPieces))) != 0L) {  //Open file/rank between rook and king
            return true;
        }
        if ((enemyBishops & BitOperations.getBishopMoves(square, allPieces, ~ownPieces) | (enemyQueens & BitOperations.getQueenMoves(square, allPieces, ~ownPieces))) != 0L) {  //Open diagonal between bishop and king
            return true;
        }
        return false;
    }
}
