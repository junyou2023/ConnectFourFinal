import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String Name;
    private char Symbol;

    public Player(String name, char symbol) {
        this.Name = name;
        this.Symbol = symbol;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public char getSymbol() {
        return Symbol;
    }

    public void setSymbol(char symbol) {
       this.Symbol = symbol;
    }

    public void takeTurn(Grid grid, int col){;
        grid.makeMove(this.Symbol,col);
    };
}
