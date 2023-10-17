import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class jSoup {

    //This was for testing
    public ArrayList<String> allLinks() {
        ArrayList<String> links = new ArrayList<>();
        links.add("https://en.wikipedia.org/wiki/Eastern_bluebird");
        links.add("https://en.wikipedia.org/wiki/American_robin");
        links.add("https://en.wikipedia.org/wiki/Northern_cardinal");
        links.add("https://en.wikipedia.org/wiki/Common_loon");
        links.add("https://en.wikipedia.org/wiki/Black-capped_chickadee");
        links.add("https://en.wikipedia.org/wiki/American_goldfinch");
        links.add("https://en.wikipedia.org/wiki/Eastern_towhee");
        links.add("https://en.wikipedia.org/wiki/Baltimore_oriole");
        links.add("https://en.wikipedia.org/wiki/Tufted_titmouse");
        links.add("https://en.wikipedia.org/wiki/Red-tailed_hawk");
        return links;
    }

    //This method scrapes and sanitizes the text
    public static ArrayList<ArrayList<String>> parseHTML(ArrayList<String> e) throws IOException {
        //Data field
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        //For each string (HTML link) in array e
        for(String str: e){
            Document doc = Jsoup.connect(str).get(); //connect to the wiki page
            String s = doc.text();
            s = s.replaceAll("[^a-zA-Z]+"," "); //replaces all characters except alphanumeric chars, and replaces it with spaces
            s = s.toLowerCase();
            result.add(stringSplit(s));
        }
        return result;
    }

    public static ArrayList<String> stringSplit(String s){
        StringTokenizer token = new StringTokenizer(s," "); //Splits at " " of string s
        int count = token.countTokens(); //gets number of tokens in token
        ArrayList<String> result = new ArrayList<>(); //arraylist for the result
        for(int i = 0; i < count; i++){
            result.add(token.nextToken());
        }
        return result;
    }
    //implement the correct hash table that Professor Lea gave us.
    public static HashMapP returnFreq(ArrayList<ArrayList<String>> text){
        HashMapP table = new HashMapP();
        //two cases the table contains said word, table does not contain said word
        for(int i = 0; i < text.size(); i++){
            ArrayList<String> s = text.get(i);
            for (String key : s) {
                if (!table.contains(key)) {
                    table.add(key);
                }
                table.docUpdate(key, i);
            }
        }
        return table;
    }
}
