import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Display the main menu with options for starting a new game, loading a saved game, or exiting
            System.out.println("Welcome to Connect Four!");
            System.out.println("1. Start New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit");

            int choice = 0;

            // Prompt user for a valid menu choice (1, 2, or 3), ensuring input is an integer and within range
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

            // If user chooses to exit, terminate the program
            if (choice == 3) {
                System.out.println("Exiting the game. Goodbye!");
                return;
            }

            // Initialize the game data based on the user's choice (new game or load saved game)
            GameData gameData = initializeGame(choice, sc);
            Grid grid = gameData.grid;
            Player currentPlayer = gameData.currentPlayer;
            Player player1 = gameData.player1;
            Player player2 = gameData.player2;

            // Display the initial empty game grid
            System.out.println(grid);
            boolean gameInProgress = true;

            // Main game loop: alternates between players until the game ends (win, draw, or exit)
            while (gameInProgress) {
                // Execute the current player's turn, passing the game grid and player details
                gameInProgress = currentPlayer.takeTurn(grid, player1, player2, sc);

                // Alternate players for the next turn by switching currentPlayer
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }

            // Display a message when the game ends
            System.out.println("Thank you for playing!");
        }
    }

    /**
     * Nested class to encapsulate core game data (grid, players) for easier management.
     * This class groups together the game grid, the two players, and the current player,
     * simplifying the passing of these components to other methods.
     */
    private static class GameData {
        Grid grid;           // The Connect Four grid (game board)
        Player player1;      // Player 1 instance
        Player player2;      // Player 2 instance
        Player currentPlayer; // The player who is currently taking their turn

        /**
         * Constructs a GameData object with the provided grid and player information.
         *
         * @param grid          The game grid.
         * @param player1       Player 1 instance.
         * @param player2       Player 2 instance.
         * @param currentPlayer The player currently taking their turn.
         */
        GameData(Grid grid, Player player1, Player player2, Player currentPlayer) {
            this.grid = grid;
            this.player1 = player1;
            this.player2 = player2;
            this.currentPlayer = currentPlayer;
        }
    }

    /**
     * Initializes the game by either loading a saved game or starting a new game,
     * depending on the user's choice.
     *
     * @param choice The user's menu choice (1 for new game, 2 for load game).
     * @param sc     Scanner object to capture user input.
     * @return       A GameData object with initialized game components.
     */
    private static GameData initializeGame(int choice, Scanner sc) {
        if (choice == 2) {
            // Attempt to load a saved game state from a file
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
        // Start a new game if loading fails or user selects "Start New Game"
        return startNewGame(sc);
    }

    /**
     * Starts a new game by creating player instances and initializing an empty grid.
     *
     * @param sc The Scanner object for capturing user input.
     * @return   A GameData object with new game components, setting Player 1 to start.
     */
    private static GameData startNewGame(Scanner sc) {
        Player player1 = createPlayer(sc, 1); // Initialize Player 1 with a name and symbol
        Player player2 = createPlayer(sc, 2); // Initialize Player 2 with a name and symbol
        Grid grid = new Grid();               // Create a new empty game grid
        return new GameData(grid, player1, player2, player1); // Player 1 starts first
    }

    /**
     * Prompts the user to enter player details (name and symbol), creating a new Player instance.
     *
     * @param sc           Scanner object for reading user input.
     * @param playerNumber The player's number (1 or 2), used for prompting and symbol assignment.
     * @return             A new Player object initialized with the specified name and symbol.
     */
    private static Player createPlayer(Scanner sc, int playerNumber) {
        System.out.print("Enter name for Player " + playerNumber + ": ");
        String name = sc.nextLine();
        char symbol = (playerNumber == 1) ? 'X' : 'O'; // Assign 'X' to Player 1 and 'O' to Player 2
        return new Player(name, symbol);
    }
}












