package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.TestUtils.UPPER;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the Machine class.
 *  @author Lyna Jiang
 */

public class MachineTest {


    private Machine machine;

    private String[] input = {"B", "BETA", "III", "IV", "I"};

    private ArrayList<Rotor> rotorsInMachine = TestUtils.ROTORS;

    private void newMachine(Alphabet alpha, int numrotors,
                            int numpawls, Collection<Rotor> allRotors) {
        machine = new Machine(alpha, numrotors, numpawls, allRotors);
    }

    @Test
    public void testInsertRotors() {
        /**Set<String> keySet = rotors.keySet();
         Collection<Rotor> rotors =

         ArrayList<String> listOfRotors = new ArrayList<String>(keySet);

         Collection<String> values = rotors.values();

         ArrayList<String> listOfValues = new ArrayList<>(values);


         for (String keySet)
         *
         */


        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        assertEquals("Rotor at 1 is Incorrect", rotorsInMachine.get(1),
                machine.returnRotor()[1]);
        assertEquals("Rotor 5 is incorrect", rotorsInMachine.get(4),
                machine.returnRotor()[2]);
    }

    @Test(expected = EnigmaException.class)
    public void testInsertRotorsGoesWrong() {
        String[] wrongInput = {"A", "BETA", "III"};
        newMachine(UPPER, 5, 2, rotorsInMachine);
        machine.insertRotors(wrongInput);
    }


    @Test
    public void testSetRotors() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("LYNA");
        assertEquals("Wrong at 1", 11, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 24, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 13, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 0, machine.returnRotor()[4].setting());



    }

    @Test
    public void testSetRotors2() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("AAAA");
    }

    @Test(expected = EnigmaException.class)
    public void testSetRotorsGoesWrongBIG() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("LYNAMN");
    }

    @Test(expected = EnigmaException.class)
    public void testSetRotorsGoesWrongLITTLE() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("LYN");

    }

    @Test(expected = EnigmaException.class)
    public void testSetRotorsGoesWrongAlpha() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("LYN-");

    }
    @Test
    public void testRotors() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("AXLE");
        machine.setPlugboard(new Permutation("(FY) (ZH)", UPPER));
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 23, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 11, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 4, machine.returnRotor()[4].setting());
        machine.convert("Y");
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 23, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 11, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 5, machine.returnRotor()[4].setting());

        String[] array = {"B", "BETA", "I", "II", "III"};

        machine.insertRotors(array);
        machine.setRotors("AAAA");
        machine.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 0, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 0, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 0, machine.returnRotor()[4].setting());
        machine.convert("H");
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 0, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 0, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 1, machine.returnRotor()[4].setting());
        machine.convert("E");
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 0, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 0, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 2, machine.returnRotor()[4].setting());
        machine.convert("LL");
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 0, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 0, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 4, machine.returnRotor()[4].setting());
        machine.convert("O");
        assertEquals("Wrong at 1", 0, machine.returnRotor()[1].setting());
        assertEquals("Wrong at 2", 0, machine.returnRotor()[2].setting());
        assertEquals("Wrong at 3", 0, machine.returnRotor()[3].setting());
        assertEquals("Wrong at 4", 5, machine.returnRotor()[4].setting());



    }

    @Test
    public void testConvert() {
        Permutation p = new Permutation("(GF) (RW) (TQ) (YD) (AJ)", UPPER);
        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("AXLE");
        machine.setPlugboard(new Permutation("(FY) (ZH)", UPPER));
        assertEquals("Wrong convert", "Z", machine.convert("Y"));

        newMachine(UPPER, 5, 3, rotorsInMachine);
        machine.insertRotors(input);
        machine.setRotors("AXLE");
        machine.setPlugboard(new Permutation("(HQ) (EX) (IP)"
                + " (TR) (BY)", UPPER));
        assertEquals("Wrong convert", "QVPQ", machine.convert("FROM"));

        newMachine(UPPER, 5, 3, rotorsInMachine);
        String[] array = {"B", "BETA", "I", "II", "III"};

        machine.insertRotors(array);
        machine.setRotors("AAAA");
        machine.setPlugboard(new Permutation("", UPPER));
        assertEquals("Wrong convert", "HELLOWORLD",
                machine.convert("ILBDAAMTAZ"));

        machine.insertRotors(array);
        machine.setRotors("AAAA");
        machine.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("Wrong convert", "HELLOWORLD",
                machine.convert("IHBDQQMTQZ"));

        machine.insertRotors(array);
        machine.setRotors("AAAA");
        machine.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("Wrong convert", "IHBDQQMTQZ",
                machine.convert("HELLOWORLD"));


    }

    @Test
    public void testRotorV() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        String[] array2 = {"B", "BETA", "III", "II", "V"};
        machine.insertRotors(array2);
        machine.setRotors("AAAZ");
        machine.setPlugboard(new Permutation("", UPPER));
        assertEquals("Wrong convert",
                "TIKZNFNKZPPBSILUZJODJHHQNIVV",
                machine.convert("AAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
    }

    @Test
    public void testRotorVSmall() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        String[] array2 = {"B", "BETA", "III", "II", "V"};
        machine.insertRotors(array2);
        machine.setRotors("AAAZ");
        machine.setPlugboard(new Permutation("", UPPER));
        assertEquals("Wrong convert", "T",
                machine.convert("A"));
        assertEquals("Wrong convert", "I",
                machine.convert("A"));
        assertEquals("Wrong convert", "K",
                machine.convert("A"));
        assertEquals("Wrong convert", "Z",
                machine.convert("A"));

    }
    @Test
    public void testStepIn() {
        newMachine(UPPER, 5, 3, rotorsInMachine);
        String[] array3 = {"B", "BETA", "III", "II", "I"};
        machine.insertRotors(array3);
        machine.setRotors("AAEA");
        machine.setPlugboard(new Permutation("", UPPER));
        assertEquals("Wrong convert", "J",
                machine.convert("A"));

    }


    @Test
    public void testWrongNumberSettings() {
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
