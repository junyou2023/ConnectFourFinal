import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grid grid;
        Player player1, player2, currentPlayer;

        System.out.println("Welcome to Connect Four!");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice == 2) {
            // Attempt to load a saved game state
            GameState loadedState = GameSaver.loadGame("saved_game.dat");
            if (loadedState != null) {
                grid = loadedState.getGrid();
                player1 = loadedState.getPlayer1();
                player2 = loadedState.getPlayer2();
                currentPlayer = loadedState.getCurrentPlayer();
                System.out.println("Game loaded successfully.");
            } else {
                // Failed to load, so initialize a new game
                System.out.println("Failed to load game. Starting a new game instead...");
                player1 = createPlayer(sc, 1);
                player2 = createPlayer(sc, 2);
                grid = new Grid();
                currentPlayer = player1;
            }
        } else {
            // Start a new game
            player1 = createPlayer(sc, 1);
            player2 = createPlayer(sc, 2);
            grid = new Grid();
            currentPlayer = player1;
        }

        // Display initial grid and begin game loop
        System.out.println(grid);
        boolean gameInProgress = true;

        while (gameInProgress) {
            gameInProgress = takeTurn(currentPlayer, grid, player1, player2, sc);
            // Alternate players after each turn
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
        }

        sc.close();
    }

    private static Player createPlayer(Scanner sc, int playerNumber) {
        System.out.print("Enter name for Player " + playerNumber + ": ");
        String name = sc.nextLine();
        char symbol = (playerNumber == 1) ? 'X' : 'O';
        return new Player(name, symbol);
    }

    private static boolean takeTurn(Player currentPlayer, Grid grid, Player player1, Player player2, Scanner sc) {
        while (true) {
            try {
                System.out.println("Player " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + "), enter column (1-7) or -1 to undo, or -2 to save:");
                int col = sc.nextInt();

                if (col == -2) {
                    // Save the game state
                    GameSaver.saveGame(new GameState(grid, player1, player2, currentPlayer), "saved_game.dat");
                    System.out.println("Game saved.");
                } else if (col == -1) {
                    // Undo last move
                    if (grid.undoMove()) {
                        System.out.println("Last move undone.");
                        System.out.println(grid);
                    } else {
                        System.out.println("No moves to undo.");
                    }
                }

                col -= 1; // Adjust to 0-based indexing


                // Ensure column is not full
                grid.checkColumnFull(col);

                // Make the move
                grid.makeMove(currentPlayer.getSymbol(), col);

                System.out.println(grid);

                // Check for a win condition after the move
                if (grid.isWinningMove(col, currentPlayer.getSymbol())) {
                    System.out.println("Player " + currentPlayer.getName() + " wins!");
                    return false; // End the game
                }

                return true; // Continue the game if no one has won
            } catch (ColumnFullException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid column number.");
                sc.nextLine(); // Clear the invalid input
            }
        }
    }
}





