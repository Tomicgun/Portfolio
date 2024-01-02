import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class jSoup implements Serializable {
    ArrayList<String> allLinks= new ArrayList<>();

    /**
     * @description setter for AllLinks variable
     * */
    public ArrayList<String> setAllLinks() throws IOException {
        //Transferring the 200 URLs from the text file to the arraylist allLinks
        File URLs = new File("C:\\Users\\thoma\\IdeaProjects\\csc365\\src\\URLs.txt");
        Scanner URL = new Scanner(URLs);
        while (URL.hasNextLine()) {
            String data = URL.nextLine();
            allLinks.add(data);
        }
        return allLinks;
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
            //s = s.replaceAll("(?s)(?<!\\S).(?!\\S)", " ");
            //s = s.replaceAll("\s+"," ");
            s = s.toLowerCase();
            result.add(stringSplit(s));
        }
        return result;
    }
    private static ArrayList<String> stringSplit(String s){
        StringTokenizer token = new StringTokenizer(s," "); //Splits at " " of string s
        int count = token.countTokens(); //gets number of tokens in token
        ArrayList<String> result = new ArrayList<>(); //arraylist for the result
        for(int i = 0; i < count; i++){
            result.add(token.nextToken());
        }
        return result;
    }

    public static  HashMapP returnFreq(ArrayList<ArrayList<String>> text){
        HashMapP table = new HashMapP();
        for(int i =0; i< text.size();i++){
            ArrayList<String> s = text.get(i);
            for (String str:s){
                table.add(str);
                table.docUpdate(str,i);
            }
        }
        return table;
    }

    public ArrayList<String> parsedHTMLforOne(String e) throws IOException{
        Document doc = Jsoup.connect(e).get(); //connect to the wiki page
        String s = doc.text();
        s = s.replaceAll("[^a-zA-Z]+"," "); //replaces all characters except alphanumeric chars, and replaces it with spaces
        //s = s.replaceAll("(?s)(?<!\\S).(?!\\S)", " ");
        //s = s.replaceAll("\s+"," ");
        s = s.toLowerCase();
        return stringSplit(s);
    }

    //implement the correct hash table that Professor Lea gave us.
    public HashMapP returnFreqforOne(ArrayList<String> text){
        HashMapP table = new HashMapP();
        //two cases the table contains said word, table does not contain said word
        for (String s : text) {
            table.add(s);
        }
        return table;
    }
}
