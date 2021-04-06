package datastructureproject.Evaluation;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import datastructureproject.BitOperations;
import java.util.HashMap;

/**
 *
 * @author artkoski
 */
public class SimpleEvaluator implements BoardEvaluation {

    HashMap<String, Integer> values;

    public SimpleEvaluator() {
        values = new HashMap<>();
        values.put("PAWN", 100);
        values.put("KNIGHT", 300);
        values.put("BISHOP", 300);
        values.put("ROOK", 500);
        values.put("QUEEN", 900);
        values.put("NONE", 0);
        values.put("KING", 10000);
    }

    @Override
    public int evaluateBoard(Board board) {
        //System.out.println(evaluateSide(Side.WHITE, board) + " vs " + evaluateSide(Side.BLACK, board));
        return (evaluateSide(Side.WHITE, board) - evaluateSide(Side.BLACK, board));
    }

    private int evaluateSide(Side side, Board board) {
        return evaluateScore(side, board) + isCheckMate(side, board);
    }

    private int evaluateScore(Side side, Board board) {
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
    
    
    private int isCheckMate(Side side, Board b) {
        Board opponentsPOV = b.clone();
        opponentsPOV.setSideToMove(side.flip());
        return BitOperations.isCheckMate(opponentsPOV) ? 10000 : 0;
    }

}
