import java.io.Serializable;
import java.util.Stack;

/**
 * The Grid class represents the Connect Four game board, a 6x7 grid where players
 * take turns to drop discs. This class includes methods for placing a disc, checking
 * for a win condition, and undoing moves.
 */
public class Grid implements Serializable {
    private static long serialVersionUID = 1L;
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private final char[][] grid; // 2D array representing the game grid
    private final Stack<Disc> moveHistory; // Stack to keep track of moves for undo functionality

    /**
     * Constructs a Grid object, initializing a 6x7 grid with empty spaces and an empty move history.
     */
    public Grid() {
        grid = new char[ROWS][COLUMNS];
        moveHistory = new Stack<>();

        // Initialize each cell in the grid with an empty space
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    /**
     * Attempts to place a disc in the specified column.
     *
     * @param symbol The symbol ('X' or 'O') representing the current player.
     * @param col    The column where the player wants to drop the disc (0-indexed).
     * @return       True if the disc was successfully placed, false otherwise.
     * @throws ColumnFullException if the specified column is already full.
     */
    public boolean makeMove(char symbol, int col) throws ColumnFullException {
        // If the column is full, throw an exception
        if (checkColumnFull(col)) {
            throw new ColumnFullException("Column " + (col + 1) + " is full.");
        }
        // Iterate from the bottom and place the disc in the first empty spot
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = symbol;
                // Record the move
                moveHistory.push(new Disc(symbol, row, col));
                return true;
            }
        }
        return false;
    }

    /**
     * Undoes the last move by removing the topmost disc in the most recent column.
     *
     * @return True if a move was successfully undone, false if there was no move to undo.
     */
    public boolean undoMove() {
        // Check if there are any moves to undo
        if (!moveHistory.isEmpty()) {
            // Retrieve and remove the last move from the history stack
            Disc lastMove = moveHistory.pop();

            // Clear the disc from the grid at the stored row and column
            grid[lastMove.getRow()][lastMove.getCol()] = ' ';

            return true; // Move was successfully undone
        }

        // If no moves were made, print a message and return false
        System.out.println("No moves to undo.");
        return false; // No moves to undo
    }


    /**
     * Checks if a column is full by inspecting the top row of the specified column.
     *
     * @param col The column to check (0-indexed).
     * @return    True if the column is full, false otherwise.
     */
    public boolean checkColumnFull(int col) {
        return grid[0][col] != ' ';
    }

    /**
     * Checks if the entire grid is full.
     *
     * @return True if the grid is full, false otherwise.
     */
    public boolean isGridFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (!checkColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the most recent move resulted in a win by forming four consecutive symbols.
     *
     * @param col    The column where the most recent disc was placed (0-indexed).
     * @param symbol The symbol of the player who made the move.
     * @return       True if the move results in a win, false otherwise.
     */
    public boolean isWinningMove(int col, char symbol) {
        int row = findRowOfLastMove(col, symbol);
        if (row == -1) return false; // If the disc is not found, no win condition is possible

        // Check four directions for a win: vertical, horizontal, and two diagonals
        return checkDirection(row, col, symbol, 1, 0) || // Vertical
                checkDirection(row, col, symbol, 0, 1) || // Horizontal
                checkDirection(row, col, symbol, 1, 1) || // Diagonal down-right
                checkDirection(row, col, symbol, 1, -1);  // Diagonal down-left
    }

    /**
     * Finds the row of the most recent disc placed in a specified column for a given symbol.
     *
     * @param col    The column to check (0-indexed).
     * @param symbol The symbol of the player who made the move.
     * @return       The row index of the disc, or -1 if the disc is not found.
     */
    private int findRowOfLastMove(int col, char symbol) {
        for (int row = 0; row < ROWS; row++) {
            if (grid[row][col] == symbol) return row;
        }
        return -1;
    }

    /**
     * Checks a specified direction from a starting point to determine if four consecutive symbols are found.
     *
     * @param row       The starting row index.
     * @param col       The starting column index.
     * @param symbol    The symbol to look for (player's symbol).
     * @param rowDelta  The row increment for the direction (e.g., 1 for downward).
     * @param colDelta  The column increment for the direction (e.g., 1 for right).
     * @return          True if four consecutive symbols are found, false otherwise.
     */
    private boolean checkDirection(int row, int col, char symbol, int rowDelta, int colDelta) {
        int count = 1;
        count += countConsecutive(row, col, symbol, rowDelta, colDelta); // Check forward in the direction
        count += countConsecutive(row, col, symbol, -rowDelta, -colDelta); // Check backward in the opposite direction
        return count >= 4;
    }

    /**
     * Counts consecutive symbols in a specified direction starting from a given position.
     *
     * @param row       The starting row index.
     * @param col       The starting column index.
     * @param symbol    The symbol to look for (player's symbol).
     * @param rowDelta  The row increment for the direction.
     * @param colDelta  The column increment for the direction.
     * @return          The count of consecutive symbols in the specified direction.
     */
    private int countConsecutive(int row, int col, char symbol, int rowDelta, int colDelta) {
        int count = 0;
        int newRow = row + rowDelta, newCol = col + colDelta;
        // Traverse in the specified direction and count matching symbols
        while (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS && grid[newRow][newCol] == symbol) {
            count++;
            newRow += rowDelta;
            newCol += colDelta;
        }
        return count;
    }

    /**
     * Returns a string representation of the grid, used to display the current state of the game board.
     *
     * @return A formatted string representing the grid with rows and columns.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                sb.append("| ").append(grid[row][col]).append(" ");
            }
            sb.append("|\n");
        }
        sb.append("-----------------------------\n"); // Divider for the bottom of the grid
        sb.append("  1   2   3   4   5   6   7  \n"); // Column numbers
        return sb.toString();
    }
}


