import java.io.*;

public class GameSaver {
    public static void saveGame(GameState gameState, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(gameState);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    public static GameState loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}



