import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @Lyna Jiang
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, HW0.max((new int[] { 0, -5, 2, 14, 10 })));
        assertEquals(81, HW0.max((new int[] {0, -18, 3, 6, 54, 81})));
        assertEquals(-5, HW0.max((new int[] {-5, -10, -100, -57})));
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(HW0.threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(HW0.threeSum(new int[] {8, 2, -1, -1, 15}));
        assertFalse(HW0.threeSum(new int[] {-6,2,5}));

    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(HW0.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertFalse(HW0.threeSumDistinct(new int[] {0, 0, 2, 3}));
        assertTrue(HW0.threeSumDistinct(new int[]{8, 2, -1, -1, 15}));
        assertTrue(HW0.threeSumDistinct(new int[]{-6, 2, 4}));
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
