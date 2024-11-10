import java.io.Serializable;
import java.util.Scanner;

/**
 * The Player class represents a participant in the Connect Four game.
 * Each player has a unique name and a symbol ('X' or 'O') which they use
 * to mark their moves on the game grid. This class also contains the logic
 * for managing a player's turn, including handling inputs for moves and
 * special commands (such as undo, save, and exit).
 *
 * This class implements Serializable to support saving and loading of the
 * player state as part of the game, enabling a player to resume from a saved game.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L; // Ensures version consistency during serialization

    private String name;  // The name of the player
    private char symbol;  // The player's symbol ('X' or 'O') used on the grid

    /**
     * Constructs a new Player with the specified name and symbol.
     *
     * @param name   The player's name as a String.
     * @param symbol The player's symbol, typically 'X' or 'O'.
     */
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public char getSymbol() {
        return symbol;
    }


    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Manages a player’s turn. This method prompts the player to enter a column
     * to place their disc in or to enter a special command (-1 to undo, -2 to save, -3 to exit).
     * Depending on the input, it handles the respective action:
     * - Places a disc in the specified column
     * - Undoes the last move if requested
     * - Saves the game state if requested
     * - Exits the game if requested
     *
     * @param grid    The game grid, which represents the Connect Four board.
     * @param player1 The first player in the game, used for saving the game state.
     * @param player2 The second player in the game, used for saving the game state.
     * @param sc      Scanner object used to capture the player’s input.
     * @return        True if the game should continue, or false if it should end (due to win, draw, or exit).
     */
    public boolean takeTurn(Grid grid, Player player1, Player player2, Scanner sc) {
        while (true) {
            try {
                // Prompt the player to make a move or enter a special command
                System.out.println("Player " + name + " (" + symbol + "), enter column (1-7) or -1 to undo, -2 to save, -3 to exit:");
                int col = sc.nextInt();

                // Exit the game if -3 is entered
                if (col == -3) {
                    System.out.println("Exiting the game...");
                    return false;  // Signal to end the game loop
                }
                // Save the current game state if -2 is entered
                else if (col == -2) {
                    GameSaver.saveGame(new GameState(grid, player1, player2, this), "saved_game.dat");
                    System.out.println("Game saved. You may continue your turn.");
                    continue;  // Remain in the loop for the current player's turn
                }
                // Undo the last move if -1 is entered
                else if (col == -1) {
                    if (grid.undoMove()) {
                        System.out.println("Last move undone.");
                        System.out.println(grid);  // Display updated grid
                        return true;  // Return to main loop, keeping same player turn
                    } else {
                        System.out.println("No moves to undo.");
                    }
                    continue;
                }

                // Convert to zero-based index for internal grid handling
                col -= 1;
                if (grid.checkColumnFull(col)) {  // Check if the selected column is full
                    System.out.println("The selected column is full or invalid. Please choose another column.");
                    continue;  // Prompt again if column is full
                }

                // Place the player's disc in the chosen column and display the updated grid
                grid.makeMove(symbol, col);
                System.out.println(grid);

                // Check if the move results in a win for the player
                if (grid.isWinningMove(col, symbol)) {
                    System.out.println("Player " + name + " wins!");
                    return false;  // End game if the player wins
                }

                // Check for a draw condition if the grid is full
                if (grid.isGridFull()) {
                    System.out.println("The grid is full! The game is a draw.");
                    return false;  // End game if there is a draw
                }

                return true;  // Return to main loop to switch players if no win or draw
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid column number.");
                sc.nextLine();  // Clear invalid input from scanner buffer to prevent infinite loop
            }
        }
    }
}




