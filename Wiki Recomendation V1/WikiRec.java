import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class WikiRec {
    private JPanel panel1;
    private JRadioButton ButtonOption1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private JRadioButton radioButton6;
    private JRadioButton radioButton7;
    private JRadioButton radioButton8;
    private JRadioButton radioButton9;
    private JButton okButton;
    private JButton exitButton;
    private JTextField Result1;
    private JTextField Result2;
    private JTextField Title;
    similarityMatrix s = new similarityMatrix();
    jSoup j = new jSoup();
    HashMapP table = jSoup.returnFreq(jSoup.parseHTML(j.allLinks()));

    public WikiRec() throws IOException {

        exitButton.addActionListener(e -> System.exit(0));
        okButton.addActionListener(e -> {
            if(ButtonOption1.isSelected()){
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(0),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton1.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(1),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton2.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(2),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton3.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(3),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton4.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(4),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton5.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(5),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton6.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(6),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton7.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(7),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton8.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(8),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }else if (radioButton9.isSelected()) {
                try {
                    ArrayList<String> result = s.similarLinks(j.allLinks().get(9),j.allLinks(),table);
                    Result1.setText(result.get(0));
                    Result2.setText(result.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("WikiRec");
        frame.setContentPane(new WikiRec().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
