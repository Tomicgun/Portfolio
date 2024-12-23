import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class recursiveSolver extends RecursiveAction {
    Data data;

    public recursiveSolver(Data data) {
        this.data = data;
    }

    public recursiveSolver() {}

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

    public Data solve(ArrayList<recursiveSolver> receivedSubtasks) {
        System.out.println("Solving...");
        ForkJoinPool pool = new ForkJoinPool();
        for(recursiveSolver subtask : receivedSubtasks){
            pool.invoke(subtask);
        }
        Data winnerBoard = data.findFinalWinner();
        pool.shutdown();
        return winnerBoard;
    }
}
