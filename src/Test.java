import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grid grid = null;

        // 主菜单
        System.out.println("Welcome to Connect Four!");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");

        int choice = sc.nextInt();
        sc.nextLine();  // 清除换行符
        if (choice == 1) {
            grid = new Grid();
        } else if (choice == 2) {
            try {
                grid = Grid.loadGame("saved_game.dat");
                System.out.println("Game loaded successfully.");
            } catch (Exception e) {
                System.out.println("Failed to load game: " + e.getMessage());
                grid = new Grid();
            }
        }

        System.out.print("Enter Player 1's name: ");
        String player1Name = sc.nextLine();
        System.out.print("Enter Player 2's name: ");
        String player2Name = sc.nextLine();
        Player player1 = new Player(player1Name, 'X');
        Player player2 = new Player(player2Name, 'O');
        boolean gameWon = false;

        while (!gameWon) {
            // 玩家 1 的回合
            System.out.println("Player 1's turn:");
            gameWon = playTurn(player1, grid, sc);
            if (gameWon) break;

            // 玩家 2 的回合
            System.out.println("Player 2's turn:");
            gameWon = playTurn(player2, grid, sc);
        }
        sc.close();
    }

    private static boolean playTurn(Player player, Grid grid, Scanner sc) {
        while (true) {
            try {
                System.out.println("Player " + player.getName() + " (" + player.getSymbol() + "), enter column (1-7) or -1 to undo, -2 to save:");
                int col = sc.nextInt();

                if (col == -2) {  // 保存游戏
                    grid.saveGame("saved_game.dat");
                    System.out.println("Game saved.");
                    continue;  // 继续当前回合
                } else if (col == -1) {  // 撤销操作
                    grid.undoMove();
                    System.out.println("Last move undone.");
                    System.out.println(grid);
                    continue;  // 继续当前回合
                }

                col -= 1;  // 调整列索引到0开始

                // 检查列是否已满
                grid.checkColumnFull(col);

                // 放置棋子
                player.takeTurn(grid, col);
                System.out.println(grid);

                // 检查是否获胜
                if (grid.isWinningMove(col, player.getSymbol())) {
                    System.out.println("Player " + player.getName() + " wins!");
                    return true;
                }

                return false;  // 正常结束回合
            } catch (ColumnFullException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid column number.");
                sc.next();  // 清除无效输入
            }
        }
    }
}


