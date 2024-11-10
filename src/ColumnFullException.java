/**
 * Thrown to indicate that a disc placement attempt has been made in a full column
 * of the Connect Four grid. This custom exception helps manage invalid moves
 * by notifying the game logic and user interface to handle the error.
 */
public class ColumnFullException extends Exception {
    public ColumnFullException(String message) {
        super(message);
    }
}

