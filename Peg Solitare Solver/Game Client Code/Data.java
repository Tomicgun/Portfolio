import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Data implements Serializable {

    int[][] board;
    ArrayList<int[]> validMoves;
    int moves = 0;
    Database db;

    public Data(int[][] board) {
        this.board = board;
        validMoves = validMoveFinder();
        db = new Database();
    }

    public Data(int[][] board, int moves,Database db) {
        this.board = board;
        validMoves = validMoveFinder();
        this.moves = moves;
        this.db = db;
    }

    public Data(int[][] board, int moves){
        this.board = board;
        validMoves = validMoveFinder();
        this.moves = moves;
        db = new Database();
        db.write(hash(),this);
    }

    public Data(){

    }

    public void updateValidMoves(){
        this.validMoves = validMoveFinder();
    }


    //find valid moves
    //more full than empty go by empty
    //more empty than full go by full
    private ArrayList<int[]> validMoveFinder(){
        if(board == null){
            return new ArrayList<>();
        }
        ArrayList<int[]> validMoves = new ArrayList<>();
        for(int i =0; i<board.length; i++){
            for(int j =0; j<board[i].length; j++){
                if(board[i][j] == 0){
                    try{
                        if(board[i+1][j] == 1 & board[i+2][j] == 1){
                            validMoves.add(new int[]{i+2,j,i+1,j,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(board[i-1][j] == 1 & board[i-2][j] == 1){
                            validMoves.add(new int[]{i-2,j,i-1,j,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(board[i][j+1] == 1 & board[i][j+2] == 1){
                            validMoves.add(new int[]{i,j+2,i,j+1,i,j});
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        //Not a valid move, but we can safely ignore it.
                    }

                    try{
                        if(board[i][j-1] == 1 & board[i][j-2] == 1){
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

    public boolean winCheck() {
        if(board == null){
            return false;
        }
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

    public Data cloneData() {
        if(board == null){
            return new Data(new int[0][0]);
        }
        int[][] clone = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, board[i].length);
        }
        return new Data(clone, moves, this.db);
    }

    public void move(int[] move) {
        board[move[0]][move[1]] = 0;
        board[move[2]][move[3]] = 0;
        board[move[4]][move[5]] = 1;
        moves++;
    }

    public String hash(){
        StringBuilder s = new StringBuilder();

        for (int[] ints : board) {
            for (int anInt : ints) {
                s.append(anInt);
            }
        }
        s.append(moves);
        return s.toString();
    }

    public Data findFinalWinner(){
        if(db.map.isEmpty()){
            return null;
        }
        Collection<Data> temp = db.map.values();
        int min = Integer.MAX_VALUE;
        Data best = new Data();
        for (Data data : temp) {
            if (data.moves < min){
                best = data;
            }
        }
        return best;
    }

    public String tostring(){
        StringBuilder s = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                if(cell == -1){
                    s.append(2).append(" ");
                }else{
                    s.append(cell).append(" ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }

    public void merge(recursiveSolver r){
        if(r == null){
            return;
        }
        db.map.putAll(r.data.db.map);
    }
}
