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
import datastructureproject.Evaluation.SimpleEvaluator;
import datastructureproject.*;
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

    MovesGenerator gen;
    Board b;
    List<Move> moves;
    BitOperations bitboard;
    BoardEvaluation eval;
    MiniMax miniMax;
    MiniMaxAB miniMaxAB;

    /*
    First String is FEN notation as an ID, second String is the best (expected) play.
     */
    HashMap<String, String> setups;

    public EvaluationTest() {
        b = new Board();
        gen = new MovesGenerator();
        moves = new ArrayList<>();
        bitboard = new BitOperations();
        eval = new SimpleEvaluator();
        miniMax = new MiniMax(b);
        miniMaxAB = new MiniMaxAB(b);
        setups = new HashMap<>();

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

    //SIMPLE EVAL
    @Test
    public void simpleEvaluationStart() {
        int evalResult = eval.evaluateBoard(b);
        assertEquals(0, evalResult);
    }

    @Test
    public void simpleEvaluationWhiteDoesntHaveQueen() {
        b.unsetPiece(Piece.WHITE_QUEEN, Square.D1);
        int evalResult = eval.evaluateBoard(b);
        assertEquals(-900, evalResult);
    }

    //Normal MiniMax, with SimpleEval
    @Test
    public void testAllSetups() {
        initializeSetups();
        for (String bId : setups.keySet()) {
            b.loadFromFen(bId);
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMax.launch(b, 3);
            System.out.println("(depth 3) Minmax took: " + (System.currentTimeMillis() - startTime));
            assertEquals(setups.get(bId), bestMove.toString());
        }
    }

    //Alpha-Beta, with SimpleEval
    @Test
    public void testAllSetupsAB() {
        initializeSetups();
        for (String bId : setups.keySet()) {
            b.loadFromFen(bId);
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMaxAB.launch(b, 4);
            System.out.println("(depth 4) Minmax w/ AB took: " + (System.currentTimeMillis() - startTime));
            assertEquals(setups.get(bId), bestMove.toString());
        }
    }

    private void initializeSetups() {
        freeQueenSetup();
        checkMateSetup();
        freeQueenWithForkSetup();
        freeQueenWithDiscoverAttackSetup();
    }

    //Setups
    private void freeQueenSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.B5);
        b.setPiece(Piece.WHITE_QUEEN, Square.A1);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.G5);
        setups.put(b.getFen(), "a1h1");

        b.setSideToMove(Side.BLACK);
        setups.put(b.getFen(), "h1a1");
    }

    private void checkMateSetup() {
        b.clear();
        b.setSideToMove(Side.BLACK);
        b.setPiece(Piece.WHITE_KING, Square.A5);
        b.setPiece(Piece.WHITE_QUEEN, Square.B8);
        b.setPiece(Piece.BLACK_QUEEN, Square.G1);
        b.setPiece(Piece.BLACK_KING, Square.C5);
        setups.put(b.getFen(), "g1a1");

    }

    private void freeQueenWithForkSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.B5);
        b.setPiece(Piece.WHITE_KNIGHT, Square.E4);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.H5);
        setups.put(b.getFen(), "e4g3");
    }

    private void freeQueenWithDiscoverAttackSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C5);
        b.setPiece(Piece.WHITE_QUEEN, Square.D3);
        b.setPiece(Piece.BLACK_QUEEN, Square.H5);
        b.setPiece(Piece.BLACK_KING, Square.E5);
        setups.put(b.getFen(), "d3d5");

    }

}
