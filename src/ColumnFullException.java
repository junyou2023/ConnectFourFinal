/**
 * Thrown to indicate that a disc placement attempt has been made in a full column
 * of the Connect Four grid. This custom exception helps manage invalid moves
 * by notifying the game logic and user interface to handle the error gracefully.
 */
public class ColumnFullException extends Exception {

    /**
     * Constructs a ColumnFullException with the specified detail message.
     *
     * @param message A message that provides additional information about the exception.
     */
    public ColumnFullException(String message) {
        super(message);
    }
}

