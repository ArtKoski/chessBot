package datastructureproject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
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
        ArrayList<Move> moves = new ArrayList<>();
        long ownPieces = b.getBitboard(b.getSideToMove());

        generateKnightMoves(b, moves, ownPieces);
        generatePawnMoves(b, moves);
        generatePawnCaptures(b, moves);
        generateRookMoves(b, moves);
        generateBishopMoves(b, moves);
        generateQueenMoves(b, moves);
        generateKingMoves(b, moves, ownPieces);
        //generateCastleMoves(b, moves);

        for (Iterator<Move> iterator = moves.iterator(); iterator.hasNext();) {
            Move move = iterator.next();
            if (!BitOperations.isMoveLegal(move, b)) {
                iterator.remove();
            }
        }

        //System.out.println(b.getSideToMove() + " Moves found: " + moves);

        return moves;
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

    //COPY PASTA FROM CHESSLIB FOR TIME BEING == NOT OWN IMPLEMENTATION!!!
    public void generateCastleMoves(Board board, List<Move> moves) {
        Side side = board.getSideToMove();
        if (board.isKingAttacked()) {
            return;
        }
        if (board.getCastleRight(side).equals(CastleRight.KING_AND_QUEEN_SIDE)
                || (board.getCastleRight(side).equals(CastleRight.KING_SIDE))) {
            if ((board.getBitboard() & board.getContext().getooAllSquaresBb(side)) == 0L) {
                if (!board.isSquareAttackedBy(board.getContext().getooSquares(side), side.flip())) {
                    moves.add(board.getContext().getoo(side));
                }
            }
        }
        if (board.getCastleRight(side).equals(CastleRight.KING_AND_QUEEN_SIDE)
                || (board.getCastleRight(side).equals(CastleRight.QUEEN_SIDE))) {
            if ((board.getBitboard() & board.getContext().getoooAllSquaresBb(side)) == 0L) {
                if (!board.isSquareAttackedBy(board.getContext().getoooSquares(side), side.flip())) {
                    moves.add(board.getContext().getooo(side));
                }
            }
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
                addPawnMoves(squareTarget, squareCurrent, b.getSideToMove(), moveList);
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
                addPawnMoves(squareTarget, squareCurrent, b.getSideToMove(), moveList);
                captures = BitOperations.removeLSB(captures);
            }

            pawns = BitOperations.removeLSB(pawns);
        }

    }

    public void addPawnMoves(Square squareTarget, Square squareCurrent, Side side, List<Move> moves) {
        long targetBB = squareTarget.getBitboard();
        if (side.equals(Side.WHITE) && (targetBB & BitOperations.rank[7]) != 0L) {
            moves.add(new Move(squareCurrent, squareTarget, Piece.WHITE_KNIGHT));
            moves.add(new Move(squareCurrent, squareTarget, Piece.WHITE_ROOK));
            moves.add(new Move(squareCurrent, squareTarget, Piece.WHITE_BISHOP));
            moves.add(new Move(squareCurrent, squareTarget, Piece.WHITE_QUEEN));
        } else if (side.equals(Side.BLACK) && (targetBB & BitOperations.rank[0]) != 0L) {
            moves.add(new Move(squareCurrent, squareTarget, Piece.BLACK_KNIGHT));
            moves.add(new Move(squareCurrent, squareTarget, Piece.BLACK_ROOK));
            moves.add(new Move(squareCurrent, squareTarget, Piece.BLACK_BISHOP));
            moves.add(new Move(squareCurrent, squareTarget, Piece.BLACK_QUEEN));
        } else {
            moves.add(new Move(squareCurrent, squareTarget, Piece.NONE));

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
        long queens = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.QUEEN));

        while (queens != 0L) {
            int pieceIndex = BitOperations.bitScanForward(queens);
            Square squareCurrent = Square.squareAt(pieceIndex);

            long queenMovesPseudo = BitOperations.getQueenMoves(squareCurrent, b.getBitboard());
            long moves = queenMovesPseudo & ~b.getBitboard(b.getSideToMove());

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            queens = BitOperations.removeLSB(queens);
        }
    }
}
