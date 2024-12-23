import java.util.ArrayList;
import java.util.Arrays;

public class GameMap {
    int[][] board;
    ArrayList<int[]> validMoves;
    int mode;
    public GameMap(int mode){
        if(mode == 1){
            this.mode = 1;
            board = new int[7][5];
            initializeBoard5x7();
        }else if(mode == 2){
            this.mode = 2;
            board = new int[7][7];
            initializeBoard7x7();
        }else{
            this.mode = -1;
            board = new int[4][4];
            initializeBoard4x4();
        }
        validMoves = validMoveFinder(this);

    }

    public GameMap(int[][] board, int mode){
        this.mode = mode;
        this.board = board;
    }

    private void initializeBoard7x7() {
        for (int[] ints : board) {
            Arrays.fill(ints, 1);
        }
        board[0][0] = -1;
        board[0][1] = -1;
        board[1][0] = -1;
        board[1][1] = -1;
        board[6][0] = -1;
        board[6][1] = -1;
        board[5][0] = -1;
        board[5][1] = -1;
        board[0][5] = -1;
        board[1][5] = -1;
        board[0][6] = -1;
        board[1][6] = -1;
        board[6][6] = -1;
        board[6][5] = -1;
        board[5][6] = -1;
        board[5][5] = -1;
        board[3][3] = 0;
    }

    private void initializeBoard5x7() {
        for (int[] ints : board) {
            Arrays.fill(ints, 1);
        }
        board[0][0] = -1;
        board[1][0] = -1;
        board[0][board[0].length-1] = -1;
        board[1][board[1].length-1] = -1;
        board[board.length-1][0] = -1;
        board[board.length-2][0] = -1;
        board[board.length-1][board[0].length-1] = -1;
        board[board.length-2][board[1].length-1] = -1;
        board[3][2] = 0;
    }

    private void initializeBoard4x4(){
        for (int[] ints : board) {
            Arrays.fill(ints, 1);
        }
        board[0][3] = -1;
        board[1][0] = -1;
        board[3][3] = -1;
        board[2][1] = 0;
    }

    public void displayMoves(){
        if(mode == 1){
            int temp = 1;
            for (int[] validMove : validMoves) {
                System.out.println(temp + ".");
                Render.renderMove(this, validMove);
                temp++;
            }
        }else if(mode == 2){
            int temp = 1;
            for (int[] validMove : validMoves) {
                System.out.println(temp + ".");
                Render.renderMove(this, validMove);
                temp++;
            }
        }else if(mode == 3){
            int temp = 1;
            for (int[] validMove : validMoves) {
                System.out.println(temp + ".");
                Render.renderMove(this, validMove);
                temp++;
            }
        }
    }

    public void move(int[] move) {
        board[move[0]][move[1]] = 0;
        board[move[2]][move[3]] = 0;
        board[move[4]][move[5]] = 1;
    }

    public void findValidMoves(){
        validMoves.clear();
        validMoves = validMoveFinder(this);
    }

    public boolean winCheck() {
        int count = 0;
        for (int[] row: board) {
            for (int num: row) {
                if (num == 1) {
                    count++;
                }
            }
        }
        return count == 1;
    }

    //find valid moves
    //more full than empty go by empty
    //more empty than full go by full
    public ArrayList<int[]> validMoveFinder(GameMap gameMap){
        ArrayList<int[]> validMoves = new ArrayList<>();
        for(int i =0; i<gameMap.board.length; i++){
            for(int j =0; j<gameMap.board[i].length; j++){
                if(gameMap.board[i][j] == 0){
                    try{
                        if(gameMap.board[i+1][j] == 1 & gameMap.board[i+2][j] == 1){
                            validMoves.add(new int[]{i+2,j,i+1,j,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(gameMap.board[i-1][j] == 1 & gameMap.board[i-2][j] == 1){
                            validMoves.add(new int[]{i-2,j,i-1,j,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(gameMap.board[i][j+1] == 1 & gameMap.board[i][j+2] == 1){
                            validMoves.add(new int[]{i,j+2,i,j+1,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(gameMap.board[i][j-1] == 1 & gameMap.board[i][j-2] == 1){
                            validMoves.add(new int[]{i,j-2,i,j-1,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }
                }
            }
        }
        return validMoves;
    }
}
