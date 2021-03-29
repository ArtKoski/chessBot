package datastructureproject;

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
        long ownPieces = b.getBitboard(b.getSideToMove());

        generateKnightMoves(b, legalMoves, ~ownPieces);
        generatePawnMoves(b, legalMoves);
        generatePawnCaptures(b, legalMoves);
        generateRookMoves(b, legalMoves);
        generateBishopMoves(b, legalMoves);
        generateKingMoves(b, legalMoves, ownPieces);

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

    public void generateKingMoves(Board b, List<Move> moveList, long ownPieces) {
        long king = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.KING));
        int pieceIndex = BitOperations.bitScanForward(king);
        Square squareCurrent = Square.squareAt(pieceIndex);
        long moves = BitOperations.getKingMoves(squareCurrent, ownPieces);

        while (moves != 0L) {
            int targetIndex = BitOperations.bitScanForward(moves);
            Square squareTarget = Square.squareAt(targetIndex);
            Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
            moveList.add(newMove);

            moves = BitOperations.removeLSB(moves);
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
            long captures = BitOperations.getPawnCaptures(squareCurrent, b.getSideToMove(), enemyPieces);
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

    public void generateRookMoves(Board b, List<Move> moveList) {
        long rooks = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.ROOK));

        while (rooks != 0L) {
            int pieceIndex = BitOperations.bitScanForward(rooks);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long rookMovesPseudo = BitOperations.getRookMoves(squareCurrent, b.getBitboard());
            long moves = rookMovesPseudo & ~b.getBitboard(b.getSideToMove());

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            rooks = BitOperations.removeLSB(rooks);

        }

    }

    public void generateBishopMoves(Board b, List<Move> moveList) {
        long bishops = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.BISHOP));

        while (bishops != 0L) {
            int pieceIndex = BitOperations.bitScanForward(bishops);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long bishopMovesPseudo = BitOperations.getBishopMoves(squareCurrent, b.getBitboard());
            long moves = bishopMovesPseudo & ~b.getBitboard(b.getSideToMove());

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            bishops = BitOperations.removeLSB(bishops);

        }

    }

    public void generateQueenMoves(Board b, List<Move> moveList) {
        long queen = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.BISHOP));

        int pieceIndex = BitOperations.bitScanForward(queen);
        Square squareCurrent = Square.squareAt(pieceIndex);
        
        long bishopMovesPseudo = BitOperations.getQueenMoves(squareCurrent, b.getBitboard());
        long moves = bishopMovesPseudo & ~b.getBitboard(b.getSideToMove());

        while (moves != 0L) {
            int targetIndex = BitOperations.bitScanForward(moves);
            Square squareTarget = Square.squareAt(targetIndex);
            Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
            moveList.add(newMove);

            moves = BitOperations.removeLSB(moves);
        }
    }
}
