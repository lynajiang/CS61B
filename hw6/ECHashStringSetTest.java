import org.checkerframework.checker.fenum.qual.SwingTitlePosition;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testPut() {
        ECHashStringSet hi = new ECHashStringSet(5);
        hi.put("hello");
        hi.put("hi");
        hi.put("anyeong");
        assertTrue(hi.contains("anyeong"));

    }



    @Test
    public void testAsList() {
        ECHashStringSet hi = new ECHashStringSet(5);
        hi.put("hello");
        hi.put("hi");
        hi.put("anyeong");
        ArrayList allHellos = new ArrayList();
        String[] strs = {"anyeong", "hello", "hi"};
        allHellos.addAll(Arrays.asList(strs));
        assertEquals(allHellos, hi.asList());
    }

    @Test
    public void testResize() {
        ECHashStringSet hi = new ECHashStringSet();
        hi.put("hello");
        hi.put("hi");
        hi.put("anyeong");
        
    }
}
