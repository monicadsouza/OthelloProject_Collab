import java.util.ArrayList;

/**
* Our AI class implementing alpha-beta pruning algorithm */
public class OthelloAI22 implements IOthelloAI {
    private int depth = 6; // initializes the maximum depth of search tree

    /**
     * Calculates the best move to make for the given game state. Prints the running time after each move.
     * @param s The current state of the game in which it should be the AI's turn.
     * @return the position where the AI wants to put its token.
     * Returns (-1, -1) if no moves are possible.
     */
    public Position decideMove(GameState s) {
        long startTime = System.nanoTime();
        ArrayList<Position> moves = s.legalMoves();
        Position bestP = null;
        if (!moves.isEmpty()) {
            int bestV = Integer.MIN_VALUE;
            int alpha = Integer.MIN_VALUE; // initializes alpha value to -infinity
            int beta = Integer.MAX_VALUE; // initializes beta value to +infinity
            for (Position p : moves) {
                GameState x = move(p, s);
                if(s.getPlayerInTurn()==1) { // MAX player
                    int value = maxValue(x, alpha, beta, this.depth);
                    if (value > bestV) {
                        bestV = value;
                        bestP = p;
                    }
                }
                else { // MIN player
                    int bestV2 = Integer.MAX_VALUE;
                    int value = minValue(x, alpha, beta, this.depth);
                    if(value < bestV2) {
                        bestV = value;
                        bestP = p;
                    }
                }
            }
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            System.out.println("Time: " + totalTime);
            return bestP;
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Time: " + totalTime);
        return new Position(-1, -1);
    }

    /**
     * Calculates the MAX value
     * @param s The duplicated state when the move is simulated
     * @param alpha Alpha value
     * @param beta Beta value
     * @param depth Current depth of the tree
     * @return the MAX value
     */
    public int maxValue(GameState s, int alpha, int beta, int depth) {
        if (depth == 0 || s.isFinished()) { // returns the final score when the depth is 0 or when the game is finished
            return finalScore(s);
        }
        int bestV = alpha;
        ArrayList<Position> moves = s.legalMoves();
        for (Position p : moves) {
            GameState x = move(p, s);
            bestV = Math.max(bestV, minValue(x, alpha, beta, depth - 1)); // calls minValue recursively
            if (bestV >= beta)
                return bestV; // cut-off
            alpha = Math.max(alpha, bestV); // updates alpha value
        }
        return bestV;
    }

    /**
     * Calculates the MIN value
     * @param s The duplicated state when the move is simulated
     * @param alpha Alpha value
     * @param beta Beta value
     * @param depth Current depth of the tree
     * @return the MIN value
     */
    public int minValue(GameState s, int alpha, int beta, int depth) {
        if (depth == 0 || s.isFinished()) {  // returns the final score when the depth is 0 or when the game is finished
            return finalScore(s);
        }
        int bestV = beta;
        ArrayList<Position> moves = s.legalMoves();
        for (Position p : moves) {
            GameState x = move(p, s);
            bestV = Math.min(bestV, maxValue(x, alpha, beta, depth - 1)); // calls maxValue recursively
            if (bestV <= alpha)
                return bestV; // cut-off
            beta = Math.min(beta, bestV); // updates beta value
        }
        return bestV;
    }

    /**
     * Simulates the move and returns the duplicated state
     * @param s The current state of the game in which it should be the AI's turn.
     * @param p possible position within the legal moves
     * @return The duplicated state when the move is simulated
     */
    public GameState move(Position p, GameState s) {
        GameState copy = new GameState(s.getBoard(), s.getPlayerInTurn());
        copy.insertToken(p);
        return copy;
    }

    /**
     * Calculates the final score using the heuristic
     * @param s The duplicated state when the move is simulated
     * @return the final score using the heuristic
     */
    public int finalScore(GameState s) {
        EvalGame e = new EvalGame(s);
        return e.evaluate();
    }

    public static void main(String[] args) {
        OthelloAI22 a = new OthelloAI22();
        GameState s = new GameState(6, 1);
        a.decideMove(s);
    }
}