import java.io.*;
import java.util.Stack;

public class Grid implements Serializable {  // 实现 Serializable 接口
    private static final long serialVersionUID = 1L; // 推荐使用 serialVersionUID 确保序列化兼容
    private char[][] grid;
    private Stack<Disc> moveHistory;  // 用于存储每一步的操作

    public Grid() {
        grid = new char[6][7];
        moveHistory = new Stack<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    // 在指定列放置棋子
    public boolean makeMove(char symbol, int col) {
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = symbol;
                moveHistory.push(new Disc(symbol, row, col));  // 保存操作
                return true;
            }
        }
        System.out.println("Column " + (col + 1) + " is full. Try another column.");
        return false;
    }

    // 撤销上一步操作
    public boolean undoMove() {
        if (!moveHistory.isEmpty()) {
            Disc lastMove = moveHistory.pop();
            grid[lastMove.getRow()][lastMove.getCol()] = ' ';
            return true;
        } else {
            System.out.println("No moves to undo.");
            return false;
        }
    }

    // 检查列是否已满
    public boolean checkColumnFull(int col) throws ColumnFullException {
        if (grid[0][col] != ' ') {  // 如果顶层有棋子，列已满
            throw new ColumnFullException("Column " + (col + 1) + " is full.");
        }
        return false;
    }

    // 检查当前移动是否满足获胜条件（四个相同符号连成一线）
    public boolean isWinningMove(int col, char symbol) {
        int row = -1;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][col] == symbol) {
                row = i;
                break;
            }
        }

        if (row == -1) {
            throw new IllegalStateException("No piece has been placed in column " + (col + 1));
        }

        return (checkDirection(row, col, symbol, 1, 0)    // 垂直
                || checkDirection(row, col, symbol, 0, 1) // 水平
                || checkDirection(row, col, symbol, 1, 1) // 主对角线
                || checkDirection(row, col, symbol, 1, -1)); // 副对角线
    }

    // 检查指定方向是否有四个相同符号连成一线
    private boolean checkDirection(int row, int col, char symbol, int rowDelta, int colDelta) {
        int count = 1;

        // 向一个方向检查
        for (int i = 1; i < 4; i++) {
            int newRow = row + i * rowDelta;
            int newCol = col + i * colDelta;
            if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length || grid[newRow][newCol] != symbol) {
                break;
            }
            count++;
        }

        // 向反方向检查
        for (int i = 1; i < 4; i++) {
            int newRow = row - i * rowDelta;
            int newCol = col - i * colDelta;
            if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length || grid[newRow][newCol] != symbol) {
                break;
            }
            count++;
        }

        return count >= 4;  // 四个相同符号连成一线则返回 true
    }

    // 将当前网格状态保存到文件
    public void saveGame(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);  // 保存当前 Grid 对象
            System.out.println("Game saved successfully to " + filename);
        }
    }

    // 从文件加载网格状态
    public static Grid loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Grid) ois.readObject();  // 加载 Grid 对象
        }
    }

    // 可视化网格状态
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                sb.append("| ").append(grid[row][col]).append(" ");
            }
            sb.append("|\n");
        }
        sb.append("-----------------------------\n");
        sb.append("  1   2   3   4   5   6   7  \n"); // 列编号
        return sb.toString();
    }
}
