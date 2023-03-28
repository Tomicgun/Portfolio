import java.util.Scanner;
public class Lab10Part1 {
    public static void displayPattern(int n){
    
    //setting array for pattern, and a int variable to shorten then number of columns after a i iteration
    int[][] pattern = new int[n][n];
    int columnShorter = 0;

    //the loop to print set the array from top to bottom, starting at 0,0.
    for(int row = 0; row < pattern.length; row++){
        int numberShorter = n-1; // causes the pattern of n, n-1, 3, 2, 1
        for(int column = 0 ; column < pattern.length - columnShorter; column++){
                pattern[row][column] = n - numberShorter; //assigns the numbers to the array
                numberShorter--;
        }
    columnShorter++;
    }
    //prints out the array from bottom to top, this causes the pattern to print out correctly
    for(int i = n-1; i >= 0 ; i--){
        for(int j = n-1; j >= 0; j--){
            if(pattern[i][j] == 0){
                System.out.print("    ");
            }
            else{
                System.out.print(pattern[i][j] + "   ");
            }
        }
        System.out.println();
    }
}

public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int n;
    System.out.println("What is the number you want to input for the patter?");
    n = scan.nextInt();
    displayPattern(n);

    // no memory leaks
    scan.close();
}
}

/* You input 5

   The program then writes out 

    5 4 3 2 1
    0 4 3 2 1
    0 0 3 2 1
    0 0 0 2 1
    0 0 0 0 1

    The program then reads it from bottom to top so it reads it out as

    0 0 0 0 1
    0 0 0 2 1
    0 0 3 2 1
    0 4 3 2 1
    5 4 3 2 1

    However while it printing it out it put spaces where the zero ares so it prints outs
                  1
              2   1
          3   2   1
      4   3   2   1
  5   4   3   2   1
*/