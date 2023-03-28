public class Lab10Part2 {
    public static void reverse(int number){

        // Creates a array and finds the length of the number
        int length = Integer.toString(number).length();
        int[] numberArray = new int[length];

        //Isolates a digit of the number and saves it to the array
        for (int i = 0; i < length; i++) {
            numberArray[i] = number % 10;
            number /= 10;  
        }

        //prints out the array in reverse order
        for(int z = 0; z <= numberArray.length-1;z++){
            System.out.print(numberArray[z]);
        }
        //prints a new line once done
        System.out.println();
    }
    public static void main(String[] args) {
        reverse(1234);
        reverse(3456);
        reverse(1230);
        reverse(1045);
        reverse(8000);
        reverse(80);
    }
    
}
