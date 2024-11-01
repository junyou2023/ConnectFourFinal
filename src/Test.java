import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grid grid = null;

        System.out.println("Welcome to Connect Four!");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");

        int choice = sc.nextInt();
        sc.nextLine(); // 清除换行符

        Player player1, player2;

        if (choice == 2) {
            // 尝试加载游戏
            try {
                grid = Grid.loadGame("saved_game.dat");
                player1 = grid.getPlayer1();
                player2 = grid.getPlayer2();
                System.out.println("Game loaded successfully.");
            } catch (Exception e) {
                System.out.println("Failed to load game: " + e.getMessage());
                System.out.print("Starting a new game instead...\n");

                // 如果加载失败，则重新输入玩家姓名并开始新游戏
                player1 = createPlayer(sc, 1);
                player2 = createPlayer(sc, 2);
                grid = new Grid(player1, player2);
            }
        } else {
            // 新游戏模式下输入玩家姓名
            player1 = createPlayer(sc, 1);
            player2 = createPlayer(sc, 2);
            grid = new Grid(player1, player2);
        }

        // 游戏主循环
        System.out.println(grid);
        boolean gameInProgress = true;
        while (gameInProgress) {
            gameInProgress = !takeTurn(player1, grid, sc);
            if (!gameInProgress) break;
            gameInProgress = !takeTurn(player2, grid, sc);
        }

        sc.close();
    }

    // 辅助方法：创建玩家
    private static Player createPlayer(Scanner sc, int playerNumber) {
        System.out.print("Enter Player " + playerNumber + "'s name: ");
        String name = sc.nextLine();
        char symbol = (playerNumber == 1) ? 'X' : 'O';
        return new Player(name, symbol);
    }

    // 辅助方法：处理单个玩家的回合
    private static boolean takeTurn(Player player, Grid grid, Scanner sc) {
        while (true) {
            try {
                System.out.println("Player " + player.getName() + " (" + player.getSymbol() + "), enter column (1-7) or -1 to undo, -2 to save:");
                int col = sc.nextInt();

                if (col == -2) {  // 保存游戏
                    grid.saveGame("saved_game.dat");
                    System.out.println("Game saved.");
                    continue;
                } else if (col == -1) {  // 撤销操作
                    grid.undoMove();
                    System.out.println("Last move undone.");
                    System.out.println(grid);
                    continue;
                }

                col -= 1;  // 转换为0索引

                // 检查列是否已满
                grid.checkColumnFull(col);

                // 放置棋子
                player.takeTurn(grid, col);
                System.out.println(grid);

                // 检查是否获胜
                if (grid.isWinningMove(col, player.getSymbol())) {
                    System.out.println("Player " + player.getName() + " wins!");
                    return true;  // 游戏结束
                }

                return false;  // 正常结束回合
            } catch (ColumnFullException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid column number.");
                sc.nextLine();  // 清除无效输入
            }
        }
    }
}




