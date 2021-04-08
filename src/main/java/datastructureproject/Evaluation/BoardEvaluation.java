package datastructureproject.Evaluation;

import com.github.bhlangonijr.chesslib.Board;

/**
 * Interface for evaluators, as I might do more than just the SimpleEvaluator.
 *
 * @author artkoski
 */
public interface BoardEvaluation {

    int evaluateBoard(Board b);
}
