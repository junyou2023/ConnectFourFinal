import java.io.Serializable;

/**
 * The Disc class represents a game piece in the Connect Four game. Each disc
 * is associated with a specific player symbol ('X' or 'O') and a position in the grid.
 * This class implements Serializable to allow individual disc placements to be
 * recorded and restored, which is useful for undo functionality and saving game state.
 */
public class Disc implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures version consistency during serialization

    private final char symbol; // The symbol of the player who placed this disc ('X' or 'O')
    private final int row;     // The row position of the disc in the grid (0-indexed)
    private final int col;     // The column position of the disc in the grid (0-indexed)

    /**
     * Constructs a Disc object with the specified symbol, row, and column.
     *
     * @param symbol The symbol representing the player who placed this disc.
     * @param row    The row position of the disc in the grid.
     * @param col    The column position of the disc in the grid.
     */
    public Disc(char symbol, int row, int col) {
        this.symbol = symbol;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the symbol of the player who placed this disc.
     *
     * @return The symbol ('X' or 'O') representing the player.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the row position of the disc in the grid.
     *
     * @return The row index (0-based) of the disc's position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column position of the disc in the grid.
     *
     * @return The column index (0-based) of the disc's position.
     */
    public int getCol() {
        return col;
    }
}



