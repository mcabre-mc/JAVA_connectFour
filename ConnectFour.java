package hw10;

/**
 * A basic implementation of the Connect Four game.
 *  * @author Vijay Gangayadi Venkatesh
 *  * @author Sagar Suhas Karambelkar
 */
public class ConnectFour {
    /** the number of rows */
    public final static int ROWS = 6;
    /** the number of columns */
    public final static int COLS = 7;
    /** how big a line one needs to win */
    public final static int WIN_LEN = 4;

    /**
     * Used to indicate a move that has been made on the board.
     */
    public enum Move {
        PLAYER_ONE('X'),
        PLAYER_TWO('O'),
        NONE('.');

        private char symbol;
        private Move(char symbol) {
            this.symbol = symbol;
        }
        public char getSymbol() {
            return symbol;
        }
    }

    /**
     * The number of rows in the board.
     */
    private int rows;

    /**
     * The number of columns in the board.
     */
    private int cols;

    /**
     * The board.
     */
    private Move[][] board;

    /**
     * Used to keep track of which player's turn it is; 0 for player 1, and 1
     * for player 2.
     */
    private int turn;

    /**
     *  The last column a piece was placed.  Used for win checking.
     */
    private int lastCol;

    /**
     * The row the last piece was placed.  Used for win checking.
     */
    private int lastRow;

    /**
     * Creates a Connect Four game using a board with the standard number of
     * rows (6) and columns (7).
     */
    public ConnectFour() {
        this(ROWS, COLS);
    }

    /**
     *  The getter send the current board with the moves.
     * @return the current board is returned.
     */
    public Move[][] getBoard() {
        return board;
    }

    /**
     * Creates a Connect Four game using a board with the specified number of
     * rows and columns. Assumes that player 1 is the first to move.
     *
     * @param rows The number of rows in the board.
     * @param cols The number of columns in the board.
     */
    public ConnectFour(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        board = new Move[cols][rows];
        for(int col=0; col<cols; col++) {
            for(int row=0; row < rows; row++) {
                board[col][row] = Move.NONE;
            }
        }

        turn = 0;
    }

    /**
     * Makes a move for the player whose turn it is. If the move is successful,
     * play automatically switches to the other player's turn.
     *
     * @param column The column in which the player is moving.
     *
     * @throws ConnectFourException If the move is invalid for any reason.
     */
    public void makeMove(int column) throws ConnectFourException {
        //Procedure: Iterate over every row for one column and place coin in last column
        Move coin;
        boolean columnEmpty = true;

        if (turn == 0)
            coin = Move.PLAYER_ONE;
        else
            coin = Move.PLAYER_TWO;

        if (column >= cols)
        {
            throw new ConnectFourException("Column out of bounds!");
        }
        if (board[column][0] != Move.NONE)
        {
            //Top slot full, no room for any more coin
            throw new ConnectFourException("Move cannot be made as column is full");
        }
        for(int i = rows-1; i >= 0 ; i--)
        {
            if(board[column][i] == Move.NONE)
            {
                board[column][i] = coin;
                columnEmpty = false;
                break;
            }
        }
        //Column empty
        if (columnEmpty)
        {
            board[column][rows-1] = coin;
        }
        turn = (turn+1) % 2;
    }

    /**
     * The function checks if there are any four repeated symbols in the board in horizontal direction.
     * @return true if there are four repeated symbols in the board else returns false.
     */
    public boolean checkHorizontal()
    {
        for(int i = 0; i < rows;i++)
        {
            for(int j =0; j <= cols - WIN_LEN; j++)
            {
                Move currentCoin = board[j][i];
                if(currentCoin != Move.NONE) {
                    //Only check if coin is not none i.e there is a player coin in the slot
                    for (int k = j + 1; k < j + WIN_LEN; k++) {
                        Move nextCoin = board[k][i];
                        if (currentCoin != nextCoin) {
                            break;
                        }
                        if (k == j + WIN_LEN - 1) {
                            return true; // If next three coins are same
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * The function checks if there are any four repeated symbols in the board in vertical direction.
     * @return true if there are four repeated symbols in the board else returns false.
     */
    public boolean checkVertical(){
        for(int i = 0; i < cols;i++)
        {
            for(int j =0; j <= rows - WIN_LEN; j++)
            {
                Move currentCoin = board[i][j];
                if(currentCoin != Move.NONE) {
                    //Only check if coin is not none i.e there is a player coin in the slot
                    for (int k = j + 1; k < j + WIN_LEN; k++) {
                        Move nextCoin = board[i][k];
                        if (currentCoin != nextCoin) {
                            break;
                        }
                        if (k == j + WIN_LEN - 1) {
                            return true; // If next three coins are same
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * The function checks if there are any four repeated symbols in the board in diagonal directions.
     * @return true if there are four repeated symbols in the board else returns false.
     */
    public boolean checkDiagonal() {
        for (int i = rows - 1; i > rows - WIN_LEN; i--) {
            for (int j = 0; j < cols; j++) {
                Move currentCoin = board[j][i];
                //Check right diagonal
                if (currentCoin != Move.NONE) {
                    if (j <= cols - WIN_LEN) {
                        for (int k = i - 1,l = j+1,counter = 1
                             ; counter < WIN_LEN; k--,l++,counter++) {
                            Move nextCoin = board[l][k];
                            if (currentCoin != nextCoin)
                                break;
                            if (counter ==  WIN_LEN - 1)
                                return true;

                        }

                    }
                    // Check left diagonal
                    if (j >= cols - WIN_LEN) {
                        for (int k = i - 1,l = j-1,counter = 1;
                             counter < WIN_LEN; k--,l--,counter++) {
                            Move nextCoin = board[l][k];
                            if (currentCoin != nextCoin)
                                break;
                            if (counter == WIN_LEN - 1)
                                return true;

                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * Look over the entire board for any N-in-a-row situations.
     * (By N we mean {@link #WIN_LEN}.)
     * @return true if one of the players has an N-in-a-row situation.
     */
    public boolean hasWonGame() {
        //TODO YOUR CODE HERE
       if (checkHorizontal() || checkDiagonal() || checkVertical())
           return true;
       return false;
    }

    /**
     * The function decides which player has to play.
     * @param turn int which represents which player is currently going to play.
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Checks to see if the game is tied - no NONE moves left in board.  This
     * is called after hasGameWon.
     *
     * @return whether game is tied or not
     */
    public boolean hasTiedGame() {
        for (int i = 0; i < cols; i++){
            if ( board[i][0] == Move.NONE)
                return false;
        }
        return !(hasWonGame());
    }

    /**
     * Returns a {@link String} representation of the board, suitable for
     * printing.
     *
     * @return A {@link String} representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                builder.append('[');
                builder.append(board[c][r].getSymbol());
                builder.append(']');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
