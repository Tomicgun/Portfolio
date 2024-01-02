import java.util.Scanner;

public class MatrixSort {

    public static void sort(int[][] m){
        int temp;
        
        // Primary search, its a bubble sort but modified to move the whole row up or down
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m.length-1; j++){
                if(m[j][0] > m[j+1][0]){
                    for(int z = 0; z < m[0].length; z++){  //this is for moving the rows around
                        temp = m[j+1][z];
                        m[j+1][z] = m[j][z];
                        m[j][z]= temp;
                    }
                }
            }
        }

        // Secondary Search, bubble sort, but keeps repeating until all columns in the array are sorted
        for(int b = 0; b < m[0].length-1; b++){ // controls how many times the bubble sort is done
            for(int i = 0; i < m.length; i++){ //start of bubble sort
                for(int j = 0; j < m.length-1; j++){
                    if(m[j][0] == m[j+1][0]){ //sorting the ones with the same first column number
                        if(m[j][b+1] > m[j+1][b+1]){ //bubble sort
                            for(int z = 0; z < m[0].length; z++){ //moving the columns around
                                temp = m[j+1][z];
                                m[j+1][z] = m[j][z];
                                m[j][z]= temp;
                            }
                        }
                    }
                }
            }
        }

        //printing out the results
        for (int i = 0; i < m.length; i++) {
            System.out.print("{");
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.print("}");
            System.out.println();
        }
    }

    public static void main(String[] args) {

        //initializing variables
        Scanner scan = new Scanner(System.in);
        int row;
        int column;

        //finding dimensions
         System.out.println("What is the numbers of rows and columns in your matrix? (enter row  then column with a space in between)");
         row = scan.nextInt();
         column = scan.nextInt();
         int[][] m = new int[row][column];
 
         //finding numbers in array
         System.out.println("What are the numbers in your " + row + " x " + column + 
         " matrix (start from the 0,0 position and end at the " + row + "," + column + " Position)");
         for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[i].length; j++){
                m[i][j] = scan.nextInt();
            }
    }
    sort(m);

    //No memory leaks
    scan.close();
}
}
