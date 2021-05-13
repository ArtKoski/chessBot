package movegeneration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import datastructureproject.Board.*;

/**
 *
 * @author artkoski
 */
public class BitBoardTest {

    long startOfTheGame = 0xffff00000000ffffL;
    long startWhite = 0xffffL;
    long startBlack = 0xffff000000000000L;
    Board b;

    public BitBoardTest() {
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

    @Test
    public void bitboardCorrectBeginningOfGame() {
        assertEquals(startOfTheGame, b.getBitBoard());
        assertEquals(startWhite, b.getBitboard(Side.WHITE));
        assertEquals(startBlack, b.getBitboard(Side.BLACK));
    }

    @Test
    public void SquareToBB() {
        assertEquals(1, Square.A1.getSquareBB());
        assertEquals(1024, Square.C2.getSquareBB());
        assertEquals(68719476736L, Square.E5.getSquareBB());
    }

    @Test
    public void bitBoardUpdatesWithMoves() {
        b.doMove(new Move(Square.E2, Square.E4));
        assertEquals(0xffff00001000efffL, b.getBitBoard());
        b.doMove(new Move(Square.E7, Square.E5));
        assertEquals(0xffef00101000efffL, b.getBitBoard());
    }

    @Test
    public void bitboardUpdatesWithUndos() {
        b.doMove(new Move(Square.E2, Square.E4));
        b.undoMove();
        assertEquals(startOfTheGame, b.getBitBoard());
    }

    @Test
    public void BitBoardsUpdateWithCaptures() {
        b.clear();
        b.setPiece(Piece.BLACK_QUEEN, Square.C3);
        b.setPiece(Piece.WHITE_PAWN, Square.B2);
        b.doMove(new Move(Square.B2, Square.C3));
        assertEquals(0x40000L, b.getBitboard(Piece.WHITE_PAWN));
        assertEquals(0x0L, b.getBitboard(Piece.BLACK_QUEEN));
    }

    @Test
    public void bitboardsUpdateWithCaptureUndos() {
        b.clear();
        b.setPiece(Piece.BLACK_QUEEN, Square.C3);
        b.setPiece(Piece.WHITE_PAWN, Square.B2);
        b.doMove(new Move(Square.B2, Square.C3));
        b.undoMove();
        assertEquals(0x200L, b.getBitboard(Piece.WHITE_PAWN));
        assertEquals(0x40000L, b.getBitboard(Piece.BLACK_QUEEN));
    }

    @Test
    public void bitboardWithArgs() {
        b.doMove(new Move(Square.B1, Square.C3));
        long knightBB = b.getBitboard(Piece.WHITE_KNIGHT);
        assertEquals(0x40040L, knightBB);
    }

    @Test
    public void clear() {
        b.clear();
        assertEquals(0x0L, b.getBitBoard());
    }

    @Test
    public void setPiece() {
        b.clear();
        b.setPiece(Piece.WHITE_KNIGHT, Square.B5);

        assertEquals(b.getBitBoard(), 0x200000000L);
    }

    @Test
    public void unsetPiece() {
        b.unsetPiece(Piece.WHITE_KNIGHT, Square.B1);
        assertEquals(0xffff00000000fffdL, b.getBitBoard());
    }

    @Test
    public void boardCanBeCloned() {
        Board clone = b.clone();
        b.doMove(new Move(Square.E2, Square.E4));
        clone.doMove(new Move(Square.D2, Square.D4));

        assertNotEquals(clone.getBitBoard(), b.getBitBoard());
        b.doMove(new Move(Square.B1, Square.C3));
        assertNotEquals(clone.getSideToMove(), b.getSideToMove());
    }

    @Test
    public void piecesAreUpdatedProperly() {
        assertEquals(Piece.makePiece(PieceType.PAWN, Side.WHITE), b.getPiece(Square.A2));
        b.doMove(new Move(Square.A2, Square.A4));
        assertEquals(Piece.makePiece(PieceType.PAWN, Side.WHITE), b.getPiece(Square.A4));
        assertEquals(Piece.makeNone(), b.getPiece(Square.A2));
    }

    @Test
    public void piecesAreUpdatedWhenCaptured() {
        b.setPiece(Piece.BLACK_QUEEN, Square.C3);
        assertEquals(Piece.makePiece(PieceType.QUEEN, Side.BLACK), b.getPiece(Square.C3));
        b.doMove(new Move(Square.B2, Square.C3));
        assertEquals(Piece.makePiece(PieceType.PAWN, Side.WHITE), b.getPiece(Square.C3));

    }

}
