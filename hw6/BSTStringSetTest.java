import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet
    @Test
    public void testPut() {
        BSTStringSet testing = new BSTStringSet();
        testing.put("hi");
        testing.put("hola");
        testing.put("abby");
        testing.put("hen");
        ArrayList newList = new ArrayList();
        newList.add("abby");
        newList.add("hen");
        newList.add("hi");
        newList.add("hola");
        String[] strs = {"abby", "hen", "hi", "hola"};
        assertTrue(testing.contains("hen"));
        assertFalse(testing.contains("Lyna"));
        assertEquals(testing.asList(), newList);
    }
    @Test
    public void testDuplicates() {
        BSTStringSet testing = new BSTStringSet();
        testing.put("hi");
        testing.put("hi");
        ArrayList newList = new ArrayList();
        newList.add("hi");
        assertEquals(testing.asList(), newList);
    }
    @Test
    public void testEmpty() {
        BSTStringSet testing = new BSTStringSet();
        ArrayList newList = new ArrayList();
        assertEquals(testing.asList(), newList);
    }
}
