import java.util.Scanner;

public class TwoDimensionArraySort {

    //declaring variables
    public static double[][] sortRows(double[][] m){

        //copying over data
        double[][] m1 = new double[3][3];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                m1[i][j] = m[i][j];        
            }
        }

        //sorting data
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length-1; j++) {
                if(m1[i][j] > m1[i][j+1]){
                   double temp = m1[i][j];
                    m1[i][j] = m1[i][j+1];
                    m1[i][j+1] = temp;
                }
            }
        }
        return m1;
    }

    public static void main(String[] args) {

        //declaring variables
        Scanner scan = new Scanner(System.in);
        double[][] m = new double[3][3];

         //finding numbers in matrix
         System.out.println("What are the numbers in your 3 x 3 matrix (start from the 0,0 position and end at the 3,3 Position)");
         for(int i = 0; i < m.length; i++){
             for(int j = 0; j < m[i].length; j++){
                 m[i][j] = scan.nextDouble();
             }
         }
        //printing results
        m = sortRows(m);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }

        //No memory leaks
        scan.close();
    }
}
