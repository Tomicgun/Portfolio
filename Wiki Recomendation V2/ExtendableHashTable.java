import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ExtendableHashTable implements Serializable {
    /**
     * @description Global variables all for ether serializing the table or for creating the table
     */
    @Serial
    private static final long serialVersionUID = 1083625628370526033L;
    int globalDepth = 1;
    int size;
    ArrayList<String> allLinks = new jSoup().setAllLinks();
    ArrayList<ArrayList<String>> textAll = jSoup.parseHTML(allLinks);
    HashMapP freqTable = jSoup.returnFreq(textAll);

    public ExtendableHashTable() throws IOException {
    }
    /**
     * @description This the first of our "node" classes, they implement a serialVersionUID and contain all the
     * data needed to make the table run.
     */
    static final class Directory implements Serializable {
        @Serial
        private static final long serialVersionUID = 4639291352041596929L;
        Bucket b;
        String URL;
        int key;

        public Directory(String URL, int key, Bucket b) {
            this.URL = URL;
            this.key = key;
            this.b = b;
        }

        public Directory(String URL, int key) {
            this.URL = URL;
            this.key = key;
        }
    }
    /**
     * @description This is our second "node" class pretty much same as above, but it stores the actual data
     * This is so when I resize it, we do not need to completely recreate the table, but simply move pointers around.
     */
    static final class Bucket implements Serializable {
        @Serial
        private static final long serialVersionUID = 8180011712761808731L;
        File dataFile;

    }

    Directory[] table = new Directory[2];
    /**
     * @description get back the globalDepth bits for the binary of the provided URL
     * @param url A string of a wiki link
     */
    private int hashCode(String url) {
        return url.hashCode() & ((1 << globalDepth) - 1);
    }
    /**
     * @description This is our resize function called when we need more significant bits to hash URLs
     * It creates a new table saves the old, and populates the new table and rearranges the pointers from the
     * new directory's with the old buckets.
     */
    private void resize() {
        Directory[] oldTable = table;
        Directory[] newTable = new Directory[table.length * 2];
        table = newTable;
        globalDepth++;
        for (Directory directory : oldTable) {
            if (directory != null) {
                int key = hashCode(directory.URL);
                for (int i = 0; i < table.length; i++) {
                    if (table[i] == null) {
                        table[i] = new Directory(directory.URL, key);
                        break;
                    }
                }
            }
        }
        for (Directory d1 : oldTable) {
            for (Directory d2 : newTable) {
                if (d2 != null && d1 != null && (d1.URL.compareTo(d2.URL) == 0)) {
                    d2.b = d1.b;
                }
            }
        }
    }
    /**
     * @description creates a mapping of URLs to their data file if a URLs key is already in the table
     * then it calls resize, then adds the URL map, otherwise it simply creates the mapping from URL to data
     * @param URL wiki link
     */
    private void add(String URL) throws IOException {
        int key = hashCode(URL);
        if (contains(key) || table.length == size) {
            resize();
            add(URL);
            return;
        }
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                Bucket b = new Bucket();
                SearCreate r = new SearCreate();
                b.dataFile = r.createSearFile(URL, freqTable, textAll);
                table[i] = new Directory(URL, key, b); //Create bucket
                size++;
                return;
            }
        }
    }
    /**
     * @description A small helper function that checks if the table contains the key
     * @param key the hashcode method of the wiki link text
     */
    private boolean contains(int key) {
        for (Directory directory : table) {
            if (directory != null && directory.key == key) {
                return true;
            }
        }
        return false;
    }
    /**
     * @description gets data from a string
     * @param URL wiki link
     */
    public File getData(String URL) {
        for (Directory directory : table) {
            if(directory != null && directory.URL.compareTo(URL) == 0){ return directory.b.dataFile;}
        }
        return null;
    }
    /**
     * @description serializes the table
     * @param e An ExtendableHashTable object that contains the mapping
     */
    public static void serializeTable(ExtendableHashTable e) throws IOException {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File file = File.createTempFile("ExtendableTable", "table", dir);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(e);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
    /**
     * @description turns a file into the SearFile object
     * @param file the file that stores the data for the SearFile object
     */
    public static SearCreate.SearFile unSerialize(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (SearCreate.SearFile) objectInputStream.readObject();
    }
    /**
     * @description turns a file into an ExtendableHashTable object
     * @param file a file that stores the data for the ExtendableHashTable object
     */
    public static ExtendableHashTable unSerializeTable(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (ExtendableHashTable) objectInputStream.readObject();
    }
    /**
     * @description finds and returns the file that stores the table
     */
    public static File getTableFile() {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File[] list = dir.listFiles();
        assert list != null;
        for (File name : list) {
            if (name.getName().contains("ExtendableTable")) {
                return name;
            }
        }
        return null;
    }
    /**
     * @description Actually builds the table
     * @param e the ExtendableHashTable object that it saves the resulting table too
     */
    public static void run(ExtendableHashTable e) throws IOException {
        File URLs = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\URLs.txt");
        Scanner URL = new Scanner(URLs);
        while (URL.hasNextLine()) {
            String url = URL.nextLine();
            e.add(url);
        }
        serializeTable(e);
    }
}

