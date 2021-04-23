package datastructureproject.Evaluation;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import datastructureproject.BitOperations;
import datastructureproject.BoardOperations;
import datastructureproject.MovesGenerator;

/**
 * Extends SimpleEvaluator. Added evaluation metrics are mobility, checks, pawn
 * and knight board locations, . Not actually complex.
 *
 * @author artkoski
 */
public class ComplexEvaluator extends SimpleEvaluator implements BoardEvaluation {

    MovesGenerator moveGen = new MovesGenerator();
    int CHECK_BONUS = 50;

    Integer[] PawnTable = {0, 0, 0, 0, 0, 0, 0, 0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5, 5, 10, 25, 25, 10, 5, 5,
        0, 0, 0, 20, 20, 0, 0, 0,
        5, -5, -10, 0, 0, -10, -5, 5,
        5, 10, 10, -20, -20, 10, 10, 5,
        0, 0, 0, 0, 0, 0, 0, 0};

    Integer[] KnightTable = {-50, -40, -30, -30, -30, -30, -40, -50,
        -40, -20, 0, 0, 0, 0, -20, -40,
        -30, 0, 10, 15, 15, 10, 0, -30,
        -30, 5, 15, 20, 20, 15, 5, -30,
        -30, 0, 15, 20, 20, 15, 0, -30,
        -30, 5, 10, 15, 15, 10, 5, -30,
        -40, -20, 0, 5, 5, 0, -20, -40,
        -50, -40, -30, -30, -30, -30, -40, -50};

    Integer[] QueenTable = {-20, -10, -10, -5, -5, -10, -10, -20,
        -10, 0, 0, 0, 0, 0, 0, -10,
        -10, 0, 5, 5, 5, 5, 0, -10,
        -5, 0, 5, 5, 5, 5, 0, -5,
        0, 0, 5, 5, 5, 5, 0, -5,
        -10, 5, 5, 5, 5, 5, 0, -10,
        -10, 0, 5, 0, 0, 0, 0, -10,
        -20, -10, -10, -5, -5, -10, -10, -20};

    public int getPawnBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? PawnTable[63 - square] : PawnTable[square];
    }

    public int getKnightBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? KnightTable[63 - square] : KnightTable[square];
    }

    public int getQueenBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? QueenTable[63 - square] : QueenTable[square];
    }

    @Override
    public int evaluateSide(Side side, Board board) {
        return evaluateScore(side, board) + (5 * mobilityBonus(side, board)) + checkBonus(side, board);
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

    @Override
    public int evaluateScore(Side side, Board board) {
        long pieces = board.getBitboard(side);
        int score = 0;

        while (pieces != 0) {
            int pieceIndex = BitOperations.bitScanForward(pieces);
            Piece currentPiece = board.getPiece(Square.squareAt(pieceIndex));
            score += values.get(String.valueOf(currentPiece.getPieceType()));

            if (String.valueOf(currentPiece.getPieceType()).equals("PAWN")) {
                score += getPawnBonus(side, pieceIndex);
            }
            if (String.valueOf(currentPiece.getPieceType()).equals("KNIGHT")) {
                score += getKnightBonus(side, pieceIndex);
            }
            if (String.valueOf(currentPiece.getPieceType()).equals("QUEEN")) {
                score += getQueenBonus(side, pieceIndex);
            }

            pieces = BitOperations.removeLSB(pieces);
        }
        return score;

    }

}
