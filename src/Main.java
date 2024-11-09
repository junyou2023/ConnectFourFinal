import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create scanner for user input and display welcome menu
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Welcome to Connect Four!");
            System.out.println("1. Start New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit");

            int choice = 0;

            // Loop until a valid choice (1, 2, or 3) is entered
            while (true) {
                System.out.print("Please enter a choice (1, 2, or 3): ");

                if (sc.hasNextInt()) {   // Check if the input is an integer
                    choice = sc.nextInt();
                    sc.nextLine();       // Consume the newline character
                    if (choice >= 1 && choice <= 3) {
                        break;           // Exit loop if choice is valid
                    } else {
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next();           // Consume the invalid input
                }
            }

            // Exit the game if the user chooses option 3
            if (choice == 3) {
                System.out.println("Exiting the game. Goodbye!");
                return;
            }

            // Initialize game data based on user choice (new game or load saved game)
            GameData gameData = initializeGame(choice, sc);
            Grid grid = gameData.grid;
            Player currentPlayer = gameData.currentPlayer;
            Player player1 = gameData.player1;
            Player player2 = gameData.player2;

            // Display initial empty grid
            System.out.println(grid);
            boolean gameInProgress = true;

            // Main game loop: runs while the game is in progress
            while (gameInProgress) {
                gameInProgress = takeTurn(currentPlayer, grid, player1, player2, sc);
                // Switch players after each turn
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }

            // End game message
            System.out.println("Thank you for playing!");
        }
    }

    /**
     * Inner class to encapsulate core game components for easy management.
     */
    private static class GameData {
        Grid grid;
        Player player1;
        Player player2;
        Player currentPlayer;

        GameData(Grid grid, Player player1, Player player2, Player currentPlayer) {
            this.grid = grid;
            this.player1 = player1;
            this.player2 = player2;
            this.currentPlayer = currentPlayer;
        }
    }

    /**
     * Initializes game data by either starting a new game or loading a saved game.
     *
     * @param choice The user's menu choice (1 for new game, 2 for load game).
     * @param sc     The Scanner object for user input.
     * @return       A GameData object containing initialized game components.
     */
    private static GameData initializeGame(int choice, Scanner sc) {
        if (choice == 2) {
            // Attempt to load game state from file
            GameState loadedState = GameSaver.loadGame("saved_game.dat");
            if (loadedState != null) {
                System.out.println("Game loaded successfully.");
                return new GameData(
                        loadedState.getGrid(),
                        loadedState.getPlayer1(),
                        loadedState.getPlayer2(),
                        loadedState.getCurrentPlayer()
                );
            } else {
                System.out.println("Failed to load game. Starting a new game instead...");
            }
        }
        // If loading fails or user chooses a new game, create fresh game data
        return startNewGame(sc);
    }

    /**
     * Starts a new game by creating players and initializing a fresh grid.
     *
     * @param sc The Scanner object for user input.
     * @return   A GameData object with initialized players and grid.
     */
    private static GameData startNewGame(Scanner sc) {
        Player player1 = createPlayer(sc, 1); // Create Player 1
        Player player2 = createPlayer(sc, 2); // Create Player 2
        Grid grid = new Grid(); // Initialize a new grid
        return new GameData(grid, player1, player2, player1); // Player 1 starts first
    }

    /**
     * Prompts the user for player details and creates a Player object.
     *
     * @param sc           The Scanner object for user input.
     * @param playerNumber The player number (1 or 2).
     * @return             A new Player object with specified name and symbol.
     */
    private static Player createPlayer(Scanner sc, int playerNumber) {
        System.out.print("Enter name for Player " + playerNumber + ": ");
        String name = sc.nextLine();
        char symbol = (playerNumber == 1) ? 'X' : 'O';
        return new Player(name, symbol);
    }

    /**
     * Manages a single player's turn, including move validation and special commands.
     *
     * @param currentPlayer The player currently taking their turn.
     * @param grid          The game grid.
     * @param player1       Player 1 (needed for save operations).
     * @param player2       Player 2 (needed for save operations).
     * @param sc            The Scanner object for user input.
     * @return              True if the game continues, false if it ends (win or exit).
     */
    private static boolean takeTurn(Player currentPlayer, Grid grid, Player player1, Player player2, Scanner sc) {
        while (true) {
            try {
                // Prompt for column input or special command (-1, -2, -3)
                int col = promptPlayerMove(currentPlayer, sc);
                if (col == -3) return exitGame(); // Handle exit command
                if (col == -2) { // Save game command
                    saveGame(grid, player1, player2, currentPlayer);
                    continue; // End current iteration, maintaining the current player's turn
                }
                if (col == -1) { // Undo last move command
                    if (undoLastMove(grid)) return true;
                    continue;
                }

                // Adjust input to 0-based indexing for the grid
                col -= 1;
                if (grid.checkColumnFull(col)) {
                    System.out.println("The selected column is full. Please choose another column.");
                    continue;
                }

                // Place disc for the current player and display the updated grid
                currentPlayer.takeTurn(grid, col);
                System.out.println(grid);

                // Check for win condition
                if (grid.isWinningMove(col, currentPlayer.getSymbol())) {
                    System.out.println("Player " + currentPlayer.getName() + " wins!");
                    return false; // End game on win
                }

                // Check for a draw condition (full grid with no winner)
                if (grid.isGridFull()) {
                    System.out.println("The grid is full! The game is a draw.");
                    return false; // End game on draw
                }

                return true; // Continue game if no win

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid column number.");
                sc.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Prompts the player to enter a column number or special command.
     *
     * @param currentPlayer The player currently taking their turn.
     * @param sc            The Scanner object for user input.
     * @return              The column number or special command.
     */
    private static int promptPlayerMove(Player currentPlayer, Scanner sc) {
        System.out.println("Player " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + "), enter column (1-7) or -1 to undo, -2 to save, -3 to exit:");
        return sc.nextInt();
    }

    /**
     * Saves the game state to a file for future loading.
     *
     * @param grid          The game grid.
     * @param player1       Player 1 data.
     * @param player2       Player 2 data.
     * @param currentPlayer The player whose turn it is.
     */
    private static void saveGame(Grid grid, Player player1, Player player2, Player currentPlayer) {
        GameSaver.saveGame(new GameState(grid, player1, player2, currentPlayer), "saved_game.dat");
        System.out.println("Game saved.");
    }

    /**
     * Undoes the last move by removing the most recent disc from the grid.
     *
     * @param grid The game grid.
     * @return     True if undo was successful, false otherwise.
     */
    private static boolean undoLastMove(Grid grid) {
        if (grid.undoMove()) {
            System.out.println("Last move undone.");
            System.out.println(grid);
            return true;
        }
        System.out.println("No moves to undo.");
        return false;
    }

    /**
     * Exits the game by displaying an exit message and returning false to end the game loop.
     *
     * @return Always returns false to indicate the game should end.
     */
    private static boolean exitGame() {
        System.out.println("Exiting the game...");
        return false;
    }
}









