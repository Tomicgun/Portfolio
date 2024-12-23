import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class recursiveSolver extends RecursiveAction {

    Data data;
    int mode;
    boolean network = false;
    public recursiveSolver(Data data) {
        this.data = data;
    }

    public recursiveSolver(int[][] board, int mode) {
        data = new Data(board);
        this.mode = mode;
    }

    //Were the actual magic happens
    @Override
    protected void compute() {
        data.updateValidMoves();
        if(data.validMoves.isEmpty() && data.winCheck()) {
            data.db.write(data.hash(),data);
        }
        ForkJoinTask.invokeAll(createSubtask());
    }

    private List<recursiveSolver> createSubtask() {
        List<recursiveSolver> subtaks = new ArrayList<>();
        for(int[] move : data.validMoves){
            Data localData = data.cloneData();
            localData.move(move);
            subtaks.add(new recursiveSolver(localData));
        }
        return subtaks;
    }

    public Data solve() {
        System.out.println("Solving...");
        ForkJoinPool pool = new ForkJoinPool();
        List<recursiveSolver> subtasks = createSubtask();
        if(network){
            List<recursiveSolver> subtasks1 = subtasks.subList(0, subtasks.size()/2);
            List<recursiveSolver> subtasks2 = subtasks.subList(subtasks.size()/2, subtasks.size());
            //TODO Fill in Port number and host name
            NetworkHandler nh = new NetworkHandler(subtasks2,"129.3.20.26",12500);
            recursiveSolver other_half = nh.run();
            for(recursiveSolver subtask : subtasks1){
                pool.invoke(subtask);
            }
            data.merge(other_half);
        }else{
            for(recursiveSolver subtask : subtasks){
                pool.invoke(subtask);
            }
        }
        Data winnerBoard = data.findFinalWinner();
        pool.close();
        return winnerBoard;
    }
}
