import java.io.*;
import java.util.ArrayList;

public class GraphData implements Serializable {

    private static class Edge implements Comparable<Edge>,Serializable{
        Node src, dst;  double weight;
        @Serial
        private static final long serialVersionUID = 1L;
        @Override
        public int compareTo(Edge o) {
            return Double.compare(weight,o.weight);
        }

        public Edge(Node s, Node d, double w){
            this.src = s;
            this.dst = d;
            this.weight = w;
        }

    }

    private static final class Node implements Serializable, Comparable<Node>{
        //lots and lots of bookkeeping
        @Serial
        private static final long serialVersionUID = 1L;
        String URL;
        Edge[] edges = new Edge[4];
        ArrayList<Double> similarityList;

        //union of disjoint set stuff
        Node disjointSet;
        int disjointSetSize;

        //for dijkstras algo
        double best;
        int pqIndex;
        Node parent;

        //constructor
        public Node(String URL, ArrayList<Double> similarityList){
            this.URL = URL;
            this.similarityList = similarityList;
            this.disjointSet = this;
            disjointSetSize = 1;
        }

        Node findDisjointSet() { //helper method
            Node d = disjointSet, t = d;
            while(t != (t = t.disjointSet)); // OK in Java, not C

            while (d != t) { // compress
                Node p = d.disjointSet; d.disjointSet = t; d = p;
            }
            return t;
        }

        //this is the actual check that two nodes connect or are in the same set
        void unionDisjointSets(Node x, Node y) {
            Node a = x.findDisjointSet(), b = y.findDisjointSet();
            if (a != b) {
                if (a.disjointSetSize < b.disjointSetSize) {
                    a.disjointSet = b;
                    b.disjointSetSize += a.disjointSetSize;
                } else {
                    b.disjointSet = a;
                    a.disjointSetSize += b.disjointSetSize;
                }
            }
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(best,o.best);
        }
    }

    //custom-built priority queue

    private static class PQ implements Serializable{
        @Serial
        private static final long serialVersionUID = 1L;
        final Node[] array;
        int size;
        static int leftOf(int k) { return (k << 1) + 1; }
        static int rightOf(int k) { return leftOf(k) + 1; }
        static int parentOf(int k) { return (k - 1) >>> 1; }


        //PQ is the starting point and initializes the nodes and creates a array of nodes from a array list
        PQ(ArrayList<Node> nodes, Node root) { //root node is start node
            array = new Node[nodes.size()];
            root.best = 0; //clean up
            root.pqIndex = 0; //clean up
            array[0] = root;
            int k = 1; //PQ index is starting at 1 and going up
            for (Node p : nodes) {
                p.parent = null; //initializes the parent

                //initializing the rest of the nodes
                if (p != root) {
                    p.best = Double.MAX_VALUE;
                    array[k] = p; p.pqIndex = k++;
                }
            }
            size = k;
        }

        //takes in a node and is moving it around based on best variable
        void resift(Node x) {
            int k = x.pqIndex;
            assert (array[k] == x);
            while (k > 0) {
                int parent = parentOf(k);
                Node p = array[parent];
                if (x.compareTo(p) >= 0)
                    break;
                array[k] = p; p.pqIndex = k;
                k = parent;
            }
            array[k] = x; x.pqIndex = k;
        }
        void add(Node x) { // unused; for illustration
            x.pqIndex = size++;
            resift(x);
        }

        //returning the least value in PQ
        Node poll() {

            //extreme case handles
            int n = size;
            if (n == 0) return null;
            Node least = array[0];
            if(least.best == Double.MAX_VALUE) return null;
            size = --n;

            //standard use
            if (n > 0) {
                Node x = array[n]; array[n] = null;
                int k = 0, child;  // while at least a left child
                while ((child = leftOf(k)) < n) {
                    Node c = array[child];
                    int right = child + 1;
                    if (right < n) {
                        Node r = array[right];
                        if (c.compareTo(r) > 0) {
                            c = r;
                            child = right;
                        }
                    }
                    if (x.compareTo(c) <= 0) {
                        break;
                    }
                    array[k] = c; c.pqIndex = k;
                    k = child;
                }
                array[k] = x; x.pqIndex = k;
            }
            return least;
        }

    }




    public GraphData() throws IOException {}
    @Serial
    private static final long serialVersionUID = 1L;
    jSoup j = new jSoup();
    similarityMatrix E = new similarityMatrix();
    ArrayList<String> allLinks = j.setAllLinks();
    ArrayList<ArrayList<String>> parsedLinks = jSoup.parseHTML(allLinks);
    HashMapP table = jSoup.returnFreq(parsedLinks);

    ArrayList<Node> nodes = new ArrayList<>();

    public void setNodes() throws IOException {
        System.out.println("Start of phase 1");
        ArrayList<ArrayList<Double>> allTFIDF = similarityMatrix.getallTFIDFval(table,parsedLinks);
        for(int i=0;i<allLinks.size();i++){
            ArrayList<Double> similarity = E.getSimilarityMetrics(allLinks.get(i),allLinks,allTFIDF);
            Node n = new Node(allLinks.get(i),similarity);
            nodes.add(n);
            System.out.println(n.URL+" worked");
        }
        System.out.println("Stage two reached");

        for(Node node: nodes){
            setEdges(node);
            System.out.println(node.URL+" completed");
        }

        //setting up disjoint sets
        for(Node node: nodes){
            Edge[] e = node.edges;
            for (Edge edge : e) {
                node.unionDisjointSets(edge.src, edge.dst);
            }
        }
        System.out.println("All done");
    }

    private void setEdges(Node n) throws IOException {
        ArrayList<String> restoflinks = j.createLinks();
        ArrayList<String> top4links = E.similarLinks(n.similarityList,restoflinks,n.URL);
        ArrayList<Double> top4sim = E.TopFourSim(n.similarityList);
        for (Node node : nodes) {
            if (node.URL.equals(top4links.get(0))) {
                Edge e = new Edge(n, node, top4sim.get(0));
                n.edges[0] = e;
            } else if (node.URL.equals(top4links.get(1))) {
                Edge e = new Edge(n, node, top4sim.get(1));
                n.edges[1] = e;
            } else if (node.URL.equals(top4links.get(2))) {
                Edge e = new Edge(n, node, top4sim.get(2));
                n.edges[2] = e;
            } else if (node.URL.equals(top4links.get(3))) {
                Edge e = new Edge(n, node, top4sim.get(3));
                n.edges[3] = e;
            }
        }
    }

    void buildShortestPathTree(Node root) { //root node is start node
        PQ pq = new PQ(nodes, root);
        Node p;
        while ((p = pq.poll()) != null) {
            for (Edge e : p.edges) {
                Node s = e.src, d = e.dst;
                double w = s.best + e.weight; // was: w = e.weight;
                if (w < d.best) {
                    d.parent = s;
                    d.best = w;
                    pq.resift(d);
                }
            }
        }
    }

    public int getNodeIndex(String URL){
        for(int i = 0; i<nodes.size();i++){
            if(nodes.get(i).URL.equals(URL)){
                return i;
            }
        }
        return -1;
    }


    public static void serializeTable(GraphData e) throws IOException {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File file = File.createTempFile("GraphData", "Data", dir);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(e);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public static GraphData unSerializeData(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (GraphData) objectInputStream.readObject();
    }

    public static File getDataFile() {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File[] list = dir.listFiles();
        assert list != null;
        for (File name : list) {
            if (name.getName().contains("GraphData")) {
                return name;
            }
        }
        return null;
    }

    public ArrayList<String> getPath(int start,int end){
        //field variables
        Node s = nodes.get(start); Node e = nodes.get(end); //Node p;
        ArrayList<String> path = new ArrayList<>();

        buildShortestPathTree(s); //actually build the path

        //retraces path from end to start recording all intermediate nodes

        while(e.parent != null){
            path.add(e.URL);
            e = e.parent;
        }
        path.add(e.URL);

        return path;
    }


    //check if two nodes are in the same disjoint set
    public boolean checkConnectivity(int start,int end){
        Node s = nodes.get(start); Node e = nodes.get(end);
        if(s.disjointSet.URL.equals(e.disjointSet.URL)){
            return true;
        }else if(e.disjointSet.URL.equals(s.URL)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        GraphData g = new GraphData();
        g.setNodes();
        serializeTable(g);
    }
}
