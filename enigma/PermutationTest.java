package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Lyna Jiang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testSize() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(p.size(), 4);

    }
    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(p.permute(0), 2);
        assertEquals(p.permute(10), 3);
        assertEquals(p.permute(11), 1);
        Permutation q = new Permutation("(ECDB) (GF)", new Alphabet("BCDEFG"));
        assertEquals(q.permute(10), 5);
    }

    @Test
    public void testPermuteIntNoPermutation() {
        Permutation p = new Permutation("", new Alphabet("ABCD"));
        assertEquals(p.permute(0), 0);
        assertEquals(p.permute(3), 3);
    }
    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));

        Permutation b = new Permutation("(FJBE)", new Alphabet("ABCDEFGHIJ"));
        assertEquals('B', b.invert('E'));
        assertEquals('J', b.invert('B'));
        assertEquals('E', b.invert('F'));

    }

    @Test
    public void testAlphabet() {
        Alphabet a = new Alphabet("ABCD");
        Alphabet b = new Alphabet("CDEFG");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(p.alphabet(), a);
        assertNotEquals(p.alphabet(), b);
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertTrue(p.derangement());

    }

    @Test(expected = enigma.EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BDCA)", new Alphabet("ABCD"));
        p.invert('M');

    }



}
