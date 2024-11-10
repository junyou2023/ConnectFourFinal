import java.io.Serializable;
import java.util.Stack;

/**
 * The Grid class represents the Connect Four game board, implemented as a 6x7 grid.
 * It manages the main game functionalities, including placing discs, tracking moves,
 * undoing moves, and checking for win or draw conditions.
 *
 * This class implements the Serializable interface, which allows instances of Grid to be
 * serialized and deserialized. Serialization is used in this context to save the state
 * of the game, enabling players to resume from where they left off.
 */
public class Grid implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures version consistency during serialization
    private static final int ROWS = 6;  // Number of rows in the grid
    private static final int COLUMNS = 7; // Number of columns in the grid
    private final char[][] grid; // 2D array representing the game board
    private final Stack<Disc> moveHistory; // Stack to keep track of moves for undo functionality

    /**
     * Constructs a Grid object, initializing a 6x7 grid with empty spaces and setting up an empty move history.
     * Each cell in the grid is represented by a space character (' ') initially.
     */
    public Grid() {
        grid = new char[ROWS][COLUMNS];
        moveHistory = new Stack<>();

        // Initialize each cell in the grid as an empty space
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    /**
     * Attempts to place a disc with the given symbol in the specified column.
     * The disc is placed in the lowest available row within the column.
     *
     * @param symbol The symbol ('X' or 'O') representing the current player.
     * @param col    The column where the player wants to drop the disc (0-indexed).
     * @return       True if the disc was successfully placed, false otherwise.
     * @throws ColumnFullException if the specified column is already full.
     */
    public boolean makeMove(char symbol, int col) throws ColumnFullException {
        // Check if the column is full and throw an exception if so
        if (checkColumnFull(col)) {
            throw new ColumnFullException("Column " + (col + 1) + " is full.");
        }

        // Start from the bottom row and place the disc in the first available empty slot
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = symbol; // Place disc in grid
                moveHistory.push(new Disc(symbol, row, col)); // Record the move in history stack
                return true; // Disc successfully placed
            }
        }
        return false; // In case of an unexpected condition (shouldn't happen)
    }

    /**
     * Undoes the last move by removing the disc from the grid and updating the move history.
     *
     * @return True if a move was successfully undone, false if there were no moves to undo.
     */
    public boolean undoMove() {
        // Check if there are moves to undo
        if (!moveHistory.isEmpty()) {
            // Retrieve and remove the last move from the history stack
            Disc lastMove = moveHistory.pop();
            // Clear the disc from the grid at the recorded row and column
            grid[lastMove.getRow()][lastMove.getCol()] = ' ';
            return true; // Successfully undone last move
        }

        // Print message and return false if no moves were made
        System.out.println("No moves to undo.");
        return false;
    }

    /**
     * Checks if a column is full by inspecting the top row of the specified column.
     *
     * @param col The column index to check (0-indexed).
     * @return    True if the column is full, false otherwise.
     */
    public boolean checkColumnFull(int col) {
        return grid[0][col] != ' ';
    }

    /**
     * Checks if the entire grid is full, indicating a draw condition if true.
     *
     * @return True if all columns in the grid are full, false otherwise.
     */
    public boolean isGridFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (!checkColumnFull(col)) {
                return false; // Found an empty slot, so grid is not full
            }
        }
        return true; // All columns are full
    }

    /**
     * Checks if the most recent move in the specified column resulted in a win by forming four consecutive symbols.
     *
     * @param col    The column where the most recent disc was placed (0-indexed).
     * @param symbol The symbol of the player who made the move.
     * @return       True if the move results in a win, false otherwise.
     */
    public boolean isWinningMove(int col, char symbol) {
        int row = findRowOfLastMove(col, symbol);
        if (row == -1) return false; // No disc found, win condition impossible

        // Check four directions: vertical, horizontal, and two diagonals
        return checkDirection(row, col, symbol, 1, 0) || // Vertical check
                checkDirection(row, col, symbol, 0, 1) || // Horizontal check
                checkDirection(row, col, symbol, 1, 1) || // Diagonal down-right
                checkDirection(row, col, symbol, 1, -1);  // Diagonal down-left
    }

    /**
     * Finds the row index of the most recent disc placed in the specified column for a given symbol.
     *
     * @param col    The column index to check (0-indexed).
     * @param symbol The symbol of the player who made the move.
     * @return       The row index of the disc, or -1 if the disc is not found.
     */
    private int findRowOfLastMove(int col, char symbol) {
        for (int row = 0; row < ROWS; row++) {
            if (grid[row][col] == symbol) {
                return row; // Return the row index of the matching disc
            }
        }
        return -1; // Disc not found in column
    }

    /**
     * Checks a specified direction from a starting point to determine if four consecutive symbols are found.
     * This method counts matching symbols in both forward and backward directions, relative to the given increments.
     *
     * @param row       The starting row index.
     * @param col       The starting column index.
     * @param symbol    The symbol to look for (player's symbol).
     * @param rowDelta  The row increment for the direction (e.g., 1 for downward).
     * @param colDelta  The column increment for the direction (e.g., 1 for right).
     * @return          True if four consecutive symbols are found in the specified direction, false otherwise.
     */
    private boolean checkDirection(int row, int col, char symbol, int rowDelta, int colDelta) {
        int count = 1; // Include the starting position in count
        count += countConsecutive(row, col, symbol, rowDelta, colDelta); // Check forward in the direction
        count += countConsecutive(row, col, symbol, -rowDelta, -colDelta); // Check backward in the opposite direction
        return count >= 4;
    }

    /**
     * Counts consecutive matching symbols in a specified direction from a given starting point.
     *
     * @param row       The starting row index.
     * @param col       The starting column index.
     * @param symbol    The symbol to look for (player's symbol).
     * @param rowDelta  The row increment for the direction.
     * @param colDelta  The column increment for the direction.
     * @return          The number of consecutive matching symbols found in the specified direction.
     */
    private int countConsecutive(int row, int col, char symbol, int rowDelta, int colDelta) {
        int count = 0;
        int newRow = row + rowDelta, newCol = col + colDelta;
        // Traverse in the specified direction, counting consecutive symbols
        while (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS && grid[newRow][newCol] == symbol) {
            count++;
            newRow += rowDelta;
            newCol += colDelta;
        }
        return count;
    }

    /**
     * Provides a formatted string representation of the grid, showing the current state of the board.
     *
     * @return A string representing the game board, including row dividers and column numbers.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                sb.append("| ").append(grid[row][col]).append(" ");
            }
            sb.append("|\n"); // End of row divider
        }
        sb.append("-----------------------------\n"); // Bottom border of the grid
        sb.append("  1   2   3   4   5   6   7  \n"); // Column numbers for user reference
        return sb.toString();
    }
}


