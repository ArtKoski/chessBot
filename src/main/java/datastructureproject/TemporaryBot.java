package datastructureproject;

import datastructureproject.Evaluation.BoardEvaluation;
import chess.bot.ChessBot;
import chess.engine.GameState;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.bhlangonijr.chesslib.move.*;

import java.util.Random;

import com.github.bhlangonijr.chesslib.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * COPY OF TESTBOT FOR TESTING
 *
 * @author artkoski
 */
public class TemporaryBot implements ChessBot {

    private final Random random;
    private Board b;
    private final MovesGenerator moveGenerator;
    private final MiniMaxAB miniMax;

    public TemporaryBot() {
        this.random = new Random();
        this.b = new Board();
        moveGenerator = new MovesGenerator();
        miniMax = new MiniMaxAB(b);
    }

    /**
     * @param gs Current gamestate
     * @return UCI String representation of a mvoe
     */
    @Override
    public String nextMove(GameState gs) {
        parseLatestMove(gs);

        Move myMove;
        try {
            //Generate a chesslib move based on the position
            myMove = this.miniMax.launch(b, 3);

            if (myMove != null) {
                //Transform the move into a UCI string representation
                return myMove.toString();
            }

        } catch (Exception ex) {
            Logger.getLogger(TemporaryBot.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Fetches a random move based on current board state
     *
     * @return A chesslib move
     * @throws MoveGeneratorException if unable to generate legal moves
     */
    public Move getMove() throws MoveGeneratorException {
       return null;
    }

    public Board getBoard() {
        return this.b;
    }

    /**
     * Parses a move in UCI move into the chess engine's move data type and
     * updates the engine's board state
     *
     * @param gs Current gamestate
     */
    public void parseLatestMove(GameState gs) {
        this.b = new Board();

        // We play all of the moves onto a new board to ensure a previously
        // started game can be resumed correctly, inefficient but it works
        if (!gs.moves.isEmpty()) {
            gs.moves.forEach(moveString -> {
                String startingString = moveString.substring(0, 2).toUpperCase();
                String endingString = moveString.substring(2, 4).toUpperCase();
                String promoteString = moveString.length() > 4 ? moveString
                        .substring(4).toUpperCase() : "".toUpperCase();
                this.setMove(startingString, endingString, promoteString);
            });
        }
    }

    /**
     * Transforms a move from UCI to a chesslib move and makes the move
     *
     * @param starting UCI String starting square
     * @param ending UCI String ending square
     * @param promote UCI String for potential promotion moves
     */
    public void setMove(String starting, String ending, String promote) {
        String promotionPiece = "";
        if (promote.length() > 0) {
            Piece piece = b.getPiece(Square.valueOf(starting));
            String side = piece.getPieceSide().value();
            switch (promote) {
                case "R":
                    promotionPiece = side + "_ROOK";
                    break;
                case "B":
                    promotionPiece = side + "_BISHOP";
                    break;
                case "N":
                    promotionPiece = side + "_KNIGHT";
                    break;
                case "Q":
                    promotionPiece = side + "_QUEEN";
                    break;
                default:
                    throw new Error("Something went wrong parsing the promoted piece");
            }
        }
        Move latestMove = promote.isEmpty() ? new Move(
                Square.fromValue(starting),
                Square.valueOf(ending)) : new Move(Square.fromValue(starting),
                Square.valueOf(ending), Piece.fromValue(promotionPiece));
        this.b.doMove(latestMove);
    }
}
