package datastructureproject;

import chess.bot.ChessBot;
import chess.bot.TestBot;
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
    private static int timeEval = 0;

    public void setGsList(List<GameState> gsList) {
        this.gsList = gsList;
    }

    public static void newGame() {
        GameState gs = new GameState();
        gsList.add(gs);

    }

    public static void times(long time1, long time2) {
        timeEval += (time2 - time1);
    }

    /**
     * ATM just testing how long the first 5-15 moves take.
     *
     * @param args
     */
    public static void main(String[] args) {
        newGame();
        GameState cur;
        int turns = 10;

        for (int i = 0; i < turns; i++) {

            long startTime = System.currentTimeMillis();
            String move = bot.nextMove(gsList.get(gsList.size() - 1));
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("time:" + timeTaken);
            timeEval += timeTaken;
            /*
            startTime = System.currentTimeMillis();
            String move2 = bot2.nextMove(gsList.get(gsList.size() - 1));
            times(timeTaken, System.currentTimeMillis() - startTime);
             */
            cur = new GameState();
            cur.moves.addAll((gsList.get(gsList.size() - 1)).moves);
            cur.moves.add(move);
            gsList.add(cur);

            System.out.println("latest move: " + move);
        }
        System.out.println("tot: " + timeEval + ", avg: " + (timeEval / turns));

    }
}
