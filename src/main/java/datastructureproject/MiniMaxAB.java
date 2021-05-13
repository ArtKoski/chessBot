package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;

import datastructureproject.Board.*;
import datastructureproject.Evaluation.*;
import datastructureproject.lists.LinkedList;


/**
 * MiniMax with Alpha-Beta pruning. Otherwise works like regular MiniMax class,
 * but now min/max -method calls carry on alpha and beta parameters, which can
 * be used to figure out if future branches can be left uncalculated altogether.
 *
 * @author artkoski
 */
public class MiniMaxAB {

    private MovesGenerator moveGenerator;
    private BoardEvaluation evaluator;
    int branches = 0;

    int highestValue;
    int lowestValue;

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

        LinkedList<Move> moves = moveGenerator.generateLegalMoves(board, true);

        Move bestMove = null;

        int currentValue;
        Board tempBoard = board.clone();

        for (Object moveObj : moves) {

            Move move = (Move) moveObj;
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

        //System.out.println("positions calculated " + branches);
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
        LinkedList<Move> moves = moveGenerator.generateLegalMoves(board, true);
        if (moves.isEmpty() && BoardOperations.isGameOver(board)) {
            return depth * 10000;
        }

        if (depth == 0) {
            branches++;
            return evaluator.evaluateBoard(board);
        }

        int lowestCurrentValue = Integer.MAX_VALUE;
        for (Object moveObj : moves) {
            Move move = (Move) moveObj;
            board.doMove(move);
            int whitesBestMove = max(board, (depth - 1), alpha, beta);
            board.undoMove();

            if (whitesBestMove < lowestCurrentValue) {
                lowestCurrentValue = whitesBestMove;
            }

            if (lowestCurrentValue < alpha) {
                return lowestCurrentValue;
            }

            if (lowestCurrentValue < beta) {
                beta = lowestCurrentValue;

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
        LinkedList<Move> moves = moveGenerator.generateLegalMoves(board, true);
        if (moves.isEmpty() && BoardOperations.isGameOver(board)) {
            return depth * -10000;
        }

        if (depth == 0) {
            branches++;
            return evaluator.evaluateBoard(board);
        }

        int highestCurrentValue = Integer.MIN_VALUE;
        for (Object moveObj : moves) {
            Move move = (Move) moveObj;
            board.doMove(move);
            int blacksBestMove = min(board, (depth - 1), alpha, beta);
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

        return highestCurrentValue;
    }
}
