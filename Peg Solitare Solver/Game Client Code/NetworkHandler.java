import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.io.IOException;

public class NetworkHandler {

    public int portNumber;

    public String hostName;

    public JSONObject networkData;

    public NetworkHandler(List<recursiveSolver> tasks, String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.networkData = convertIntoJson(tasks);
    }

    public recursiveSolver run(){
        try{
            Socket socket = new Socket(hostName, portNumber);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String m = networkData.toJSONString();
            dos.writeUTF(m);
            dos.flush();
            dos.close();

            //TODO figure out why we arent getting return message
            ServerSocket clusterSolution = new ServerSocket(portNumber+1);
            Socket socket1 = clusterSolution.accept();
            DataInputStream dis = new DataInputStream(socket1.getInputStream());
            String message = dis.readUTF();
            System.out.println(message);

            recursiveSolver s = convertIntoRecursiveSolver(message);

            socket.close();
            socket1.close();
            clusterSolution.close();

            return s;
        }catch (IOException ignored){
            System.out.println("Could not connect to " + hostName + ":" + portNumber);
            return null;
        }
    }

    public JSONObject convertIntoJson(List<recursiveSolver> tasks){
        JSONObject networkData = new JSONObject();
        int i =0;
        for(recursiveSolver solver : tasks){
            JSONArray jArray = new JSONArray();
            jArray.add("board: " + solver.data.tostring());
            jArray.add("moves: " + solver.data.moves);
            networkData.put("tasks "+i,jArray);
            i++;
        }
        return networkData;
    }

    public recursiveSolver convertIntoRecursiveSolver(String js){
        if(js.equals("{}")){
            return null;
        }
        JSONParser p = new JSONParser();
        JSONObject jobj;
        try{
            jobj = (JSONObject) p.parse(js);
            String boardString = jobj.get("board").toString();
            String movesString = jobj.get("moves").toString();
            int[][] board = convertToBoard(boardString);
            int moves = Integer.parseInt(movesString);
            return new recursiveSolver(new Data(board,moves));
        }catch (ParseException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    //keep testing this method
    public int[][] convertToBoard(String map){
        String[] rows = map.split("\n");
        String [] column = rows[0].split(" ");
        int[][] board = new int[rows.length][column.length];
        for(int i=0;i<rows.length;i++){
            String [] cells = rows[i].split(" ");
            for(int j=0;j<cells.length;j++){
                //hack but it works
                if(cells[j].equals("2")){
                    board[i][j] = -1;
                }else{
                    board[i][j] = Integer.parseInt(cells[j]);
                }
            }
        }
        return board;
    }
}
