package datastructureproject.Evaluation;

import datastructureproject.BitOperations;
import datastructureproject.BoardOperations;
import datastructureproject.MovesGenerator;
import datastructureproject.Board.*;

/**
 * Extends SimpleEvaluator. Added evaluation metrics are mobility, checks, piece
 * locations on board.
 *
 * @author artkoski
 */
public class ComplexEvaluator extends SimpleEvaluator implements BoardEvaluation {

    MovesGenerator moveGen = new MovesGenerator();
    int CHECK_BONUS = 50;

    /**
     * All the tables are from
     * https://www.chessprogramming.org/Simplified_Evaluation_Function
     *
     */
    Integer[] PawnTable = {0, 0, 0, 0, 0, 0, 0, 0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5, 5, 10, 27, 27, 10, 5, 5,
        0, 0, 0, 25, 25, 0, 0, 0,
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

    Integer[] BishopTable = {-20, -10, -10, -10, -10, -10, -10, -20,
        -10, 0, 0, 0, 0, 0, 0, -10,
        -10, 0, 5, 10, 10, 5, 0, -10,
        -10, 5, 5, 10, 10, 5, 5, -10,
        -10, 0, 10, 10, 10, 10, 0, -10,
        -10, 10, 10, 10, 10, 10, 10, -10,
        -10, 5, 0, 0, 0, 0, 5, -10,
        -20, -10, -10, -10, -10, -10, -10, -20};
    Integer[] RookTable = {
        0, 0, 0, 0, 0, 0, 0, 0,
        5, 10, 10, 10, 10, 10, 10, 5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        -5, 0, 0, 0, 0, 0, 0, -5,
        0, 0, 0, 5, 5, 0, 0, 0
    };

    public int getPawnBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? PawnTable[63 - square] : PawnTable[square];
    }

    public int getKnightBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? KnightTable[63 - square] : KnightTable[square];
    }

    public int getQueenBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? QueenTable[63 - square] : QueenTable[square];
    }

    public int getBishopBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? BishopTable[63 - square] : QueenTable[square];
    }

    public int getRookBonus(Side side, int square) {
        return side.equals(Side.WHITE) ? RookTable[63 - square] : QueenTable[square];
    }

    @Override
    public int evaluateSide(Side side, Board board) {
        return evaluateScore(side, board) + (int) (0.1 * mobilityBonus(side, board)) + checkBonus(side, board);
    }

    /**
     * Mobility bonus is based on how many moves the player has.
     *
     * @param side - current side
     * @param board - current board
     * @return
     */
    private double mobilityBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side);
        return 1.0 * moveGen.generateLegalMoves(tempBoard, false).size();
    }

    private int checkBonus(Side side, Board board) {
        Board tempBoard = board.clone();
        tempBoard.setSideToMove(side);
        return BoardOperations.isKingAttacked(tempBoard) ? CHECK_BONUS : 0;
    }

    /**
     * Otherwise same as SimpleEval's, but also has feature flag for Zobrist
     * Hashing. (Zobrist not in use :( )
     *
     * @param board - current board
     * @param hash - current hash
     * @param zobristFlag - use Zobrist Hashing or no
     * @param depth - current depth, for Zobrist Hashing
     * @return
     */
    public int evaluateBoard(Board board, long hash, boolean zobristFlag, int depth) {
        //  if (!zobristFlag) {
        return evaluateSide(Side.WHITE, board) - evaluateSide(Side.BLACK, board);
        //}
        /*
        ZobristUnit unit = Zobrist.getUnitFromHash(hash);

        if (Zobrist.unitExists(hash) && unit.getDepth() < depth) {
            i++;
            return unit.getScore();
        }
        Integer score = evaluateSide(Side.WHITE, board) - evaluateSide(Side.BLACK, board);
        freshPositions++;
        Zobrist.updateHash(hash, score, depth);
        return score;
         */
    }

    @Override
    public int evaluateScore(Side side, Board board) {

        long pieces = board.getBitboard(side);
        int score = 0;

        while (pieces != 0L) {
            int pieceIndex = BitOperations.bitScanForward(pieces);
            Piece currentPiece = board.getPiece(Square.squareAt(pieceIndex));
            score += values.get(String.valueOf(currentPiece.getPieceType()));
            switch (String.valueOf(currentPiece.getPieceType())) {
                case "PAWN":
                    score += getPawnBonus(side, pieceIndex);
                    break;
                case "KNIGHT":
                    score += getKnightBonus(side, pieceIndex);
                    break;
                case "QUEEN":
                    score += getQueenBonus(side, pieceIndex);
                    break;
                case "BISHOP":
                    score += getBishopBonus(side, pieceIndex);
                    break;
                case "ROOK":
                    score += getRookBonus(side, pieceIndex);
                    break;
                default:
                    break;
            }

            pieces = BitOperations.removeLSB(pieces);
        }
        return score;

    }

}
