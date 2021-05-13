package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;

import datastructureproject.Board.*;
import datastructureproject.Evaluation.*;
import datastructureproject.lists.*;

/**
 *
 * MiniMax algorithm. Picks the best evaluated move with a given depth. Becomes
 * inefficient (slow) beyond depth 3.
 *
 * @author artkoski
 */
public class MiniMax {

    private MovesGenerator moveGenerator;
    private BoardEvaluation evaluator;
    int branches = 0;

    public MiniMax(Board b) {
        moveGenerator = new MovesGenerator();
        evaluator = new ComplexEvaluator();
    }

    private boolean isWhiteTurn(Board board) {
        return board.getSideToMove().value().equals("WHITE");
    }

    /**
     * To use minimax, launch is called. Expected to be used when it is the
     * players(bots) turn.
     *
     * @param board - current board
     * @param depth - how deep the algorithm calculates
     * @return the best evaluated move with given depth
     */
    public Move launch(Board board, int depth) {

        LinkedList<Move> moves = moveGenerator.generateLegalMoves(board, true);

        Move bestMove = null;

        int highestValue = Integer.MIN_VALUE;
        int lowestValue = Integer.MAX_VALUE;
        int currentValue;
        Board tempBoard = board.clone();

        for (Object moveObj : moves) {
            Move move = (Move) moveObj;
            tempBoard.doMove(move);
            currentValue = (isWhiteTurn(tempBoard))
                    ? max(tempBoard, (depth - 1))
                    : min(tempBoard, (depth - 1));
            tempBoard.undoMove();

            if (isWhiteTurn(tempBoard) && currentValue >= highestValue) {
                highestValue = currentValue;
                bestMove = move;

            } else if (!isWhiteTurn(tempBoard) && currentValue <= lowestValue) {
                lowestValue = currentValue;
                bestMove = move;
            }

        }
        //System.out.println("positions calculated " + branches);
        return bestMove;
    }

    /**
     * Part of the algorithm. Used for finding the move that MINIMIZES score,
     * AKA the best move for BLACK.
     *
     * @param board - current board
     * @param depth - current depth
     * @return current branch evaluation
     */
    public int min(Board board, int depth) {

        if (BoardOperations.isGameOver(board)) {
            return depth * 10000;
        }

        if (depth == 0) {
            branches++;
            return evaluator.evaluateBoard(board);
        }

        int lowestValue = Integer.MAX_VALUE;
        for (Object moveObj : moveGenerator.generateLegalMoves(board, true)) {

            Move move = (Move) moveObj;
            board.doMove(move);
            int enemysBestMove = max(board, (depth - 1));
            board.undoMove();
            if (enemysBestMove < lowestValue) {
                lowestValue = enemysBestMove;
            }

        }

        return lowestValue;
    }

    /**
     * Part of the algorithm. Used for finding the move that MAXIMIZES score,
     * AKA the best move for WHITE.
     *
     * @param board - current board
     * @param depth - current depth
     * @return current branch evaluation
     */
    public int max(Board board, int depth) {

        if (BoardOperations.isGameOver(board)) {
            return depth * -10000;
        }
        if (depth == 0) {
            branches++;
            return evaluator.evaluateBoard(board);
        }

        int highestValue = Integer.MIN_VALUE;
        for (Object moveObj : moveGenerator.generateLegalMoves(board, true)) {
            Move move = (Move) moveObj;
            board.doMove(move);
            int enemysBestMove = min(board, (depth - 1));
            board.undoMove();
            if (enemysBestMove > highestValue) {
                highestValue = enemysBestMove;
            }

        }

        return highestValue;
    }
}
