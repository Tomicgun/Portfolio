import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;

public class SearCreate {
    /**
     * @description This class has one subclass which is the searFile which stores all the data
     * in our persistent data store. The rest of the class gives useful methods to run on SearFile.
     * This is mainly a helper class for the extendable hash table.
     */

    public static class SearFile implements Serializable {
        @Serial
        private static final long serialVersionUID = 2135848576080190127L;
        public String URL;
        //public HashMapP freqtable;
        public ArrayList<Double> tfIdf;
        //ArrayList<String> textOne;
        public SearFile(String URL /*,HashMapP freqtable,ArrayList<String> textOne*/,ArrayList<Double> tfIdf) {
            this.URL = URL;
            //this.freqtable = freqtable;
            //this.textOne = textOne;
            this.tfIdf = tfIdf;

        }
    }

    public SearCreate() {}

    /**
     * @description taking all the URLs from allLinks it creates a jSoup object. It then gets the text from the URL
     * then it gets the freqtable for that URL, and the freqtable for all URLs. Then it gets the tfidf values and
     * stores it in a searFile Object. It then saves that object to a list.
     * */
    private SearFile createSearObj(String URL,HashMapP freqAll,ArrayList<ArrayList<String>> textAll) throws IOException {
        //actual creation of serialized files
        jSoup link = new jSoup();
        //getting required data
        ArrayList<String> textOne = link.parsedHTMLforOne(URL);
        //HashMapP table = link.returnFreqforOne(textOne);
        ArrayList<Double> tfIdf = similarityMatrix.tfIdf(freqAll,textOne,textAll);
        //creates SearFile
        return new SearFile(URL/*,table, textOne*/,tfIdf);
    }

    

    /**
     * @description It saves the directory for the temp/serialized files to dir, it then saves the bytes of the searFile objects
     * after being serialized to that temp file, and save the name of that temp file to a list of all names of all the temp files.
     * */
    public File createSearFile(String URL,HashMapP freqAll,ArrayList<ArrayList<String>>textAll) throws IOException {
        //creating directory path
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        //creating temp file
        File file = File.createTempFile("link","txt",dir);
        //Creating SearObject
        SearFile s = createSearObj(URL,freqAll,textAll);
        //writing out sear obj to temp file
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(s);
        System.out.println(URL + " worked");
        objectOutputStream.flush();
        objectOutputStream.close();
        return file;
    }
}

