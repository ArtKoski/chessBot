package movegeneration;

import datastructureproject.Board.*;
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
    //MiniMaxABZobrist miniMaxZobrist = new MiniMaxABZobrist(b);

    /*
    First param is Board itself, second String is the best (expected) play.
     */
    HashMap<Board, String> setups;

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

    //Normal MiniMax
    //@Test
    public void testAllSetups() {
        initializeDepth3Setups();

        int depth = 4;
        int sum = 0;
        for (Board board : setups.keySet()) {
            b = board;
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMax.launch(b, depth);
            long timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("Minmax took: " + timeTaken);
            sum += timeTaken;
            assertEquals(setups.get(b), bestMove.toString());
        }

        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }

    //Alpha-Beta, with ComplexEval
    @Test
    public void testAllSetupsAB() {
        initializeDepth3Setups();
        //initializeDepth5Setups();

        int depth = 4;
        int sum = 0;
        for (Board board : setups.keySet()) {
            b = board;

            long startTime = System.currentTimeMillis();
            Move bestMove = miniMaxAB.launch(b, depth);

            double timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("Minmax w/ AB took: " + timeTaken + ", setup was: " + b.getBitBoard());
            sum += timeTaken;

            assertEquals(setups.get(b), bestMove.toString());
        }
        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }

    //Alpha-Beta, with ComplexEval and Zobrist
    //@Test
    /*public void testAllSetupsABZobrist() {
        initializeDepth3Setups();
        //initializeDepth5Setups();

        int depth = 5;
        int sum = 0;
        for (Board board : setups.keySet()) {
            System.out.println("-------------------------------------");
            b = board;
            long startTime = System.currentTimeMillis();
            Move bestMove = miniMaxZobrist.launch(b, depth);

            double timeTaken = (System.currentTimeMillis() - startTime);
            System.out.println("Minmax w/ AB&Zobrist took: " + timeTaken + ", setup was: " + b.getBitBoard());
            sum += timeTaken;

            assertEquals(setups.get(b), bestMove.toString());
            System.out.println("best move was " + bestMove);

            System.out.println("-------------------------------------");
        }
        System.out.println("AVG time taken w/ depth " + depth + ": " + sum / setups.size());
    }*/
    /**
     * Min depth 3 required
     */
    private void initializeDepth3Setups() {
        freeQueenSetup();
        checkMateSetup();
        royalForkSetup();
        freeQueenWithDiscoverAttackSetup();
        //mateInTwoWithPromotionSetup();

    }

    /**
     * Min depth 5 required
     */
    private void initializeDepth5Setups() {
        mateInThreeSetup();

    }

    //Setups
    private void freeQueenSetup() {
        b = new Board();
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.B5);
        b.setPiece(Piece.WHITE_QUEEN, Square.A1);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.G5);
        setups.put(b, "a1h1");

        b = new Board();
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.B5);
        b.setPiece(Piece.WHITE_QUEEN, Square.A1);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.G5);
        b.setSideToMove(Side.BLACK);
        setups.put(b, "h1a1");
    }

    private void checkMateSetup() {
        b = new Board();
        b.clear();
        b.setSideToMove(Side.BLACK);
        b.setPiece(Piece.WHITE_KING, Square.A5);
        b.setPiece(Piece.WHITE_QUEEN, Square.B8);
        b.setPiece(Piece.BLACK_QUEEN, Square.G1);
        b.setPiece(Piece.BLACK_KING, Square.C5);
        setups.put(b, "g1a1");

    }

    private void freeQueenWithDiscoverAttackSetup() {
        b = new Board();
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C5);
        b.setPiece(Piece.WHITE_QUEEN, Square.D3);
        b.setPiece(Piece.BLACK_QUEEN, Square.H5);
        b.setPiece(Piece.BLACK_KING, Square.E5);
        setups.put(b, "d3d5");

    }

    private void royalForkSetup() {
        b = new Board();
        b.clear();
        b.setPiece(Piece.WHITE_KING, Square.C5);
        b.setPiece(Piece.WHITE_PAWN, Square.A2);
        b.setPiece(Piece.WHITE_KNIGHT, Square.E4);
        b.setPiece(Piece.BLACK_QUEEN, Square.H1);
        b.setPiece(Piece.BLACK_KING, Square.H5);
        setups.put(b, "e4g3");

    }

    private void mateInThreeSetup() {
        b = new Board();
        b.unsetPiece(Piece.BLACK_KNIGHT, Square.B8);
        b.unsetPiece(Piece.BLACK_KNIGHT, Square.G8);
        b.unsetPiece(Piece.BLACK_PAWN, Square.E7);
        b.unsetPiece(Piece.BLACK_QUEEN, Square.D8);
        b.unsetPiece(Piece.WHITE_PAWN, Square.D2);
        b.unsetPiece(Piece.WHITE_PAWN, Square.F2);
        b.unsetPiece(Piece.WHITE_PAWN, Square.E2);
        b.unsetPiece(Piece.WHITE_PAWN, Square.G2);
        b.unsetPiece(Piece.WHITE_KNIGHT, Square.B1);
        b.unsetPiece(Piece.WHITE_KNIGHT, Square.G1);
        b.unsetPiece(Piece.WHITE_KING, Square.E1);

        b.setPiece(Piece.BLACK_KNIGHT, Square.E5);
        b.setPiece(Piece.BLACK_QUEEN, Square.F6);

        b.setPiece(Piece.WHITE_KING, Square.D4);
        b.setPiece(Piece.WHITE_PAWN, Square.E4);
        b.setPiece(Piece.WHITE_PAWN, Square.G3);
        b.setPiece(Piece.WHITE_KNIGHT, Square.C3);
        b.setPiece(Piece.WHITE_KNIGHT, Square.H3);
        b.setSideToMove(Side.BLACK);

        //  b.loadFromFen("r1b1kb1r/pppp1ppp/5q2/4n3/3KP3/2N3PN/PPP4P/R1BQ1B1R b kq");
        setups.put(b, "f8c5");
    }
    /*
    private void mateInTwoWithPromotionSetup() {
        b.clear();
        b.loadFromFen("1rb4r/pkPp3p/1b1P3n/1Q6/N3Pp2/8/P1P3PP/7K b");
        b.setSideToMove(Side.WHITE);
        setups.put(b.getFen(), "b5d5");
    }
     */
}
