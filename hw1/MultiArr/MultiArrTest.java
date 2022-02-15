import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {{1, 3, 3, 6},{5, 3, 1, 5}};
        int[][] arr1 = {{-5, -8, -9, -3}, {-10, -11, -29, -40}};
        int[][] arr2 = {{1,4,2,5,3}, {3,5,3}};
        assertEquals(MultiArr.maxValue(arr), 6);
        assertEquals(MultiArr.maxValue(arr1), -3);
        assertEquals(MultiArr.maxValue(arr2), 5);
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = {{1, 3, 3, 6},{5, 3, 1, 5}};
        int[] arrSum = {13,14};
        int[][] arr1 = {{-5, -8, -9, -3}, {-10, -11, -29, -40}};
        int[] arrSum1 = {-25, -90};
        assertArrayEquals(MultiArr.allRowSums(arr), arrSum);
        assertArrayEquals(MultiArr.allRowSums(arr1), arrSum1);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
