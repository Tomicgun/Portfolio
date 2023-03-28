
public class Palindrome {
    //Method returns a string, this was easiest for the logic control at the end
    public static String palindromeFinder(String str1) {

        //saving the unedited string for output at the end
        String str2 = str1;

        //removing spaces and and converting uppercase to lower case
        str1 = str1.toLowerCase();
        str1 = str1.replaceAll("\\s","");

        /* I created a char array for the un-reversed word and the reversed word, this made it easier to
           compare the two arrays at the end, and reverse the array so that the word is in reverse order. */
        char[] word1Array = str1.toCharArray();
        char[] wordrev =  new char[str1.length()];

        //reversing the order of word1Array and saving it to wordrev
        for (int i = 0; i < word1Array.length; i++) {
            wordrev[(wordrev.length-1)-i] = word1Array[i];
        }

        //adding the the first character back into the array, since it gets removed during the above for loop
        wordrev[0] = str1.charAt(0);


        /* checking if the reverse array equals the normal array, initially i used .equals on the arrays, but it was not cooperating
           so I had to put in my own logic, this is why the method returns string instead of void. So that I can put in return statements
           that end the method. */
        for(int i = 0; i < wordrev.length-1; i++){
            if(word1Array[i] == wordrev[i]){
                continue;
            }
            else{
                return  str2 +" is NOT a palindrome";
            }
        }
        return str2 +" is a palindrome";
    }

    //testing results
    public static void main(String[] args) {
        System.out.println(palindromeFinder("Race Car"));
        System.out.println(palindromeFinder("madam"));
        System.out.println(palindromeFinder("banana"));
        System.out.println(palindromeFinder("Too     HOT to hoot"));
    }
}
