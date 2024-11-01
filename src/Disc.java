import java.io.Serializable;

public class Disc implements Serializable {
    private static final long serialVersionUID = 1L;
    private final char symbol;
    private final int row;
    private final int col;

    public Disc(char symbol, int row, int col) {
        this.symbol = symbol;
        this.row = row;
        this.col = col;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}


