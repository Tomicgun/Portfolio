import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class WikiRec {
    private JPanel panel1;
    private JButton okButton;
    private JButton exitButton;
    private JTextField Title;
    private JFormattedTextField recbox2;
    private JFormattedTextField entrybox;
    private JFormattedTextField categorybox;
    private JFormattedTextField recbox1;
    private JRadioButton radioButton1;
    private JFormattedTextField formattedTextField1;
    private boolean flag = false;
    /**
     * @description
     */
    public WikiRec() {
        radioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = true;
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        okButton.addActionListener(e -> {
            String input = entrybox.getText();
            input = "https://en.wikipedia.org/wiki/" + input;
            int numMeans = Integer.parseInt(formattedTextField1.getText());
            similarityMatrix s = new similarityMatrix();
            ExtendableHashTable E;
            String cluster;
            try {
                if(flag){
                    E = new ExtendableHashTable();
                    ExtendableHashTable.run(E); //takes about 6 hours
                    ExtendableHashTable.serializeTable(E);
                    Clustering c = new Clustering(numMeans,E,E.allLinks);
                    c.clusteringfirst(); //varies stops when the old averages are with in 10 percent range of the new averages
                    int i=0;
                    while(c.check()){
                        c.recluster(c.newSimilarity);
                        i++;
                        System.out.println(i+" Clusters complete");
                    }
                    Clustering.serializeCluster(c);
                    cluster = c.getClusters(input);
                }else{
                    E = ExtendableHashTable.unSerializeTable(ExtendableHashTable.getTableFile());
                    Clustering c = Clustering.unSerializeCluster(Clustering.getClsuterFile());
                    cluster = c.getClusters(input);
                }
                SearCreate.SearFile inputData = ExtendableHashTable.unSerialize(E.getData(input));
                ArrayList<String> result = s.similarLinksV2(input,E,inputData.tfIdf);
                recbox1.setText(result.get(0));
                recbox2.setText(result.get(1));
                categorybox.setText(cluster);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("WikiRec");
        frame.setContentPane(new WikiRec().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
