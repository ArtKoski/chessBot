package datastructureproject;

import chess.engine.GameState;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author artkoski
 */
public class MovesGenerator {

    BitOperations bitboard;

    public ArrayList<Move> GenerateLegalMoves(Board b) {
        bitboard = new BitOperations();
        ArrayList<Move> legalMoves = new ArrayList<>();

        generateKnightMoves(b, legalMoves, ~b.getBitboard(b.getSideToMove()));
        generatePawnMoves(b, legalMoves);
        generatePawnCaptures(b, legalMoves);

        //Here rest of 'generate' methods
        for (Iterator<Move> iterator = legalMoves.iterator(); iterator.hasNext();) {
            Move move = iterator.next();
            if (!isMoveLegal(move, b)) {
                iterator.remove();
            }
        }

        System.out.println("Moves found: " + legalMoves);

        return legalMoves;
    }

    public boolean isMoveLegal(Move move, Board b) {
        Board tempBoard = b.clone();
        tempBoard.doMove(move);
        if (tempBoard.isKingAttacked()) {
            return false;
        }
        tempBoard.undoMove();

        return true;
    }

    public void generateKnightMoves(Board b, List<Move> moveList, long ownPieces) {
        long knights = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.KNIGHT)); //get own side knights as bitboard

        while (knights != 0L) {                                      //Go through the knights
            int pieceIndex = BitOperations.bitScanForward(knights);    //index of the least significant bit (from knights)
            Square squareCurrent = Square.squareAt(pieceIndex);        //square of the just removed piece
            long moves = BitOperations.getKnightMoves(squareCurrent, ownPieces);

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves); //Same as above, but with attacks
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            knights = BitOperations.removeLSB(knights);              //AND operation to remove least significant bit
        }
    }

    public void generatePawnMoves(Board b, List<Move> moveList) {
        long pawns = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.PAWN));

        while (pawns != 0L) {
            int pieceIndex = BitOperations.bitScanForward(pawns);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long moves = BitOperations.getPawnMoves(squareCurrent, b.getSideToMove(), b.getBitboard());

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            pawns = BitOperations.removeLSB(pawns);

        }

    }

    public void generatePawnCaptures(Board b, List<Move> moveList) {
        long enemyPieces = b.getBitboard(b.getSideToMove().flip());
        long pawns = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.PAWN));

        while (pawns != 0L) {
            int pieceIndex = BitOperations.bitScanForward(pawns);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long captures = BitOperations.getPawnCaptures(squareCurrent, enemyPieces, b.getSideToMove());
            while (captures != 0L) {
                int targetIndex = BitOperations.bitScanForward(captures);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                captures = BitOperations.removeLSB(captures);
            }

            pawns = BitOperations.removeLSB(pawns);
        }

    }
}
