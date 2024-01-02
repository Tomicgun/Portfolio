import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class WikiRec {
    private JPanel panel1;
    private JRadioButton radioButton1;
    private JButton enterButton;
    private JButton exitButton;
    private JFormattedTextField Start;
    private JFormattedTextField End;
    private JTextArea textArea1;

    boolean flag = false;
    GraphData g;

    /**
     * @description
     */
    public WikiRec() {
        enterButton.addActionListener(e -> {
            /* Notes
            * We must one run union of disjoint sets over all the nodes from the start node
            * then we must check if the end node disjoint set is either the start node or both nodes have
            * the same disjoint set if they do then run dijkstra if not then return they don't match
            *
            * once we run dijkstra we must start at the end node and trace back parent nodes till we get to start node
            * we should append these nodes to an arraylist and then spit out the name of node and weight from start node to end
            *
            *
            * */
            if(flag){
                try {
                    textArea1.setText(null);
                    g = new GraphData();
                    g.setNodes(); //might take forever
                    GraphData.serializeTable(g);
                    int indexStart = g.getNodeIndex(Start.getText());
                    int indexEnd = g.getNodeIndex(End.getText());

                    //check if in the same set
                    if(g.checkConnectivity(indexStart,indexEnd)){
                       ArrayList<String> path = g.getPath(indexStart,indexEnd);
                       //print out the path
                        for(int i = path.size()-1;i>=0;i--){
                            textArea1.append(path.get(i)+"\n");
                        }
                    }else{
                        //they don't connect
                        textArea1.setText("These nodes don't connect and so have no path");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else{
                try {
                    textArea1.setText(null);
                    g = GraphData.unSerializeData(GraphData.getDataFile());
                    int indexStart = g.getNodeIndex(Start.getText());
                    int indexEnd = g.getNodeIndex(End.getText());

                    //check if in the same set
                    if(g.checkConnectivity(indexStart,indexEnd)){
                        ArrayList<String> path = g.getPath(indexStart,indexEnd);
                        //print out the path
                        for(int i = path.size()-1;i>=0;i--){
                            textArea1.append(path.get(i)+"\n");
                        }
                    }else{
                        //they don't connect
                        textArea1.setText("These nodes don't connect and so have no path");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        radioButton1.addActionListener(e -> flag = !flag);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("WikiRec");
        frame.setContentPane(new WikiRec().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
