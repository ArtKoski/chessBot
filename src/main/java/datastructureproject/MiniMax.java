package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.Evaluation.*;
import java.util.List;

/**
 *
 * @author artkoski
 */
public class MiniMax {

    private MovesGenerator moveGenerator;
    private BoardEvaluation evaluator;

    public MiniMax(Board b) {
        moveGenerator = new MovesGenerator();
        evaluator = new SimpleEvaluator();
    }

    private boolean isWhiteTurn(Board board) {
        return board.getSideToMove().value().equals("WHITE");
    }

    public Move launch(Board board, int depth) {

        List<Move> moves = moveGenerator.GenerateLegalMoves(board);

        Move bestMove = null;

        int highestValue = Integer.MIN_VALUE;
        int lowestValue = Integer.MAX_VALUE;
        int currentValue;
        Board tempBoard = board.clone();

        for (Move move : moves) {
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

        return bestMove;
    }

    public int min(Board board, int depth) {

        if (depth == 0 || BitOperations.isCheckMate(board)) {
            return evaluator.evaluateBoard(board);
        }

        int lowestValue = Integer.MAX_VALUE;
        for (Move move : moveGenerator.GenerateLegalMoves(board)) {
            board.doMove(move);
            int enemysBestMove = max(board, (depth - 1));
            board.undoMove();
            if (enemysBestMove <= lowestValue) {
                lowestValue = enemysBestMove;
            }

        }

        return lowestValue;
    }

    public int max(Board board, int depth) {
        if (depth == 0 || BitOperations.isCheckMate(board)) {
            return evaluator.evaluateBoard(board);
        }

        int highestValue = Integer.MIN_VALUE;
        for (Move move : moveGenerator.GenerateLegalMoves(board)) {
            board.doMove(move);
            int enemysBestMove = min(board, (depth - 1));
            board.undoMove();
            if (enemysBestMove >= highestValue) {
                highestValue = enemysBestMove;
            }

        }

        return highestValue;
    }
}
