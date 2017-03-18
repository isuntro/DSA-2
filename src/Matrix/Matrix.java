package Matrix;
/**
 * Created by tiberiusimionvoicu - 100125468
 * on 10/03/2017.
 */
import java.util.*;
public class Matrix {
    private int[][] matrix;
    private int n;
    private Random rad = new Random();

    /** Constructor creates a Matrix.Matrix from
     *  a 2d array
     *
     * @param m - 2d array
     */
    public Matrix(int[][] m){
        this.matrix = m;
        n = m[0].length;
    }
    public Matrix(int n){
        this.n = n;
    }

    /** Algorithm to find an element of a
     *  matrix with no special properties
     *
     * @param A = the matrix(2d array)
     * @param n = length of the matrix
     * @param p = number to look for
     * @return - true if found / false otherwise
     */
    public static boolean findElementD(int A[][], int n, int p){
        int notries = 0;
        for(int c=0; c<n; c++){
            for (int a : A[c]) {
                notries++;
                if (p == a){
                    System.out.println(" No tries = " + notries + " found no : " + a);
                    return true;
                }
            }
        }
        System.out.println("Alg 1 No tries = " + notries);
        return false;
    }

    /** Algorithm to find an element of a
     *  matrix that has each row of integers
     *  in non-decreasing order
     *
     * @param A = the matrix(2d array)
     * @param n = length of the matrix
     * @param p = number to look for
     * @return - true if found / false otherwise
     */
    public static boolean findElementD1(int A[][], int n, int p){
        int notries = 0;
        for(int []row : A){
            int max = n-1;
            int min = 0;
            int i;
            if(p <= row[n-1] && p >= row[0]) {
                while(max >= min){
                    notries++;
                    i = (max+min)/2;
                    if(p == row[i]){
                        System.out.println(" No tries = " +notries + " found no : " + row[i]);
                        return true;

                    }
                    else if ( p < row[i]){
                        max = max - 1;
                    }
                    else {
                        min = i + 1;
                    }
                }
            }
            else
                notries++;
        }
        System.out.println(" No tries = " + notries);
        return false;
    }

    /**
     *
     * @param A = the matrix(2d array)
     * @param n = length of the matrix
     * @param p = number to look for
     * @return - true if found / false otherwise
     */
    public static boolean findElementD2(int A[][], int n, int p){
        int notries = 0;
        int index = n-1;
        for(int[] row : A){
            while(index != -1) {
                notries++;
                if(p == row[index]) {
                    System.out.println(" No tries = " + notries + " found no : "+row[index]);
                    return true;
                }
                if (p < row[index]) {
                    index -= 1;
                }
                else
                    break;
            }
        }
        System.out.println(" No tries = "+notries);
        return false;
    }

    public String toString(){
        String out = "";
        for( int c=0; c<n; c++) {
            for (int i : matrix[c]) {
                out += (i + "\t");

            }
            out += ("\n");
        }
        return out;
    }
/*  INITIAL IMPLEMENTATION OF RANDOM MATRIX GENERATOR
    INEFFICIENT

    public void randomD1( int p){
        int upperBound = 8;
        int lowerBound = 1;
        int number;
        int previous = 0;
        this.matrix = new int[n][n];
        for(int i=0; i < n; i++){
            for(int c=0; c < n; c++){
                number = rad.nextInt(upperBound-lowerBound)+lowerBound;
                while(number == p || number < previous){
                    number = rad.nextInt(upperBound-lowerBound)+lowerBound;
                }
                previous = number;
                matrix[i][c] = number;
                upperBound++;
                lowerBound++;
            }
            // reset upper and lower bound
            // and previous for next row
            previous = 0;
            upperBound = 8;
            lowerBound = 1;
        }
    }
*/

    /** Method used to generate a random matrix
     *  sorted row-wise to be used to test worst case
     *  of algorithm findElementD1
     *
     * @param p - number excluded from matrix
     */

    public void randomD1(int p){
        // first number is a random one between 0 - 5
        int number = rad.nextInt(5);
        this.matrix = new int[n][n];
        for(int i=0; i < n; i++){
            for(int c=0; c < n; c++){
                // increment number with a random number between 0 - 5
                number += rad.nextInt(5);
                while(number == p ){
                    number += rad.nextInt(5);
                }
                matrix[i][c] = number;
            }
            // reset number to create a matrix
            // sorted only row-wise
            number = rad.nextInt(5);
        }

    }

    /** Method used to generate a random matrix
     *  sorted rows and columns wise
     *  to be used to test worst case
     *  of algorithm findElementD2
     *
     * @param p - number excluded from matrix
     */
    public void randomD2(int p){
        // first number is a random one between 0 - 5
        int number = rad.nextInt(5);
        this.matrix = new int[n][n];
        for(int i=0; i < n; i++){
            for(int c=0; c < n; c++){
                // increment number with a random number between 0 - 5
                number += rad.nextInt(5);
                while(number == p ){
                    number += rad.nextInt(5);
                }
                matrix[i][c] = number;
            }
        }

    }


    public static void main(String[] args){
        int [][] test = {   {1,3,7,8,8,9,12},
                            {2,4,8,9,10,30,38},
                            {4,5,10,20,29,50,60},
                            {8,10,11,30,50,60,61},
                            {11,12,40,80,90,100,111},
                            {13,15,50,100,110,112,120},
                            {22,27,61,112,119,138,153}  };
        Matrix testMatrix = new Matrix(test);
        Matrix aMatrix = new Matrix(5 );
        System.out.println(testMatrix);
        System.out.println(findElementD(testMatrix.matrix,testMatrix.n,153));
        System.out.println(findElementD1(testMatrix.matrix,testMatrix.n,153));
        System.out.println(findElementD2(testMatrix.matrix,testMatrix.n,153));
        aMatrix.randomD2(5);
        System.out.println(aMatrix);
        System.out.println(findElementD1(aMatrix.matrix,aMatrix.n,24));
    }
}


