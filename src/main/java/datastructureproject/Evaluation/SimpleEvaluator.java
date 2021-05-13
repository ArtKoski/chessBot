package datastructureproject.Evaluation;

import datastructureproject.Board.*;
import datastructureproject.BitOperations;
import java.util.HashMap;

/**
 * First iteration of the evaluator. Kept here just as a reference.
 *
 * @author artkoski
 */
public class SimpleEvaluator implements BoardEvaluation {

    static final HashMap<String, Integer> values;

    static {
        values = new HashMap<>();
        values.put("PAWN", 100);
        values.put("KNIGHT", 300);
        values.put("BISHOP", 300);
        values.put("ROOK", 500);
        values.put("QUEEN", 900);
        values.put("NONE", 0);
        values.put("KING", 10000);
    }

    /**
     * Used to evaluate the current board.
     *
     * @param board - current board
     * @return evaluation
     */
    @Override
    public int evaluateBoard(Board board) {
        int score = evaluateSide(Side.WHITE, board) - evaluateSide(Side.BLACK, board);
        return score;
    }

    /**
     * Evaluate given side
     *
     * @param side - Side.WHITE or Side.BLACK
     * @param board - current board
     * @return evaluation
     */
    public int evaluateSide(Side side, Board board) {
        return evaluateScore(side, board);
    }

    /**
     * Evaluation based on how many and which pieces are on the board for the
     * given side.
     *
     * @param side - Side.WHITE or Side.BLACK
     * @param board - curent board
     * @return sum of piece values
     */
    public int evaluateScore(Side side, Board board) {
        long pieces = board.getBitboard(side);
        int score = 0;

        while (pieces != 0) {
            int pieceIndex = BitOperations.bitScanForward(pieces);
            Piece currentPiece = board.getPiece(Square.squareAt(pieceIndex));
            score += values.get(String.valueOf(currentPiece.getPieceType()));

            pieces = BitOperations.removeLSB(pieces);
        }
        return score;

    }

}
