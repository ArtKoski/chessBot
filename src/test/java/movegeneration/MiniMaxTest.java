package movegeneration;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import datastructureproject.BitOperations;
import datastructureproject.*;
import datastructureproject.lists.MoveList;
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
public class MiniMaxTest {

    MovesGenerator gen;
    Board b;
    MoveList moves;
    BitOperations bitboard;
    MiniMax miniMax;
    MiniMaxAB miniMaxAB;
    MiniMaxABZobrist miniMaxZobrist = new MiniMaxABZobrist(b);

    /*
    First String is FEN notation as an ID, second String is the best (expected) play.
     */
    HashMap<String, String> setups;

    public MiniMaxTest() {
        b = new Board();
        gen = new MovesGenerator();
        moves = new MoveList();
        bitboard = new BitOperations();
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

    //Normal MiniMax, with SimpleEval
    //@Test
    public void testAllSetups() {
        initializeDepth3Setups();

        int depth = 4;
        int sum = 0;
        for (String bId : setups.keySet()) {
            b.loadFromFen(bId);
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMax.launch(b, depth);
            long timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("(depth 3) Minmax took: " + timeTaken);
            sum += timeTaken;
            assertEquals(setups.get(bId), bestMove.toString());
        }

        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }

    //Alpha-Beta, with ComplexEval
    //@Test
    public void testAllSetupsAB() {
        initializeDepth3Setups();
        //initializeDepth5Setups();

        int depth = 5;
        int sum = 0;
        for (String bId : setups.keySet()) {

            b.loadFromFen(bId);
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMaxAB.launch(b, depth);

            double timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("Minmax w/ AB took: " + timeTaken + ", setup was: " + b.getBitboard());
            sum += timeTaken;

            assertEquals(setups.get(bId), bestMove.toString());
        }
        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }

    //Alpha-Beta, with ComplexEval and Zobrist
    @Test
    public void testAllSetupsABZobrist() {
        initializeDepth3Setups();
        //initializeDepth5Setups();

        int depth = 5;
        int sum = 0;
        for (String bId : setups.keySet()) {

            b.loadFromFen(bId);
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMaxZobrist.launch(b, depth);

            double timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("Minmax w/ AB&Zobrist took: " + timeTaken + ", setup was: " + b.getBitboard());
            sum += timeTaken;

            assertEquals(setups.get(bId), bestMove.toString());
        }
        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }

    /**
     * Min depth 3 required
     */
    private void initializeDepth3Setups() {
        freeQueenSetup();
        checkMateSetup();
        freeQueenWithForkSetup();
        freeQueenWithDiscoverAttackSetup();
        mateInTwoWithPromotionSetup();
        royalForkSetup();
    }

    /**
     * Min depth 5 required
     */
    private void initializeDepth5Setups() {
        mateInThreeSetup();

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

    private void royalForkSetup() {
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C5);
        b.setPiece(Piece.WHITE_KNIGHT, Square.E4);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.H5);
        setups.put(b.getFen(), "e4g3");

    }

    private void mateInThreeSetup() {
        b.clear();
        b.loadFromFen("r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq");
        setups.put(b.getFen(), "f8c5");
    }

    private void mateInTwoWithPromotionSetup() {
        b.clear();
        b.loadFromFen("1rb4r/pkPp3p/1b1P3n/1Q6/N3Pp2/8/P1P3PP/7K b");
        b.setSideToMove(Side.WHITE);
        setups.put(b.getFen(), "b5d5");
    }

}
