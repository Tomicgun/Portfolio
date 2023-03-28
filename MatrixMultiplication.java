import java.util.Scanner;

public class MatrixMultiplication {
    public static double[][] multiplyMatrix(double [][] a, double[][] b){

        //creating a product matrix of the numbers of rows of matrix a and the columns of matrix b
        double[][] c = new double[a.length][b[0].length];

        /*This loop multiplies each row of matrix a by the first column of matrix b
         It then repeats but this time its times the second column of matrix b */
        for(int i = 0; i < c.length; i++){ //controlling where the sum is put into the c matrix and what column the a rows are multiplying against.
            for (int j = 0; j < c.length; j++) { //controlling rows
                for (int z = 0; z < c[0].length; z++) { //controlling columns
                    c[j][i] += a[j][z] * b[z][i];
                }
            }
        }
        return c;
    }

    public static void main(String[] args) {
        //variables
        Scanner scan = new Scanner(System.in);
        double[][] a = new double[3][3];
        double[][] b = new double[3][3];


            //finding numbers in matrix
            System.out.println("What are the numbers in your first matrix (start from the 0,0 position and end at the 3,3 Position)");
            for(int i = 0; i < a.length; i++){
                for(int j = 0; j < a[i].length; j++){
                    a[i][j] = scan.nextDouble();
                }
            }
    
            System.out.println("What are the numbers in your second matrix (start from the 0,0 position and end at the 3,3 Position)");
            for(int i = 0; i < b.length; i++){
                for(int j = 0; j < b[i].length; j++){
                    b[i][j] = scan.nextDouble();
                }
            }
            //outputting answers and rounding the numbers
            double[][] productMatrix = multiplyMatrix(a, b);
            for (int i = 0; i < productMatrix.length; i++) {
                for (int j = 0; j < productMatrix[0].length; j++) {
                    double temp = Math.round(productMatrix[i][j] * 100.0) /100.0;
                    System.out.print(temp + " ");
                }
                System.out.println();
        }
        //no memory leaks
        scan.close();
    }
}
