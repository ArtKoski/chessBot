package chess.bot;

import chess.engine.GameState;
import java.util.logging.Level;
import java.util.logging.Logger;
import datastructureproject.Board.*;
import datastructureproject.MiniMaxAB;

/**
 * Copy of TestBot with miniMax in use.
 *
 * @author artkoski
 */
public class BlunderBot implements ChessBot {

    private Board b;
    private final MiniMaxAB miniMax;

    public BlunderBot() {
        this.b = new Board();
        miniMax = new MiniMaxAB(b);
    }

    /**
     * @param gs Current gamestate
     * @return UCI String representation of a move
     */
    @Override
    public String nextMove(GameState gs) {
        parseLatestMove(gs);

        Move myMove;
        try {
            if (gs.getMoveCount() < 10) {
                myMove = this.miniMax.launch(b, 4);
            } else {
                myMove = this.miniMax.launch(b, 4);

            }
            if (myMove != null) {
                //Transform the move into a UCI string representation
                return myMove.toString();
            }

        } catch (Exception ex) {
            Logger.getLogger(BlunderBot.class.getName()).log(Level.SEVERE, null, ex);
        }

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
