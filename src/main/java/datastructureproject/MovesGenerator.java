package datastructureproject;

import datastructureproject.Board.*;
import java.util.Iterator;
import datastructureproject.lists.LinkedList;

/**
 *
 * @author artkoski
 */
public class MovesGenerator {

    BitOperations bitboard;
    LinkedList<Move> moveList;
    LinkedList attacks;

    public MovesGenerator() {
        bitboard = new BitOperations();
        moveList = new LinkedList();
    }

    /**
     * Generate all legal moves for side in play. First generates pseudo legal
     * moves, then filters out illegal moves, then sorts by 'checks'.
     *
     * @param b - current board
     * @param sort - boolean passed on to filterMoveList
     *
     * @return list of legal moves
     */
    public LinkedList generateLegalMoves(Board b, boolean sort) {
        moveList = new LinkedList();
        attacks = new LinkedList();
        long ownPieces = b.getBitboard(b.getSideToMove());

        generatePawnMoves(b, moveList);
        generateKnightMoves(b, moveList, ownPieces);
        generateQueenMoves(b, moveList);
        generateBishopMoves(b, moveList);
        generateRookMoves(b, moveList);
        generatePawnCaptures(b, moveList);
        generateKingMoves(b, moveList, ownPieces);

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
        Move move;

        for (Iterator<Move> iterator = moveList.iterator(); iterator.hasNext();) {
            move = iterator.next();
            if (!BoardOperations.isMoveLegal(move, b)) {
                iterator.remove();
                continue;
            }
            if (BoardOperations.isEnemyKingCheckedAfterMove(move, b) && sort) {
                attacks.addFirst(move);
                iterator.remove();
            } else if (BoardOperations.isMoveCapture(move, b) && sort) {
                attacks.addLast(move);
                iterator.remove();
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
    public void generateKnightMoves(Board b, LinkedList moveList, long ownPieces) {
        long knights = b.getBitboard(Piece.makePiece(PieceType.KNIGHT, b.getSideToMove()));

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

    public void generateKingMoves(Board b, LinkedList moveList, long ownPieces) {
        long king = b.getBitboard(Piece.makePiece(PieceType.KING, b.getSideToMove()));

        if (king != 0L) {
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
    }

    public void generatePawnMoves(Board b, LinkedList moveList) {
        long pawns = b.getBitboard(Piece.makePiece(PieceType.PAWN, b.getSideToMove()));

        while (pawns != 0L) {
            int pieceIndex = BitOperations.bitScanForward(pawns);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long moves = BitOperations.getPawnMoves(squareCurrent, b.getSideToMove(), b.getBitBoard());

            while (moves != 0L) {
                int targetIndex = BitOperations.bitScanForward(moves);
                Square squareTarget = Square.squareAt(targetIndex);
                addPawnMoves(squareTarget, squareCurrent, b.getSideToMove(), moveList);
                moves = BitOperations.removeLSB(moves);
            }
            pawns = BitOperations.removeLSB(pawns);

        }

    }

    public void generatePawnCaptures(Board b, LinkedList moveList) {
        long enemyPieces = b.getBitboard(b.getSideToMove().flip());
        long pawns = b.getBitboard(Piece.makePiece(PieceType.PAWN, b.getSideToMove()));

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

    public void addPawnMoves(Square squareTarget, Square squareCurrent, Side side, LinkedList moves) {
        long targetBB = squareTarget.getSquareBB();
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

    public void generateRookMoves(Board b, LinkedList moveList) {
        long rooks = b.getBitboard(Piece.makePiece(PieceType.ROOK, b.getSideToMove()));

        while (rooks != 0L) {
            int pieceIndex = BitOperations.bitScanForward(rooks);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long moves = BitOperations.getRookMoves(squareCurrent, b.getBitBoard(), ~b.getBitboard(b.getSideToMove()));

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

    public void generateBishopMoves(Board b, LinkedList moveList) {
        long bishops = b.getBitboard(Piece.makePiece(PieceType.BISHOP, b.getSideToMove()));

        while (bishops != 0L) {
            int pieceIndex = BitOperations.bitScanForward(bishops);
            Square squareCurrent = Square.squareAt(pieceIndex);
            long moves = BitOperations.getBishopMoves(squareCurrent, b.getBitBoard(), ~b.getBitboard(b.getSideToMove()));

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

    public void generateQueenMoves(Board b, LinkedList moveList) {
        long queens = b.getBitboard(Piece.makePiece(PieceType.QUEEN, b.getSideToMove()));

        while (queens != 0L) {
            int pieceIndex = BitOperations.bitScanForward(queens);
            Square squareCurrent = Square.squareAt(pieceIndex);

            long moves = BitOperations.getQueenMoves(squareCurrent, b.getBitBoard(), ~b.getBitboard(b.getSideToMove()));

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
