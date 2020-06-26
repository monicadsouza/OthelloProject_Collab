
/**
 * The class that provides the heuristic to calculate the final score for AI22 */

public class EvalGame {
    private static final int CORNER = 20;               // 4 corner positions
    private static final int INNER_CORNER = 15;         // 4 inner corner positions
    private static final int SIDE_ADJACENT_CORNER = 10; // side positions adjacent to the corner
    private static final int SIDE = 7;                  // side positions
    private int board[][];                              // current board
    GameState state;                                    // current state


    public EvalGame(GameState s){
        board = s.getBoard();
        state = s;
    }

    /**
     * Evaluates the final score for each positions by calling relevant method per position
     * @return the final score
     */
    public int evaluate() {
        int finalScore = 0;

        //Top left corner
        finalScore += corner(0, 0);
        // Top right corner
        finalScore += corner(0, board.length - 1);
        //Bottom left corner
        finalScore += corner(board.length - 1, 0);
        //Bottom right corner
        finalScore += corner(board.length - 1, board.length - 1);

        //Top side of board
        finalScore += row(0);
        //Bottom side of board
        finalScore += row(board.length - 1);
        //Left side of board
        finalScore += col(0);
        // Right side of board
        finalScore += col(board.length- 1);

        //The inner corner to the top left corner
        finalScore += innerCorner(0, 0, 1, 1);
        //The inner corner to the top right corner
        finalScore += innerCorner(0, board.length - 1, 1, board.length - 2);
        //The inner corner to the bottom left corner
        finalScore += innerCorner(board.length - 1, 0, board.length - 2, 1);
        //The inner corner to the bottom right corner
        finalScore += innerCorner(board.length - 1, board.length - 1, board.length - 2, board.length - 2);

        finalScore += scoreDiff(this.state);

        return finalScore;
    }

    /**
     * Count the tokens for player 1(black) and 2(white), and returns the difference
     * @param s current state
     * @return Score based on the raw count token difference
     */
    public int scoreDiff(GameState s) {
        int black = s.countTokens()[0];
        int white = s.countTokens()[1];
        if (s.getPlayerInTurn() == 1) {
            return black - white;
        } else {
            return white - black;
        }
    }
    /**
     * Evaluates score for the corner position.
     * If the player 1(black) holds the position, returns the positive value, otherwise the negative value.
     * @param row row number of the corner position (0 / board.length-1)
     * @param col col number of the corner position (0 / board.length-1)
     * @return designated corner value (+/-)
     */
    public int corner(int row, int col) {
        if (board[row][col] == 1) {
            return CORNER;
        } else if (board[row][col] == 2) {
            return -CORNER;
        }
        return 0;
    }

    /**
     * Evaluates score for the inner corner position.
     * If the corner position is empty it is disadvantageous to hold this position.
     * Thus if the player 1(black) holds the position, returns the negative value, otherwise the positive value.
     * @param corner_row row number of the corner position (0 / board.length-1)
     * @param corner_col col number of the corner position (0 / board.length-1)
     * @param row row number of the inner corner position (1 / board.length-2)
     * @param col col number of the inner corner position (1 / board.length-2)
     * @return designated inner corner value (-/+)
     */
    private int innerCorner(int corner_row, int corner_col, int row, int col) {
        if (board[corner_row][corner_col] == 0){
            if (board[row][col] == 1) {
                return -INNER_CORNER;
            }
            else if (board[row][col] == 2){
                return INNER_CORNER;
            }
        }
        return 0;
    }

    /**
     * Evaluates score for the top and bottom side
     * @param row row number of the position (0 / board.length-1)
     * @return top and bottom side position scores
     */
    public int row(int row) {
        int rowScore = 0;
        int left = 1, right = board.length-1; // col counters to loop through the row horizontally

        // If the corner position is empty it is disadvantageous to hold the side adjacent position.
        // Thus if the player 1(black) holds the position, returns the negative value, otherwise the positive value.
        if (board[0][0] == 0) {
            left++;
            if (board[row][1] == 1) {
                rowScore += -SIDE_ADJACENT_CORNER;
            } else if (board[row][1] == 2) {
                rowScore += SIDE_ADJACENT_CORNER;
            }
        }
        if (board[row][board.length-1] == 0) {
            right--;
            if (board[row][board.length-2] == 1) {
                rowScore += -SIDE_ADJACENT_CORNER;
            } else if (board[row][board.length-2] ==2) {
                rowScore += SIDE_ADJACENT_CORNER;
            }
        }

        // loops through the row horizontally and evaluates side positions
        for (int i = left; i <right; i++){
            if (board[row][i] == 1) {
                rowScore += SIDE;
            } else if (board[row][i] ==2 ){
                rowScore += -SIDE;
            }
        }
        return rowScore;
    }

    /**
     * Evaluates score for the left and right side
     * @param col col number of the position (0 / board.length-1)
     * @return left and right side position scores
     */
    private int col(int col){
        int colScore = 0;
        int top = 1, bottom = board.length - 1; // row counters to loop through the col vertically

        // If the corner position is empty it is disadvantageous to hold the side adjacent position.
        // Thus if the player 1(black) holds the position, returns the negative value, otherwise the positive value.
        if (board[0][col] == 0){
            top++;
            if (board[1][col] == 1) {
                colScore += -SIDE_ADJACENT_CORNER;
            }
            else if (board[1][col] == 2){
                colScore += SIDE_ADJACENT_CORNER;
            }
        }
        if (board[board.length - 1][col] == 0){
            bottom--;
            if (board[board.length - 2][col] == 1){
                colScore += -SIDE_ADJACENT_CORNER;
            }
            else if (board[board.length - 2][col] == 2){
                colScore += SIDE_ADJACENT_CORNER;
            }
        }

        // loops through the col vertically and evaluates side positions
        for (int i = top; i < bottom; i++) {
            if (board[i][col] == 1){
                colScore += SIDE;
            }
            else if (board[i][col] == 2){
                colScore += -SIDE;
            }
        }
        return colScore;
    }

}
