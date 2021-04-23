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
import datastructureproject.Evaluation.BoardEvaluation;
import datastructureproject.Evaluation.ComplexEvaluator;
import datastructureproject.Evaluation.SimpleEvaluator;
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
public class EvaluationTest {

    List<Move> moves;
    BitOperations bitboard;
    BoardEvaluation simpleEval;
    BoardEvaluation complexEval;
    Board b;

    public EvaluationTest() {
        moves = new ArrayList<>();
        bitboard = new BitOperations();
        simpleEval = new SimpleEvaluator();
        complexEval = new ComplexEvaluator();
        b = new Board();

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        b = new Board();
    }

    @After
    public void tearDown() {
    }

    //SIMPLE EVAL
    @Test
    public void simpleEvaluationStart() {
        int evalResult = simpleEval.evaluateBoard(b);
        assertEquals(0, evalResult);
    }

    @Test
    public void simpleEvaluationWhiteDoesntHaveQueen() {
        b.unsetPiece(Piece.WHITE_QUEEN, Square.D1);
        int evalResult = simpleEval.evaluateBoard(b);
        assertEquals(-900, evalResult);
    }

    //COMPLEX EVAL
    @Test
    public void centerKnightBetterThanOnEdge() {
        cornerKnightSetup();
        int cornerKnightEval = complexEval.evaluateBoard(b);

        centerKnightSetup();
        int centerKnightEval = complexEval.evaluateBoard(b);

        assertTrue(centerKnightEval > cornerKnightEval);
    }

    @Test
    public void checkBonusTest() {
        centerKnightSetup();
        int centerKnightEval = complexEval.evaluateBoard(b);

        b.unsetPiece(Piece.BLACK_KING, Square.H8);
        b.setPiece(Piece.BLACK_KING, Square.F5);
        int centerKnightWithCheckEval = complexEval.evaluateBoard(b);

        assertTrue(centerKnightWithCheckEval > centerKnightEval);
    }

    public void cornerKnightSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.A1);
        b.setPiece(Piece.WHITE_KING, Square.B2);
        b.setPiece(Piece.BLACK_KING, Square.H8);
    }

    public void centerKnightSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.D4);
        b.setPiece(Piece.WHITE_KING, Square.B2);
        b.setPiece(Piece.BLACK_KING, Square.H8);
    }

    @Test
    public void centerPawnBetterThanOnEdge() {
        b.clear();
        b.setPiece(Piece.BLACK_KING, Square.D8);
        b.setPiece(Piece.WHITE_KING, Square.E1);
        b.setPiece(Piece.WHITE_PAWN, Square.E4);
        int centerPawn = complexEval.evaluateBoard(b);

        b.clear();
        b.setPiece(Piece.BLACK_KING, Square.D8);
        b.setPiece(Piece.WHITE_KING, Square.E1);
        b.setPiece(Piece.WHITE_PAWN, Square.H4);
        int edgePawn = complexEval.evaluateBoard(b);

        assertTrue(centerPawn > edgePawn);
    }

    @Test
    public void pawnTableWorksForBothSides() {
        int beginningEval = complexEval.evaluateBoard(b);
        b.doMove(new Move(Square.D7, Square.D5));
        int blackPawnToCenterEval = complexEval.evaluateBoard(b);
        b.undoMove();
        b.doMove(new Move(Square.E2, Square.E4));
        int whitePawnToCenterEval = complexEval.evaluateBoard(b);

        assertTrue(blackPawnToCenterEval < beginningEval);
        assertTrue(whitePawnToCenterEval > beginningEval);
    }

}
