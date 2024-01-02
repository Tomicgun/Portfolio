import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class similarityMatrix implements Serializable {

    //Calculates the tf value of a term within the specific web page
    public static Double tf(ArrayList<String> webText, String word) {
        int num = 0;
        for(String s: webText){ //gets the frequency of a word in a single text
            if(s.equals(word)){
                num++;
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
    public static Double idf(HashMapP freqTableAll, String word) {
        int numOfDocsWithWord = 0;
        outer:
        for (int i = 0; i < freqTableAll.table.length; ++i){
            for (HashMapP.Node e = freqTableAll.table[i]; e != null; e = e.next){
                if(word.equals(e.key)){ //CHANGE HERE
                    numOfDocsWithWord = e.Documents.size();
                    break outer;
                }
            }
        }
        Double idf = Math.log(1000.0 / numOfDocsWithWord);
        if(idf < 0){
            return (double) 0;
        }else{
            return idf;
        }
    }

    //Calculates the tf-idf value of the web page's text amongst the list of strings from all the web pages
    public static ArrayList<Double> tfIdf(HashMapP freqTable, ArrayList<String> webText) {
        ArrayList<Double> tfIdfValues = new ArrayList<>(); //tfidf vector for this document

        //iterate over every unique word in the freq table and calculate a tfidf score for each word
        //most will be zero, but some will not
        for (int j = 0; j < freqTable.table.length; ++j) {
            for (HashMapP.Node e = freqTable.table[j]; e != null; e = e.next) {
                double tfValue = tf(webText, (String) e.key);
                if(tfValue == 0){
                    continue;
                }
                double idfValue = idf(freqTable, (String) e.key);
                if(idfValue == 0){
                    continue;
                }
                tfIdfValues.add(tfValue * idfValue);
            }
        }
        //return this vector
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

        int size = Math.min(list1.size(), list2.size());

        for (int i = 0; i < size; i++) {
            double element1 = list1.get(i);
            double element2 = list2.get(i);
            double product = element1 * element2;
            numerator += product;
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
    public ArrayList<Integer> topFour(ArrayList<Double> values) {

        double highestNum1 = Double.NEGATIVE_INFINITY;
        double highestNum2 = Double.NEGATIVE_INFINITY;
        double highestNum3 = Double.NEGATIVE_INFINITY;
        double highestNum4 = Double.NEGATIVE_INFINITY;
        int index1 = -1;
        int index2 = -1;
        int index3 = -1;
        int index4 = -1;

        for (int i = 0; i < values.size(); i++) {
            double number = values.get(i);
            if (number > highestNum1) {
                highestNum2 = highestNum1;
                index2 = index1;
                highestNum1 = number;
                index1 = i;
            } else if (number > highestNum2) {
                highestNum3 = highestNum2;
                index3 = index2;
                highestNum2 = number;
                index2 = i;
            } else if (number > highestNum3) {
                highestNum4 = highestNum3;
                index4 = index3;
                highestNum3 = number;
                index3 = i;
            } else if (number > highestNum4) {
                highestNum4 = number;
                index4 = i;
            }
        }

        ArrayList<Integer> topFourIndexes = new ArrayList<>();
        topFourIndexes.add(index1);
        topFourIndexes.add(index2);
        topFourIndexes.add(index3);
        topFourIndexes.add(index4);

        return topFourIndexes;
    }

    public ArrayList<Double> TopFourSim(ArrayList<Double> values){
        double highestNum1 = Double.NEGATIVE_INFINITY;
        double highestNum2 = Double.NEGATIVE_INFINITY;
        double highestNum3 = Double.NEGATIVE_INFINITY;
        double highestNum4 = Double.NEGATIVE_INFINITY;

        for (double number : values) {
            if (number > highestNum1) {
                highestNum2 = highestNum1;
                highestNum1 = number;
            } else if (number > highestNum2) {
                highestNum3 = highestNum2;
                highestNum2 = number;
            } else if (number > highestNum3) {
                highestNum4 = highestNum3;
                highestNum3 = number;
            } else if (number > highestNum4) {
                highestNum4 = number;
            }
        }
        ArrayList<Double> topFourVal = new ArrayList<>();
        topFourVal.add(highestNum1);
        topFourVal.add(highestNum2);
        topFourVal.add(highestNum3);
        topFourVal.add(highestNum4);
        return topFourVal;
    }

    public ArrayList<Double> getSimilarityMetrics(String link1, ArrayList<String> allLinks,ArrayList<ArrayList<Double>> allTFIDF) throws IOException {
        ArrayList<Double> tfIdf1;
        ArrayList<Double> tfIdf2;
        ArrayList<Double> similarityList = new ArrayList<>();

        int index = -1;

        for(int i = 0;i<allLinks.size();i++){
            if(allLinks.get(i).equals(link1)){
                index = i;
                break;
            }
        }
        tfIdf1 = allTFIDF.get(index);
        for(int i = 0; i<allTFIDF.size();i++){
            if(i == index){
                continue;
            }
            tfIdf2 = allTFIDF.get(i);
            similarityList.add(similarityTest(tfIdf1, tfIdf2));
        }
        return similarityList;
    }

    public static ArrayList<ArrayList<Double>> getallTFIDFval(HashMapP freqTable,ArrayList<ArrayList<String>> parsedLinks){
        ArrayList<ArrayList<Double>> allTFIDF = new ArrayList<>();
        int i = 0;
        for (ArrayList<String> parsedLink : parsedLinks) {
            ArrayList<Double> tfIdf = tfIdf(freqTable, parsedLink);
            allTFIDF.add(tfIdf);
            i++;
            System.out.println("TFIDF worked" + i);
        }
        return allTFIDF;
    }

    /**
     * @description Generates a list containing four links that are similar to the web page that the user inputted
     * @param link1 { String } contains the link that the user chose
     * @param allLinks { ArrayList<String> } contains all the web page links
     * @return { ArrayList<String> } the two most similar links
     */
    public ArrayList<String> similarLinks(ArrayList<Double> similarityList,ArrayList<String> allLinks,String link1) {
        //getting the rest of links besides our link1
        for(int i = 0; i < allLinks.size(); i++){
            if(allLinks.get(i).equals(link1)){
                allLinks.remove(i);
                break;
            }
        }
        ArrayList<Integer> highestValIndex = topFour(similarityList);

        ArrayList<String> FourLinks = new ArrayList<>();
        FourLinks.add(allLinks.get(highestValIndex.get(0)));
        FourLinks.add(allLinks.get(highestValIndex.get(1)));
        FourLinks.add(allLinks.get(highestValIndex.get(2)));
        FourLinks.add(allLinks.get(highestValIndex.get(3)));

        return FourLinks;
    }
}
