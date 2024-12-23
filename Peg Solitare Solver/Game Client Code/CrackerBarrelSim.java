import java.util.Scanner;
public class CrackerBarrelSim {
    static int mode;
    static GameMap board;
    static Render r = new Render();
    static int move = 0;
    public static void gameLoop() {
        Scanner in = new Scanner(System.in);
        if(mode == 1){
            board = new GameMap(1);
        }else if(mode == 2){
            board = new GameMap(2);
        }else{
            board = new GameMap(-1);
            recursiveSolver solver = new recursiveSolver(board.board,-1);
            Data winner = solver.solve();
            if(winner == null){
                System.out.println("No winner found");
            }else{
                System.out.println("Here is the solution");
                r.render(new GameMap(winner.board,-1));
                System.out.println("This solution was found in " + winner.moves);
            }
            System.exit(1);
        }
        while(true){
            r.render(board);
            System.out.println("If you ever get stuck just enter -1 to have the game solved for you");
            System.out.println("Please select one of the bellow moves to make:");
            board.displayMoves();
            int temp = in.nextInt();
            if(temp == -1){
                recursiveSolver solver = new recursiveSolver(board.board,mode);
                Data winner = solver.solve();
                if(winner == null){
                    System.out.println("No winner found");
                    break;
                }else{
                    System.out.println("Here is the solution");
                    r.render(new GameMap(winner.board,mode));
                    System.out.println("Solved in " + winner.moves);
                    break;
                }
            }
            if(temp <= board.validMoves.size()){
                board.move(board.validMoves.get(temp-1));
                move++;
            }else{
                System.out.println("Invalid move");
            }
            board.findValidMoves();

            if(board.validMoves.isEmpty()){
                System.out.println("Oh no, there are no more valid moves! Game Over");
                break;
            }
            if(board.winCheck()){
                System.out.println("Good Job you won in " + move + " moves!");
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Cracker Barrel");
        System.out.println("We will have your food out shortly, please feel free to play a complimentary\n" +
                "game of Peg solitaire, and if it gets too hard feel free to use the solver");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Please Select one of the 2 board layouts");
        System.out.println("1. Cross 5x7");
        System.out.println("2. Cross 7x7");
        int choice = sc.nextInt();
        switch (choice) {
            case 1: mode = 1; System.out.println("5x7 selected"); break;
            case 2: mode = 2; System.out.println("7x7 selected"); break;
            case -1: mode = -1; System.out.println("test selected"); break;
        }
        System.out.println("Enjoy The game!");
        gameLoop();
        System.out.println("Here is your food, Enjoy and Thanks for coming to Cracker Barrel!");
    }
}
