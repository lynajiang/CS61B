package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Lyna Jiang
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }
    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));

        Permutation b = getNewPermutation("(FJBE)", getNewAlphabet("FJEB"));
        assertEquals('B', b.invert('E'));

    }
    @Test
    public void testInvertInt() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(p.invert(1), 3);

    }
    @Test(expected = enigma.EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(ABCD)", getNewAlphabet("BACD"));
        p.invert('M');
        p.invert(5);


    }


    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(p.size(), 4);
    }

    @Test
    public void testPermuteEffect() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(p.permute(10), 3);
        assertEquals(p.permute(11), 1);
        assertEquals(p.permute(0), 2);

        Permutation q = getNewPermutation("(ECDB) (GF)", getNewAlphabet("BCDEFG"));
        assertEquals(q.permute(10), 5);

    }





    @Test(expected = EnigmaException.class)
    public void testPermuteInt2() {
        Permutation p = getNewPermutation("(GHFE)", getNewAlphabet("ABCD"));
        p.permute(0);
    }


    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(p.permute('B'), 'A');
        assertNotEquals(p.permute('C'), 'A');
    }
    @Test(expected = EnigmaException.class)
    public void testPermuteChar1() {
        Permutation p = getNewPermutation("(BABD)", getNewAlphabet("BBD"));
        p.permute('B');
    }

    @Test(expected = EnigmaException.class)
    public void testPermuteChar2() {
        Permutation p = getNewPermutation("(BABD)", getNewAlphabet("BBD"));
        p.permute('B');
    }
    @Test
    public void testAlphabet() {
        Alphabet a = getNewAlphabet("ABCD");
        Alphabet b = getNewAlphabet("DCEG");
        Permutation p = getNewPermutation("(BACD)", a);
        assertEquals(p.alphabet(), a);
        assertNotEquals(p.alphabet(), b);
    }

    @Test
    public void testDerangement() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertTrue(p.derangement());

    }



    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.
}
