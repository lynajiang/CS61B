package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Lyna Jiang
 */
public class AlphabetTest {
    @Test
    public void testContains() {
        Alphabet a = new Alphabet("ABCD");
        assertFalse(a.contains('E'));
        assertTrue(a.contains('A'));
    }

    @Test
    public void testToChar() {
        Alphabet a = new Alphabet("ABCD");
        assertEquals(a.toChar(0), 'A');
        assertEquals(a.toChar(3), 'D');

    }

    @Test
    public void testToInt() {
        Alphabet a = new Alphabet("ABCD");
        assertEquals(a.toInt('A'), 0);
        assertEquals(a.toInt('D'), 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToCharException() {
        Alphabet a = new Alphabet("ABCD");
        a.toChar(-1);
        a.toChar(5);
    }

    @Test(expected = EnigmaException.class)
    public void testToIntException() {
        Alphabet a = new Alphabet("ABCD");
        a.toInt('F');
        a.toInt('Z');

    }
}
