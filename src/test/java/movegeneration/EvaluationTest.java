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
import datastructureproject.BitOperations;
import datastructureproject.Evaluation.BoardEvaluation;
import datastructureproject.Evaluation.ComplexEvaluator;
import datastructureproject.Evaluation.ComplexEvaluator;
import datastructureproject.Evaluation.SimpleEvaluator;
import datastructureproject.Evaluation.Zobrist;
import java.util.ArrayList;
import java.util.HashMap;
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
    ComplexEvaluator complexEvalTEST;
    Board b;

    public EvaluationTest() {
        moves = new ArrayList<>();
        bitboard = new BitOperations();
        simpleEval = new SimpleEvaluator();
        complexEval = new ComplexEvaluator();
        complexEvalTEST = new ComplexEvaluator();
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
    public void pawnTableCenterPawnValues() {
        //Center pawns
        assertTrue(complexEvalTEST.getPawnBonus(Side.WHITE, Square.E4.ordinal()) == 25);
        assertTrue(complexEvalTEST.getPawnBonus(Side.WHITE, Square.D4.ordinal()) == 25);
        assertTrue(complexEvalTEST.getPawnBonus(Side.BLACK, Square.E5.ordinal()) == 25);
        assertTrue(complexEvalTEST.getPawnBonus(Side.BLACK, Square.D5.ordinal()) == 25);
    }

    @Test
    public void ZobristXORworksForHash() {
        long start = Zobrist.getKeyForBoard(b);
        long moveForward = Zobrist.getKeyForMove(new Move(Square.E2, Square.E4), b);

        long newPosition = start ^ moveForward;
        assertTrue(newPosition != start);

        newPosition ^= moveForward;
        assertTrue(newPosition == start);
    }

    @Test
    public void ZobristTranspositionTable() {
        int startScore = 0;
        int scoreAfterE2E4 = 5;
        long startHash = Zobrist.getKeyForBoard(b);
        Zobrist.updateHash(startHash, startScore);

        b.doMove(new Move(Square.E2, Square.E4));
        long hashE2E4 = Zobrist.getKeyForBoard(b);
        Zobrist.updateHash(hashE2E4, scoreAfterE2E4);

        assertTrue(Zobrist.getScoreFromHash(startHash) == startScore);
        assertTrue(Zobrist.getScoreFromHash(hashE2E4) == scoreAfterE2E4);
    }

    //@Test
    public void asd() {
        int evaluate = 10;

    }

}
