import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        ArrayList<int[]> node = new ArrayList<>();
        int total = 0;
        for (int i = 0; i < intervals.size(); i++) {
            node.add(new int[] {intervals.get(i)[0], 1});
            node.add(new int[] {intervals.get(i)[1], -1});
        }

        Collections.sort(node, new Comparator<int[]>() {

            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] == o2[0]) {
                    return o2[1];
                }
                else {
                    return o1[0] - o2[0];
                }
            }
        });
//        int first = 0;
//        for (int i = 0; i < node.size(); i++) {
//            first = node.get(i)[0];
//            total += node.get(i)[0] - first;
//        }
//        return total;

        int first = 0, test = 0;
        for (int i = 0; i < node.size(); i++) {
            if (test == 0) {
                first = node.get(i)[0];
            }
            test += node.get(i)[1];
            if(test == 0) {
                total += node.get(i)[0] - first;
            }
        }

        return total;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
