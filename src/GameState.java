import java.io.Serializable;

/**
 * The GameState class stores all necessary data to capture the current state of a Connect Four game.
 * This includes the grid, players, and the player whose turn it is. The class implements Serializable
 * to allow game states to be saved and loaded.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures compatibility during serialization

    private final Grid grid;            // The current game board state
    private final Player player1;       // Player 1 information
    private final Player player2;       // Player 2 information
    private final Player currentPlayer; // The player who will take the next turn

    /**
     * Constructs a GameState object to capture the current game data.
     *
     * @param grid           The current grid state of the game.
     * @param player1        The first player in the game.
     * @param player2        The second player in the game.
     * @param currentPlayer  The player whose turn it is to play.
     */
    public GameState(Grid grid, Player player1, Player player2, Player currentPlayer) {
        this.grid = grid;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns the grid state of the game.
     *
     * @return The current game grid.
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Returns the first player in the game.
     *
     * @return Player 1 object.
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Returns the second player in the game.
     *
     * @return Player 2 object.
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return The current player who will make the next move.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

