/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        int row = arr.length;
        int column = arr[0].length;

        System.out.println(arr + " prints:");
        System.out.println("Rows: " + row);
        System.out.println("Columns: " + column);
    }

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int max = arr[0][0];
        for (int k = 0; k < arr.length; k++) {
            for (int j = 0; j < arr[k].length; j++) {
                if (arr[k][j] > max) {
                    max = arr[k][j];
                }
            }
        }
        return max;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int sumRow = 0;
        int[] array = new int [arr.length];
        for (int k = 0; k < arr.length; k++) {
            for (int j = 0; j < arr[k].length; j++) {
                sumRow += arr[k][j];
            }
            array[k] += sumRow;
            sumRow = 0;

        }
        return array;
    }

}