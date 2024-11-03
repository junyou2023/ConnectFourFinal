import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Grid grid;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public GameState(Grid grid, Player player1, Player player2, Player currentPlayer) {
        this.grid = grid;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
    }

    public Grid getGrid() {
        return grid;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
