import java.io.*;

/**
 * The GameSaver class provides functionality to save and load the game state
 * to and from a file. This allows players to pause and resume their game
 * sessions by serializing and deserializing the GameState object.
 */
public class GameSaver {

    /**
     * Saves the current game state to a specified file.
     *
     * @param gameState The GameState object containing all data necessary to restore the game.
     * @param fileName  The name of the file to which the game state will be saved.
     */
    public static void saveGame(GameState gameState, String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState); // Serialize GameState to file
            System.out.println("Game saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Loads a game state from a specified file.
     *
     * @param fileName The name of the file from which the game state will be loaded.
     * @return The GameState object loaded from the file, or null if loading fails.
     */
    public static GameState loadGame(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (GameState) in.readObject(); // Deserialize GameState from file
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}




