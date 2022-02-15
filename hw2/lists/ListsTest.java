package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** HW2 Problem 1 testing
 *
 *  @author Lyna Jiang
 */

public class ListsTest {

     @Test
     public void testList() {
         int[][] a = {{1,3,7}, {5}, {4, 6, 9, 10}, {10, 11}};
         IntList b = IntList.list(new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11});
         int[][] a1 = {{4, 6, 7}, {3, 7, 9}, {5}};
         IntList b1 = IntList.list(new int[]{4, 6, 7, 3, 7, 9, 5});
         assertEquals(Lists.naturalRuns(b), IntListList.list(a));
         assertEquals(Lists.naturalRuns(b1), IntListList.list(a1));
     }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
