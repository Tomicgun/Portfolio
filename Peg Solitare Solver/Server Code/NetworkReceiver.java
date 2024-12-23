import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NetworkReceiver {

    public int portNumber;

    public String hostName;

    public NetworkReceiver(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() {
        Time start = new Time(System.currentTimeMillis());
        Time end;
        try {
            do {
                ServerSocket serverSocket = new ServerSocket(portNumber);
                System.out.println("Listening on port " + portNumber);
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + hostName);

                DataInputStream dis = new DataInputStream(socket.getInputStream());

                String message = dis.readUTF();
                System.out.println(message);

                ArrayList<recursiveSolver> s = convertIntoRecursiveSolver(message);
                Data d = runSolve(s);

                JSONObject returnValue = convertIntoJson(d);

                Socket sendbackSocket = new Socket("129.3.20.1", portNumber+1);
                DataOutputStream dos = new DataOutputStream(sendbackSocket.getOutputStream());
                dos.writeUTF(returnValue.toJSONString());
                dos.flush();
                dos.close();

                socket.close();
                sendbackSocket.close();
                serverSocket.close();

                //greater than one hour break and end
                end = new Time(System.currentTimeMillis());
            } while (end.getTime() - start.getTime() <= 3600000);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Data runSolve(ArrayList<recursiveSolver> s) {
        return s.get(0).solve(s);
    }

    public JSONObject convertIntoJson(Data d){
        if(d == null){
            JSONObject networkData = new JSONObject();
            return networkData;
        }
        JSONObject networkData = new JSONObject();
        networkData.put("board",d.tostring());
        networkData.put("moves",d.moves);
        return networkData;
    }

    public int[][] convertToBoard(String map){
        String [] cleanup = map.split(":");
        map = cleanup[1].trim();
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

    public ArrayList<recursiveSolver> convertIntoRecursiveSolver(String js) {
        JSONParser p = new JSONParser();
        JSONObject jobj;
        ArrayList<recursiveSolver> tasks = new ArrayList<>();
        try{
            jobj = (JSONObject) p.parse(js);
            for(int i = 0; i< jobj.size(); i++){
            JSONArray jarr = (JSONArray) jobj.get("tasks "+i);
            String boardString = jarr.get(0).toString();
            String movesString = jarr.get(1).toString();
            int[][] board = convertToBoard(boardString);
            int moves = Integer.parseInt(movesString.substring(movesString.length()-1));
            tasks.add(new recursiveSolver(new Data(board,moves,new Database())));
            }
        }catch (ParseException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return tasks;
    }



    public static void main(String[] args) {
        NetworkReceiver nr = new NetworkReceiver(12500,"129.3.20.26");
        nr.run();
    }

}
