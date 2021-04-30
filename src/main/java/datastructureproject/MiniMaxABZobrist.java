package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.Evaluation.*;
import datastructureproject.lists.LinkedList;
import datastructureproject.lists.MoveList;
import java.util.HashMap;
import java.util.List;

/**
 * MiniMax with Alpha-Beta pruning and Zobrist Hashing. Alpha & Beta parameters
 * are used to leave out unpromising branches uncalculated, whereas Zobrist
 * Hashing is used so that the same position won't be calulated twice.
 *
 * @author artkoski
 */
public class MiniMaxABZobrist {

    private MovesGenerator moveGenerator;
    private ComplexEvaluator evaluator;

    int highestValue;
    int lowestValue;

    public MiniMaxABZobrist(Board b) {
        moveGenerator = new MovesGenerator();
        evaluator = new ComplexEvaluator();
    }

    private boolean isWhiteTurn(Board board) {
        return board.getSideToMove().value().equals("WHITE");
    }

    public Move launch(Board board, int depth) {

        highestValue = Integer.MIN_VALUE;
        lowestValue = Integer.MAX_VALUE;

        MoveList moves = moveGenerator.generateLegalMoves(board, true);

        Move bestMove = null;

        int currentValue;
        long zobristHash = Zobrist.getKeyForBoard(board);

        for (Move move : moves) {
            long newHash = Zobrist.getKeyForMove(move, board) ^ zobristHash;

            board.doMove(move);
            currentValue = (isWhiteTurn(board))
                    ? max(board, (depth - 1), highestValue, lowestValue, newHash)
                    : min(board, (depth - 1), highestValue, lowestValue, newHash);
            board.undoMove();

            if (isWhiteTurn(board) && currentValue >= highestValue) {
                highestValue = currentValue;
                bestMove = move;

            } else if (!isWhiteTurn(board) && currentValue <= lowestValue) {
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
     * @param hash - current board hash value
     * @return best score for black
     */
    public int min(Board board, int depth, int alpha, int beta, long hash) {
        MoveList moves = moveGenerator.generateLegalMoves(board, true);
        if (moves.isEmpty() && BoardOperations.isGameOver(board)) {
            return depth * 10000;
        }

        if (depth == 0) {
            return evaluator.evaluateBoard(board, hash, true);
        }

        int lowestCurrentValue = Integer.MAX_VALUE;
        for (Move move : moves) {
            long newHash = Zobrist.getKeyForMove(move, board) ^ hash;

            board.doMove(move, false);
            int whitesBestMove = max(board, (depth - 1), alpha, beta, newHash);
            board.undoMove();

            if (whitesBestMove < lowestCurrentValue) {
                lowestCurrentValue = whitesBestMove;
            }

            if (lowestCurrentValue < alpha) {
                return lowestCurrentValue;
            }

            if (lowestCurrentValue < beta) {
                beta = lowestCurrentValue;

                /*
            lowestCurrentValue = Math.min(whitesBestMove, lowestCurrentValue);

            beta = Math.min(beta, whitesBestMove);
            if (beta <= alpha) {
                return Integer.MIN_VALUE;
            }*/
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
     * @param hash - current board hash value
     * @return best score for white
     */
    public int max(Board board, int depth, int alpha, int beta, long hash) {
        MoveList moves = moveGenerator.generateLegalMoves(board, true);
        if (moves.isEmpty() && BoardOperations.isGameOver(board)) {
            return depth * -10000;
        }

        if (depth == 0/* || BoardOperations.isGameOver(board)*/) {
            return evaluator.evaluateBoard(board, hash, true);
        }

        int highestCurrentValue = Integer.MIN_VALUE;
        for (Move move : moveGenerator.generateLegalMoves(board, true)) {
            long newHash = Zobrist.getKeyForMove(move, board) ^ hash;

            board.doMove(move, false);
            int blacksBestMove = min(board, (depth - 1), alpha, beta, newHash);
            board.undoMove();

            if (blacksBestMove > highestCurrentValue) {
                highestCurrentValue = blacksBestMove;
            }

            if (highestCurrentValue > beta) {
                return highestCurrentValue;
            }

            if (highestCurrentValue > alpha) {
                alpha = highestCurrentValue;
            }
        }

        /*
        highestCurrentValue = Math.max(highestCurrentValue, blacksBestMove);

            alpha = Math.max(alpha, blacksBestMove);
            if (beta <= alpha) {
                return Integer.MAX_VALUE;
            }
        }

         */
        return highestCurrentValue;
    }
}
