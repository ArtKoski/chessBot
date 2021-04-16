package datastructureproject.Evaluation;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import datastructureproject.BoardOperations;
import datastructureproject.MovesGenerator;

/**
 * Extends SimpleEvaluator. Added evaluation metrics are mobility, checks, ?.
 * Not actually complex.
 *
 * @author artkoski
 */
public class ComplexEvaluator extends SimpleEvaluator implements BoardEvaluation {

    MovesGenerator moveGen = new MovesGenerator();
    int CHECK_BONUS = 50;

    @Override
    public int evaluateSide(Side side, Board board) {
        return super.evaluateSide(side, board) + super.isCheckMate(side, board) + (5 * mobilityBonus(side, board)) + checkBonus(side, board);
    }

    private int mobilityBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side);
        return moveGen.generateLegalMoves(tempBoard, false).size();
    }

    private int checkBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side.flip());
        return BoardOperations.isKingAttacked(tempBoard) ? CHECK_BONUS : 0;
    }

}
