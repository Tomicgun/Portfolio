import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class similarityMatrix implements Serializable {

    //Calculates the tf value of a term within the specific web page
    public static Double tf(HashMapP freqTable, ArrayList<String> webText, String word, int documentIndex) {
        int num = 0;
        outer:
        for (int i = 0; i < freqTable.table.length; ++i) {
            for (HashMapP.Node e = freqTable.table[i]; e != null; e = e.next) {
                if(word.equals(e.key)){
                    num = e.frequency; //Set num to the number of occurences of the word in the given document
                    break outer; //CHANGE HERE
                }
            }
        }
        Double tf = (double) num / webText.size();
        if(tf < 0){
            return (double) 0;
        }else{
            return tf;
        }
    }

    //Calculates the idf value of a term within the corpus
    public static Double idf(HashMapP freqTable, String word) {
        int numOfDocsWithWord = 0;
        for (int i = 0; i < freqTable.table.length; ++i){
            for (HashMapP.Node e = freqTable.table[i]; e != null; e = e.next){
                if(word.equals(e.key)){ //CHANGE HERE
                    numOfDocsWithWord = e.Documents.size();
                }
            }
        }
        Double idf = Math.log(200.0 / numOfDocsWithWord);
        if(idf < 0){
            return (double) 0;
        }else{
            return idf;
        }
    }

    //Calculates the tf-idf value of the web page's text amongst the list of strings from all the web pages
    public static ArrayList<Double> tfIdf(HashMapP freqTable, ArrayList<String> webText, ArrayList<ArrayList<String>> allText) {
        ArrayList<Double> tfIdfValues = new ArrayList<>();

        for (int i = 0; i < allText.size(); i++) {
            ArrayList<String> doc = allText.get(i);
            for (String word : doc) {
                double tfValue = tf(freqTable, webText, word, i);
                if(tfValue ==0){
                    continue;
                }
                double idfValue = idf(freqTable, word);
                if(idfValue == 0){
                    continue;
                }
                tfIdfValues.add(tfValue * idfValue);
            }
        }
        return tfIdfValues;
    }

    /**
     * @description Calculates the cosine similarity between the web page that the user inputted and an indexed web page
     * @param list1 { ArrayList<Double> } contains the tf-idf values of the web page that the user inputted
     * @param list2 { ArrayList<Double> } contains the tf-idf values of the indexed web page
     * @return { double } their cosine similarity
     * @see { https://www.youtube.com/watch?v=hc3DCn8viWs&ab_channel=MinsukHeo%ED%97%88%EB%AF%BC%EC%84%9D }
     */
    public static double similarityTest(ArrayList<Double> list1, ArrayList<Double> list2){
        double similarityScore;
        double numerator = 0.0;
        double denominator;
        double sumSquares1 = 0.0;
        double sumSquares2 = 0.0;

        for (int i = 0; i < list1.size(); i++) {
            double element1 = list1.get(i);
            double element2 = list2.get(i);
            double product = element1 * element2;
            numerator += product;
        }
        for (int i = 0; i < list1.size(); i++) {
            double element1 = list1.get(i);
            double element2 = list2.get(i);
            sumSquares1 += Math.pow(element1, 2);
            sumSquares2 += Math.pow(element2, 2);
        }
        double sqrtSum1 = Math.sqrt(sumSquares1);
        double sqrtSum2 = Math.sqrt(sumSquares2);

        denominator = sqrtSum1 * sqrtSum2;

        similarityScore = numerator/denominator;

        return similarityScore;
    }

    /*
    Generates a list containing the two cosine similarity values that are closest to 1,
    indicating that they are the most similar to the web page the user inputted
    */
    public ArrayList<Integer> topTwo(ArrayList<Double> values) {

        double highestNum1 = Double.NEGATIVE_INFINITY;
        double highestNum2 = Double.NEGATIVE_INFINITY;
        int index1 = -1;
        int index2 = -1;

        for (int i = 0; i < values.size(); i++) {
            double number = values.get(i);
            if (number > highestNum1) {
                highestNum2 = highestNum1;
                index2 = index1;
                highestNum1 = number;
                index1 = i;
            } else if (number > highestNum2) {
                highestNum2 = number;
                index2 = i;
            }
        }

        ArrayList<Integer> topTwoIndexes = new ArrayList<>();
        topTwoIndexes.add(index1);
        topTwoIndexes.add(index2);

        return topTwoIndexes;
    }

    /**
     * @description Generates a list containing two links that are similar to the web page that the user inputted
     * @param link1 { String } contains the link that the user chose
     * @param allLinks { ArrayList<String> } contains all the web page links
     * @return { ArrayList<String> } the two most similar links
     */
    public ArrayList<String> similarLinks(String link1, ArrayList<String> allLinks, HashMapP freqTable) throws IOException{
        ArrayList<Double> tfIdf1 = null;
        ArrayList<Double> tfIdf2;
        ArrayList<Double> similarityList = new ArrayList<>();
        ArrayList<String> restOfLinks = new ArrayList<>();
        ArrayList<ArrayList<String>> parsedLinks = jSoup.parseHTML(allLinks); //can provide this

        for(int i = 0; i < allLinks.size(); i++){
            if (allLinks.get(i).equals(link1)){
                tfIdf1 = tfIdf(freqTable, parsedLinks.get(i), parsedLinks); //can provide this
                break;
            }
        }
        for(int i = 0; i < allLinks.size(); i++){
            if(!(allLinks.get(i).equals(link1))){
                tfIdf2 = tfIdf(freqTable, parsedLinks.get(i), parsedLinks);
                restOfLinks.add(allLinks.get(i));
                similarityList.add(similarityTest(tfIdf1, tfIdf2));
            }
        }
        ArrayList<Integer> highestValIndex = topTwo(similarityList);

        ArrayList<String> twoLinks = new ArrayList<>();
        twoLinks.add(restOfLinks.get(highestValIndex.get(0)));
        twoLinks.add(restOfLinks.get(highestValIndex.get(1)));

        return twoLinks;
    }

    public ArrayList<String> similarLinksV2(String link1, ExtendableHashTable e,ArrayList<Double>tfIdf1) throws IOException, ClassNotFoundException {
        ArrayList<Double> tfIdf2;
        ArrayList<Double> similarityList = new ArrayList<>();
        ArrayList<String> restOfLinks = new ArrayList<>();

        for(int i = 0; i < e.allLinks.size(); i++){
            if(!(e.allLinks.get(i).equals(link1))){
                tfIdf2 = ExtendableHashTable.unSerialize(e.getData(e.allLinks.get(i))).tfIdf;
                restOfLinks.add(e.allLinks.get(i));
                similarityList.add(similarityTest(tfIdf1, tfIdf2));
            }
        }
        ArrayList<Integer> highestValIndex = topTwo(similarityList);
        ArrayList<String> twoLinks = new ArrayList<>();
        twoLinks.add(restOfLinks.get(highestValIndex.get(0)));
        twoLinks.add(restOfLinks.get(highestValIndex.get(1)));

        return twoLinks;
    }
}
