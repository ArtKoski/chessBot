package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.Evaluation.*;
import java.util.List;

/**
 * MiniMax with Alpha-Beta pruning. Otherwise an exact copy of MiniMax class,
 * but now min/max -method calls carry on alpha and beta parameters, which can
 * be used to figure out if future branches can be left uncalculated altogether.
 *
 * @author artkoski
 */
public class MiniMaxAB {

    private MovesGenerator moveGenerator;
    private BoardEvaluation evaluator;

    int highestValue = Integer.MIN_VALUE;
    int lowestValue = Integer.MAX_VALUE;

    public MiniMaxAB(Board b) {
        moveGenerator = new MovesGenerator();
        evaluator = new ComplexEvaluator();
    }

    private boolean isWhiteTurn(Board board) {
        return board.getSideToMove().value().equals("WHITE");
    }

    public Move launch(Board board, int depth) {

        highestValue = Integer.MIN_VALUE;
        lowestValue = Integer.MAX_VALUE;

        List<Move> moves = moveGenerator.generateLegalMoves(board);

        Move bestMove = null;

        int currentValue;
        Board tempBoard = board.clone();

        for (Move move : moves) {
            tempBoard.doMove(move);
            currentValue = (isWhiteTurn(tempBoard))
                    ? max(tempBoard, (depth - 1), highestValue, lowestValue)
                    : min(tempBoard, (depth - 1), highestValue, lowestValue);
            tempBoard.undoMove();

            if (isWhiteTurn(tempBoard) && currentValue >= highestValue) {
                highestValue = currentValue;
                bestMove = move;

            } else if (!isWhiteTurn(tempBoard) && currentValue <= lowestValue) {
                lowestValue = currentValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Finds the move that minimizes score.
     *
     * @param board - current board
     * @param depth - current depth
     * @param alpha - current MINIMUM score
     * @param beta - current MAXIMUM score
     * @return best score for black
     */
    public int min(Board board, int depth, int alpha, int beta) {

        if (depth == 0 || BoardOperations.isGameOver(board)) {
            return evaluator.evaluateBoard(board);
        }

        int lowestCurrentValue = Integer.MAX_VALUE;
        for (Move move : moveGenerator.generateLegalMoves(board)) {
            board.doMove(move);
            int whitesBestMove = max(board, (depth - 1), alpha, beta);
            board.undoMove();

            lowestCurrentValue = Math.min(whitesBestMove, lowestCurrentValue);

            beta = Math.min(beta, whitesBestMove);
            if (beta <= alpha) {
                return Integer.MIN_VALUE;
            }
        }

        return lowestCurrentValue;
    }

    /**
     * Finds the move that maximises score.
     *
     * @param board - current board
     * @param depth - current depth
     * @param alpha - current MINIMUM
     * @param beta - current MAXIMUM
     * @return best score for white
     */
    public int max(Board board, int depth, int alpha, int beta) {
        if (depth == 0 || BoardOperations.isGameOver(board)) {
            return evaluator.evaluateBoard(board);
        }

        int highestCurrentValue = Integer.MIN_VALUE;
        for (Move move : moveGenerator.generateLegalMoves(board)) {
            board.doMove(move);
            int blacksBestMove = min(board, (depth - 1), alpha, beta);
            board.undoMove();

            highestCurrentValue = Math.max(highestCurrentValue, blacksBestMove);

            alpha = Math.max(alpha, blacksBestMove);
            if (beta <= alpha) {
                return Integer.MAX_VALUE;
            }
        }

        return highestCurrentValue;
    }
}
