public class Emirp {

    // Method for getting the reverse of a int
    public static int numPalindrome(int num){

        //Finding the length, and creating two char arrays one for the num and the reverse of the num
        int length = Integer.toString(num).length();
        char[] numArray = new char[length];
        char[] numArrayRev = new char[length];

        //Casting the num into a string, creating a empty string for the reverse of str1, parsing the string str1 into a char array
        String str1 = Integer.toString(num);
        String str2 = "";
        numArray = str1.toCharArray();

        // Reversing the numArray into a the reverse array
        for(int i = 0; i < length; i++){
            numArrayRev[i] = numArray[(length-1)-i];
            str2 += numArrayRev[i];
        }
        return Integer.valueOf(str2);

    }

    //Method for checking if it's prime
    public static int isPrime(int num){
        for(int i = 2; i < num; i++){
            if(num % i != 0){
                continue;
            } else {
                return -1;
            }
        }
        return num;
    }

    public static void main(String[] args) {
        // variables for controlling the while loop
        int count = 0; // for counting printed emrips
        int i = 1; // for entering numbers 1 and up

        // While loop to print the first 100 emrips
        while(count <= 99){

            //checking prime status
            if(isPrime(i) == i){

                //checking if the reverse is prime and if the i is a palindrome
                if(isPrime(numPalindrome(i)) == numPalindrome(i) & i != numPalindrome(i)){
                    System.out.print(i + " ");
                    count++;

                    //printing out a new line for every ten emrips
                    if(count%10 == 0){
                        i++;
                        System.out.println();
                    } else {
                        i++;
                    }
                } else{
                    i++;
                }
            } else{
                i++;
            }
        }
        
    }
}