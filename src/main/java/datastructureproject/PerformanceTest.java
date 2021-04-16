package datastructureproject;

import chess.bot.ChessBot;
import chess.engine.GameState;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.ArrayList;
import java.util.List;

/**
 * Will be used for performance testing (probably)
 *
 */

public class PerformanceTest {

    private static final ChessBot bot = new TemporaryBot();
    private static List<GameState> gsList = new ArrayList();

    public void setGsList(List<GameState> gsList) {
        this.gsList = gsList;
    }

    public static void newGame() {
        GameState gs = new GameState();
        gsList.add(gs);

    }

    public static void main(String[] args) {
        newGame();
        GameState cur;

        while (true) {
            String move = bot.nextMove(gsList.get(gsList.size() - 1));

            cur = new GameState();
            cur.moves.addAll((gsList.get(gsList.size() - 1)).moves);
            cur.moves.add(move);
            gsList.add(cur);

            System.out.println("latest move: " + move);

        }

    }
}
