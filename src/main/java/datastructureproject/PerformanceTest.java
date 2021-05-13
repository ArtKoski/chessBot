package datastructureproject;

import chess.bot.ChessBot;
import chess.engine.GameState;
import datastructureproject.Board.Side;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for testing
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
     * Testing how long the first x moves take.
     *
     * @param args
     */
    public static void main(String[] args) {
        newGame();
        GameState cur;
        int turns = 100;
        Side side;
        for (int i = 0; i < turns; i++) {

            long startTime = System.currentTimeMillis();
            String move = bot.nextMove(gsList.get(gsList.size() - 1));
            long timeTaken = System.currentTimeMillis() - startTime;
            timeEval += timeTaken;

            cur = new GameState();
            cur.moves.addAll((gsList.get(gsList.size() - 1)).moves);
            cur.moves.add(move);
            gsList.add(cur);
            if (i % 2 == 0) {
                side = Side.WHITE;
            } else {
                side = Side.BLACK;
            }
            System.out.println(side + "'s move: " + move +" | time taken: " +  timeTaken);

        }
        System.out.println("(time) total: " + timeEval + ", avg: " + (timeEval / turns));

    }
}
