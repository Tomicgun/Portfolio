/*
10-means algo for URLs
 1. Pick 10 random URLs as the means
 2. Get the data / Name the cluster
 3. Compare 190 URLs vs. the 10 means
 4. assign the URL to its most similar mean
 5. compute an average similarity metric for that cluster
 6. check that average similarity for each cluster vs. previous iteration

1. call create 10 mean
2. call create cluster
3. call clustering then avg
4. call check
5. repeat
*/

import java.io.*;
import java.util.*;

public class Clustering implements Serializable{
    /**
     * @description this class builds and forms the clusters, it can handle any cluster size,it does need some outside
     * source control for actual looping, but this class on its own can form and do all the required steps
     * to create one clustering of our wiki links.
    */

    @Serial
    private static final long serialVersionUID = 3396236165247195554L;

    public Clustering(int numMean, ExtendableHashTable e, ArrayList<String> allLinks) throws IOException, ClassNotFoundException{
        numMeans = numMean;
        this.table = e;
        this.allLinks = allLinks;
    }
    public ArrayList<Double> oldSimilarity = new ArrayList<>();
    public ArrayList<Double> newSimilarity = new ArrayList<>();
    public int numMeans;
    public jSoup j = new jSoup();
    public transient ArrayList<String> allLinks;
    public ExtendableHashTable table;
    cluster[] clusters;

    int correct=0;
    /**
     * @description this is our cluster class that contains all the data needed for a cluster mean
     */
    private static class cluster implements Serializable{
        @Serial
        private static final long serialVersionUID = 4188562192294180377L;
        ArrayList<String> clusterMember = new ArrayList<>();
        ArrayList<Double> similarityNum = new ArrayList<>();
        double avgSim;
        int name;
        String mean;

        public cluster(int name, String mean) {
            this.name = name;
            this.mean = mean;
        }
    }
    /**
     * @description picks ten random wiki links from our record, then creates the cluster object for each and saves
     * it to an array.
     */
    private cluster[] createCluster() { //work
        Random r = new Random();
        cluster[] clusters = new cluster[numMeans];
        for(int i = 0; i < numMeans; i++){
            String mean = allLinks.get(r.nextInt(200));
            clusters[i] = new cluster(i, mean);
        }
        return clusters;
    }
    /**
     * @description computes the avg and saves it to the cluster for all clusters
     */
    public void avg() {
        for(int i=0;i<numMeans;i++){
            double avg;
            double sum = 0;
            for(int j=0;j<clusters[i].similarityNum.size();j++){
                sum += clusters[i].similarityNum.get(j);
            }
            avg = sum/clusters[i].similarityNum.size();
            clusters[i].avgSim = avg;
            newSimilarity.add(avg);
        }
    }
    /**
     * @description check if the previous iteration matches the current iteration
     */
    public boolean check() {
        if (!oldSimilarity.isEmpty()) {
            double sum1 = 0;
            double sum2 = 0;
            double num1;
            double num2;
            for (int i = 0; i <numMeans; i++) {
                sum1 += oldSimilarity.get(i);
                sum2 += newSimilarity.get(i);
                num1 = sum1/oldSimilarity.size();
                num2 = sum2/newSimilarity.size();
                double low = num1-(num1 * 0.2);
                double high = num1+(num1 * 0.2);
                if (num2 < high | num2 > low) {
                    correct++;
                }
            }
            if(correct>5){
                return true;
            }
        }
        return true;
    }
    /**
     * @description
     */
    public String getClusters(String url) {
        for(int i =0; i<numMeans;i++){
            for(int j=0;j<clusters[i].clusterMember.size();j++){
                String member = clusters[i].clusterMember.get(j);
                if(url.equals(member)){
                    return Integer.toString(clusters[i].name);
                }
            }
        }
        return "Cluster not found";
    }
    /**
     * @description
     */
    public void clusteringfirst() throws IOException, ClassNotFoundException {
        clusters = createCluster();
        ArrayList<SearCreate.SearFile> meanData = new ArrayList<>();
        for(int i=0;i<numMeans;i++){
            meanData.add(ExtendableHashTable.unSerialize(table.getData(clusters[i].mean)));
        }
        File URLs = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\URLs.txt");
        Scanner URL = new Scanner(URLs);
        while (URL.hasNextLine()) {
            String url = URL.nextLine();
            SearCreate.SearFile data = ExtendableHashTable.unSerialize(table.getData(url));
            double max = 0;
            int index = -1;
            ArrayList<Double> sim = new ArrayList<>();
            for (int i = 0; i < numMeans; i++) {
                if (!clusters[i].mean.equals(url)) {
                    double k = similarityMatrix.similarityTest(data.tfIdf,meanData.get(i).tfIdf);
                    sim.add(k);
                    if (k > max) {
                        max = k;
                        index = i;
                    }
                }
            }
            sim.sort(Comparator.naturalOrder());
            clusters[index].clusterMember.add(url);
            clusters[index].similarityNum.add(sim.get(sim.size()-1));
        }
        avg();
    }

    public void recluster(ArrayList<Double> oldSimilarity) throws IOException, ClassNotFoundException {
        clusters = createCluster();
        this.oldSimilarity = oldSimilarity;
        ArrayList<SearCreate.SearFile> meanData = new ArrayList<>();
        for(int i=0;i<numMeans;i++){
            meanData.add(ExtendableHashTable.unSerialize(table.getData(clusters[i].mean)));
        }
        File URLs = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\URLs.txt");
        Scanner URL = new Scanner(URLs);
        while (URL.hasNextLine()) {
            String url = URL.nextLine();
            SearCreate.SearFile data = ExtendableHashTable.unSerialize(table.getData(url));
            double max = 0;
            int index = -1;
            ArrayList<Double> sim = new ArrayList<>();
            for (int i = 0; i < numMeans; i++) {
                if (!clusters[i].mean.equals(url)) {
                    double k = similarityMatrix.similarityTest(data.tfIdf,meanData.get(i).tfIdf);
                    sim.add(k);
                    if (k > max) {
                        max = k;
                        index = i;
                    }
                }
            }
            sim.sort(Comparator.naturalOrder());
            clusters[index].clusterMember.add(url);
            clusters[index].similarityNum.add(sim.get(sim.size()-1));
        }
        avg();
    }

    public static Clustering unSerializeCluster(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Clustering) objectInputStream.readObject();
    }

    public static File getClsuterFile() {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File[] list = dir.listFiles();
        assert list != null;
        for (File name : list) {
            if (name.getName().contains("Cluster")) {
                return name;
            }
        }
        return null;
    }

    public static void serializeCluster(Clustering c) throws IOException {
        File dir = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\SeralizedFile");
        File file = File.createTempFile("Cluster", "record", dir);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(c);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
}
