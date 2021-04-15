package datastructureproject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author artkoski
 */
public class MovesGenerator {

    BitOperations bitboard;
    LinkedList<Move> moveList;
    LinkedList<Move> attacks;

    public MovesGenerator() {
        bitboard = new BitOperations();
        moveList = new LinkedList();
    }

    /**
     * Generate all legal moves for side in play. First generates pseudo legal
     * moves, then filters out illegal moves, then sorts by 'checks'.
     *
     * @param b - current board
     * @return list of legal moves
     */
    public LinkedList<Move> generateLegalMoves(Board b, boolean sort) {
        moveList = new LinkedList();
        attacks = new LinkedList();
        long ownPieces = b.getBitboard(b.getSideToMove());

        generateKnightMoves(b, moveList, ownPieces);
        generateKingMoves(b, moveList, ownPieces);
        generatePawnMoves(b, moveList);
        generatePawnCaptures(b, moveList);
        generateRookMoves(b, moveList);
        generateBishopMoves(b, moveList);
        generateQueenMoves(b, moveList);
        //generateCastleMoves(b, moves);

        filterMoveList(b, sort);

        return moveList;
    }

    /**
     * Remove illegal moves and rearrange the moves: first checks, then
     * captures, then the rest.
     *
     * @param b - current board
     * @param sort - boolean for whether you want to sort the movelist or not
     */
    public void filterMoveList(Board b, boolean sort) {
        for (Iterator<Move> iterator = moveList.iterator(); iterator.hasNext();) {
            Move move = iterator.next();
            if (!BoardOperations.isMoveLegal(move, b)) {
                iterator.remove();
                continue;
            }
            if (BoardOperations.isEnemyKingCheckedAfterMove(move, b) && sort) {
                attacks.addFirst(move);
                iterator.remove();
            } else if (BoardOperations.isMoveCapture(move, b) && sort) {
                attacks.addLast(move);
            }

        }
        attacks.addAll(moveList);
        moveList = attacks;
    }

    /**
     * Iterates through all (own) knights. For every knight, finds the available
     * knight moves and adds them to the list. Since bitboards are used for
     * everything, iterating is done with bitScanForward and removeLSB, which
     * both have to do with Least Significant Bits. All the following generating
     * methods use the same logic.
     *
     * @param b - current board
     * @param moveList - list of moves
     * @param ownPieces - bitboard of own pieces for filtering
     */
    public void generateKnightMoves(Board b, List<Move> moveList, long ownPieces) {
        long knights = b.getBitboard(Piece.make(b.getSideToMove(), PieceType.KNIGHT));

        while (knights != 0L) {
            int pieceIndex = BitOperations.bitScanForward(knights);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long moves = BitOperations.getKnightMoves(squareCurrent, ownPieces);

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                Move newMove = new Move(squareCurrent, squareTarget, Piece.NONE);
                moveList.add(newMove);

                moves = BitOperations.removeLSB(moves);
            }
            knights = BitOperations.removeLSB(knights);
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
