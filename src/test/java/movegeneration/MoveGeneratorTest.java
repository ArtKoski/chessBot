/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movegeneration;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
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

    //KNIGHT
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
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.D4);
        b.setPiece(Piece.WHITE_PAWN, Square.C2);
        b.setPiece(Piece.WHITE_PAWN, Square.C6);
        b.setPiece(Piece.WHITE_PAWN, Square.E2);
        b.setPiece(Piece.WHITE_PAWN, Square.E6);
        b.setPiece(Piece.WHITE_PAWN, Square.B3);
        b.setPiece(Piece.WHITE_PAWN, Square.B5);
        b.setPiece(Piece.WHITE_PAWN, Square.F3);
        b.setPiece(Piece.WHITE_PAWN, Square.F5);
        knightMoves();
        assertEquals(0, moves.size());
    }

    @Test
    public void knightSurroundedByEnemies() {
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.D4);
        b.setPiece(Piece.BLACK_PAWN, Square.C2);
        b.setPiece(Piece.BLACK_PAWN, Square.C6);
        b.setPiece(Piece.BLACK_PAWN, Square.E2);
        b.setPiece(Piece.BLACK_PAWN, Square.E6);
        b.setPiece(Piece.BLACK_PAWN, Square.B3);
        b.setPiece(Piece.BLACK_PAWN, Square.B5);
        b.setPiece(Piece.BLACK_PAWN, Square.F3);
        b.setPiece(Piece.BLACK_PAWN, Square.F5);
        knightMoves();
        assertEquals(8, moves.size());
    }

    public void knightMoves() {
        gen.generateKnightMoves(b, moves, b.getBitboard(b.getSideToMove()));
    }

    //PAWN
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

    @Test
    public void whitePawnCanPromoteByMoving() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.A7);
        pawnMoves();

        assertEquals(4, moves.size());
        assertThatPromotionMovesAvailable("WHITE");
    }

    @Test
    public void whitePawnCanPromoteByCapturing() {
        b.clear();
        b.setPiece(Piece.WHITE_PAWN, Square.A7);
        b.setPiece(Piece.BLACK_QUEEN, Square.B8);
        pawnCaptures();

        assertThatPromotionMovesAvailable("WHITE");
    }

    @Test
    public void blackPawnCanPromoteByMoving() {
        b.clear();
        b.setSideToMove(Side.BLACK);
        b.setPiece(Piece.BLACK_PAWN, Square.A2);
        pawnMoves();

        assertThatPromotionMovesAvailable("BLACK");
    }

    @Test
    public void blackPawnCanPromoteByCapturing() {
        b.clear();
        b.setSideToMove(Side.BLACK);

        b.setPiece(Piece.BLACK_PAWN, Square.A2);
        b.setPiece(Piece.WHITE_QUEEN, Square.B1);
        pawnCaptures();

        assertThatPromotionMovesAvailable("BLACK");
    }

    public void assertThatPromotionMovesAvailable(String side) {
        for (Move move : moves) {
            assertTrue(move.getPromotion().equals(Piece.make(Side.fromValue(side), PieceType.fromValue("KNIGHT")))
                    | move.getPromotion().equals(Piece.make(Side.fromValue(side), PieceType.fromValue("ROOK")))
                    | move.getPromotion().equals(Piece.make(Side.fromValue(side), PieceType.fromValue("BISHOP")))
                    | move.getPromotion().equals(Piece.make(Side.fromValue(side), PieceType.fromValue("QUEEN"))));
        }
    }

    //ROOK
    @Test
    public void rookMovesEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_ROOK, Square.A1);
        gen.generateRookMoves(b, moves);

        assertEquals(14, moves.size());
    }

    @Test
    public void rookMovesBlockedByFriends() {
        b.clear();
        b.setPiece(Piece.WHITE_ROOK, Square.A1);
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.WHITE_KNIGHT, Square.B1);
        gen.generateRookMoves(b, moves);

        assertEquals(0, moves.size());
    }

    @Test
    public void rookMovesWhenBlockedBySingleEnemy() {
        b.clear();
        b.setPiece(Piece.WHITE_ROOK, Square.A1);
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.BLACK_KNIGHT, Square.B1);
        gen.generateRookMoves(b, moves);

        assertEquals(1, moves.size());
    }

    @Test
    public void rookMovesEnemyTwoBlocksAway() {
        b.clear();
        b.setPiece(Piece.WHITE_ROOK, Square.A1);
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.BLACK_KNIGHT, Square.C1);
        gen.generateRookMoves(b, moves);

        assertEquals(2, moves.size());
    }

    @Test
    public void rookMovesSurroundedByEnemies() {
        b.clear();
        b.setPiece(Piece.WHITE_ROOK, Square.C3);
        b.setPiece(Piece.BLACK_PAWN, Square.C2);
        b.setPiece(Piece.BLACK_KNIGHT, Square.C4);
        b.setPiece(Piece.BLACK_PAWN, Square.B3);
        b.setPiece(Piece.BLACK_KNIGHT, Square.D3);
        gen.generateRookMoves(b, moves);

        assertEquals(4, moves.size());
    }

    //BISHOP
    @Test
    public void bishopC3MovesEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_BISHOP, Square.C3);
        gen.generateBishopMoves(b, moves);

        assertEquals(11, moves.size());
    }

    @Test
    public void bishopB2MovesEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_BISHOP, Square.B2);
        gen.generateBishopMoves(b, moves);

        assertEquals(9, moves.size());
    }

    @Test
    public void bishopBlockedByEnemiesClose() {
        b.clear();
        b.setPiece(Piece.WHITE_BISHOP, Square.B2);
        b.setPiece(Piece.BLACK_BISHOP, Square.A1);
        b.setPiece(Piece.BLACK_BISHOP, Square.A3);
        b.setPiece(Piece.BLACK_KNIGHT, Square.C1);
        b.setPiece(Piece.BLACK_KNIGHT, Square.C3);
        gen.generateBishopMoves(b, moves);

        assertEquals(4, moves.size());
    }

    @Test
    public void bishopBlockedByEnemiesFurther() {
        b.clear();
        b.setPiece(Piece.WHITE_BISHOP, Square.C3);
        b.setPiece(Piece.BLACK_BISHOP, Square.A1);
        b.setPiece(Piece.BLACK_BISHOP, Square.E1);
        b.setPiece(Piece.BLACK_KNIGHT, Square.G7);
        b.setPiece(Piece.BLACK_KNIGHT, Square.B4);
        gen.generateBishopMoves(b, moves);

        assertEquals(9, moves.size());

    }

    @Test
    public void bishopBlockedByFriends() {
        b.clear();
        b.setPiece(Piece.WHITE_BISHOP, Square.B2);
        b.setPiece(Piece.WHITE_KNIGHT, Square.A1);
        b.setPiece(Piece.WHITE_KNIGHT, Square.A3);
        b.setPiece(Piece.WHITE_KING, Square.C1);
        b.setPiece(Piece.WHITE_PAWN, Square.C3);
        gen.generateBishopMoves(b, moves);

        assertEquals(0, moves.size());
    }

    //KING
    @Test
    public void kingMovesAtStart() {
        gen.generateKingMoves(b, moves, b.getBitboard(b.getSideToMove()));
        assertEquals(0, moves.size());
    }

    @Test
    public void kingMovesEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C4);

        gen.generateKingMoves(b, moves, b.getBitboard(b.getSideToMove()));
        assertEquals(8, moves.size());
    }

    @Test
    public void kingCanEatEnemies() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C4);
        b.setPiece(Piece.BLACK_PAWN, Square.C3);

        gen.generateKingMoves(b, moves, b.getBitboard(b.getSideToMove()));
        assertEquals(8, moves.size());
    }

    @Test
    public void kingCantBeCaptured() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.D2);
        b.setPiece(Piece.BLACK_PAWN, Square.C3);
        b.setSideToMove(Side.BLACK);

        gen.generatePawnMoves(b, moves);
        assertEquals(1, moves.size());
        assertFalse(moves.get(0).toString().equals("c3d2"));

    }

    //QUEEN - essentially just combines rook and bishop, no need for indepth testing
    @Test
    public void queenMovesStart() {
        gen.generateQueenMoves(b, moves);
        assertEquals(0, moves.size());
    }

    @Test
    public void queenMovesEmptyBoard() {
        b.clear();
        b.setPiece(Piece.WHITE_QUEEN, Square.E5);
        gen.generateQueenMoves(b, moves);
        assertEquals(27, moves.size());
    }

    //RANDOM
    //@Test
    public void moveFilterWorksProperly() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.B5);
        b.setPiece(Piece.WHITE_QUEEN, Square.A1);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.G5);

        moves = gen.generateLegalMoves(b, true);
        //System.out.println(moves);
    }

    //@Test
    public void generatingMovesIsNotTooSlow() throws MoveGeneratorException {
        b.loadFromFen("r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq");
        long startTime = System.currentTimeMillis();
        gen.generateLegalMoves(b, false);
        long myTime = (System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        MoveGenerator.generateLegalMoves(b);
        long chessLibTime = (System.currentTimeMillis() - startTime);

        System.out.println(myTime + " vs " + chessLibTime);

        System.out.println("chessLib was " + ((1.0 * myTime) / (1.0 * chessLibTime)) + " times faster");

    }
}
