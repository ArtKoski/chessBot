/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movegeneration;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.BoardOperations;
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
public class BoardOperationsTest {

    Board b;
    List<Move> moves;
    MovesGenerator gen;

    public BoardOperationsTest() {
        b = new Board();
        gen = new MovesGenerator();
        moves = new ArrayList<>();

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

    public void CheckTestingSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C3);
        b.setPiece(Piece.WHITE_PAWN, Square.E3);
        b.setPiece(Piece.BLACK_ROOK, Square.B4);
        b.setPiece(Piece.BLACK_KING, Square.H8);
        b.setPiece(Piece.BLACK_PAWN, Square.D5);
        b.setPiece(Piece.BLACK_KNIGHT, Square.D4);
        b.setPiece(Piece.BLACK_QUEEN, Square.F3);
        b.setSideToMove(Side.WHITE);
    }

    /**
     * This is supposed to be used with specific setup of CheckTestingSetup.
     */
    public void setMovesList() {
        //ILLEGAL MOVES
        moves.add(new Move(Square.C3, Square.B3));    //Rook+Knight check
        moves.add(new Move(Square.C3, Square.B2));    //Rook check
        moves.add(new Move(Square.C3, Square.C4));    //Rook+Pawn check
        moves.add(new Move(Square.C3, Square.C2));    //Knight check
        moves.add(new Move(Square.C3, Square.D4));    //Rook check
        moves.add(new Move(Square.E3, Square.E4));    //Queen discover check

        //LEGAL MOVES
        moves.add(new Move(Square.C3, Square.B4));    //Legal rook capture
        moves.add(new Move(Square.C3, Square.D2));    //Legal move
    }

    @Test
    public void isMoveLegal() {
        CheckTestingSetup();
        setMovesList();
        moves.forEach((move) -> {
            assertMoveIsLegal(move, b);
        });
    }

    public static void assertMoveIsLegal(Move move, Board b) {
        try {
            assertEquals(b.isMoveLegal(move, true), BoardOperations.isMoveLegal(move, b));
        } catch (AssertionError e) {
            System.out.println(move.toString() + " - failed");
            throw e;
        }
    }

    //@Test
    public void isEnemyKingChecked() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C3);
        b.setPiece(Piece.WHITE_PAWN, Square.E3);
        b.setPiece(Piece.BLACK_ROOK, Square.B4);
        b.setPiece(Piece.BLACK_KING, Square.H8);
        b.setPiece(Piece.BLACK_PAWN, Square.D5);
        b.setPiece(Piece.BLACK_KNIGHT, Square.D4);
        b.setPiece(Piece.BLACK_QUEEN, Square.F3);
        b.setSideToMove(Side.BLACK);
        List<Move> asd = gen.generateLegalMoves(b);
        System.out.println();
    }
}
