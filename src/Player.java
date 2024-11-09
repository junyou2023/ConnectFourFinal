import java.io.Serializable;

/**
 * The Player class represents a player in the Connect Four game. Each player has a
 * unique name and a symbol ('X' or 'O') that they use to mark their moves on the grid.
 * This class implements Serializable to allow player details to be saved and loaded
 * as part of the game state, which enabling game save functionality.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures compatibility during serialization

    private String name;  // Name of the player
    private char symbol;  // Symbol representing the player ('X' or 'O')

    /**
     * Constructs a Player object with the specified name and symbol.
     *
     * @param name   The name of the player.
     * @param symbol The symbol assigned to the player ('X' or 'O').
     */
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * Returns the name of the player.
     *
     * @return The name of the player as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The new name of the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the symbol associated with the player.
     *
     * @return The symbol of the player ('X' or 'O').
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the player.
     *
     * @param symbol The new symbol for the player ('X' or 'O').
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void takeTurn(Grid grid, int col) throws ColumnFullException {
        grid.makeMove(symbol, col);
    }
}



