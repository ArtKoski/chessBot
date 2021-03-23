/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movegeneration;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.BitOperations;
import datastructureproject.MovesGenerator;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author artkoski
 */
public class MoveGeneratorTest {

    MovesGenerator gen;
    Board b;
    List<Move> moves;
    BitOperations bitboard;

    public MoveGeneratorTest() {
        b = new Board();
        gen = new MovesGenerator();
        moves = new ArrayList<>();
        bitboard = new BitOperations();

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        moves.clear();
        b = new Board();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void KnightMovesAtStart() {
        knightMoves();
        assertEquals(4, moves.size());
    }

    @Test
    public void KnightMovesInMiddleOnEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.D4);
        knightMoves();
        assertEquals(8, moves.size());
    }

    @Test
    public void knightDoesntDoFriendlyFire() {
        b.setPiece(Piece.WHITE_PAWN, Square.A3);
        b.setPiece(Piece.WHITE_PAWN, Square.C3);
        b.setPiece(Piece.WHITE_PAWN, Square.F3);
        b.setPiece(Piece.WHITE_PAWN, Square.H3);
        knightMoves();
        assertEquals(0, moves.size());
    }

    public void knightMoves() {
        gen.generateKnightMoves(b, moves, ~b.getBitboard(b.getSideToMove()));
    }

    @Test
    public void pawnMovesFromStart() {
        pawnMoves();
        assertEquals(16, moves.size());
    }

    @Test
    public void pawnCantMoveThroughFriend() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.WHITE_KING, Square.A3);

        pawnMoves();
        assertEquals(0, moves.size());
    }

    @Test
    public void Rank2PawnCantAdvanceTwoSquaresIfSecondIsObstructed() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.BLACK_ROOK, Square.A4);
        pawnMoves();
        assertEquals(1, moves.size());
    }

    @Test
    public void pawnCanCaptureOnCloseDiagonals() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.B2);
        b.setPiece(Piece.BLACK_BISHOP, Square.A3);
        b.setPiece(Piece.BLACK_BISHOP, Square.C3);
        pawnCaptures();

        assertEquals(2, moves.size());
    }

    @Test
    public void pawnCantCaptureOwnPieces() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.B2);
        b.setPiece(Piece.WHITE_BISHOP, Square.A3);
        b.setPiece(Piece.WHITE_ROOK, Square.C3);
        pawnCaptures();

        assertEquals(0, moves.size());
    }

    @Test
    public void pawnCantCaptureNothing() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.B2);
        pawnCaptures();

        assertEquals(0, moves.size());
    }

    public void pawnMoves() {
        gen.generatePawnMoves(b, moves);
    }

    public void pawnCaptures() {
        gen.generatePawnCaptures(b, moves);
    }

}
