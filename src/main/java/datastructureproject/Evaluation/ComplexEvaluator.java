package datastructureproject.Evaluation;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import datastructureproject.BitOperations;
import datastructureproject.BoardOperations;
import datastructureproject.MovesGenerator;
import java.util.HashMap;

/**
 * Extends SimpleEvaluator. Added evaluation metrics are mobility, checks,
 * Not actually complex.
 * @author artkoski
 */
public class ComplexEvaluator extends SimpleEvaluator implements BoardEvaluation {

    MovesGenerator moveGen = new MovesGenerator();
    int CHECK_BONUS = 50;

    @Override
    public int evaluateSide(Side side, Board board) {
        return super.evaluateSide(side, board) + super.isCheckMate(side, board) /*+ mobilityBonus(side, board)*/ + checkBonus(side, board);
    }

    //Needs better implementation.......
    private int mobilityBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side);
        return moveGen.generateLegalMoves(tempBoard).size();
    }

    private int checkBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side);
        return BoardOperations.isKingAttacked(tempBoard) ? CHECK_BONUS : 0;
    }

}
