package movegeneration;

import datastructureproject.Board.*;
import datastructureproject.BoardOperations;
import datastructureproject.MovesGenerator;
import datastructureproject.lists.LinkedList;
import java.util.HashMap;
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
    LinkedList<Move> moves;
    MovesGenerator gen;
    static HashMap<Move, Boolean> legalMoves;

    public BoardOperationsTest() {
        legalMoves = new HashMap<>();
        b = new Board();
        gen = new MovesGenerator();
        moves = new LinkedList();

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

    //@Test
    public void illegalSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C3);
        b.setPiece(Piece.BLACK_KING, Square.C2);
        Move move = new Move(Square.C3, Square.C2);
        legalMoves.put(move, Boolean.FALSE);
        assertMoveIsLegal(move, b);
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

        for (Object mObj : moves) {
            Move m = (Move) mObj;
            legalMoves.put(m, Boolean.FALSE);
        }

        //LEGAL MOVES
        moves.add(new Move(Square.C3, Square.B4));    //Legal rook capture
        moves.add(new Move(Square.C3, Square.D2));    //Legal move

        for (Object mObj : moves) {
            Move m = (Move) mObj;
            legalMoves.putIfAbsent(m, Boolean.TRUE);
        }

    }

    @Test
    public void isMoveLegal() {
        CheckTestingSetup();
        setMovesList();
        moves.forEach((moveObj) -> {
            Move m = (Move) moveObj;
            assertMoveIsLegal(m, b);
        });
    }

    public static void assertMoveIsLegal(Move move, Board b) {
        try {
            assertEquals(legalMoves.get(move), BoardOperations.isMoveLegal(move, b));
        } catch (AssertionError e) {
            System.out.println(move.toString() + " - failed");
            throw e;
        }
    }

}
