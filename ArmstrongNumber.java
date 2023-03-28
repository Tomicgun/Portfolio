import java.util.Scanner;
public class ArmstrongNumber {
    public static void main(String[] args) {

    // setting variables
    Scanner scan = new Scanner(System.in);
    int lowNum;
    int highNum;

    //collecting range
    System.out.println("What is the lower and upper range for finding Armstrong numbers? (PLease put a space between low and high number)");
    lowNum = scan.nextInt();
    highNum = scan.nextInt();

    //iterating through range and finding all armstrong numbers
    for (int i = lowNum; i <= highNum; i++) {
        int sum = 0;
        int length = String.valueOf(i).length();
        for(int j = i; j > 0;j /= 10){
            sum += (int) Math.pow(j%10,length);
        }
        if(sum == i){
            System.out.println(i);
        }
        
    }
    // No memory leaks
    scan.close();
    }
}
