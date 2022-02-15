package enigma;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Lyna Jiang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {

        _alphabet = alphabet;
        _cyclesInString = "";
        String temp = cycles;
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(temp);
        while (m.find()) {
            _cyclesInString = _cyclesInString.concat(m.group(1) + " ");
        }
        _cyclesInString = _cyclesInString.trim();
        _cycles = _cyclesInString.split(" ");





    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] tempArray = new String[_cycles.length + 1];
        System.arraycopy(_cycles, 0, tempArray, 0, _cycles.length);
        tempArray[_cycles.length + 1] = cycle;
        _cycles = tempArray;

    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int lookingForChar = wrap(p);
        char letter = _alphabet.toChar(lookingForChar);
        char newLetter;

        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length(); j++) {
                if (_cycles[i].charAt(j) == letter) {
                    newLetter = _cycles[i].charAt(mod((j + 1),
                            _cycles[i].length()));
                    return _alphabet.toInt(newLetter);
                }

            }

        }

        return p;
    }

    /** Returns the mod function given two inputs.
     * @param num the num needed to modulo.
     * @param size the size that is the modulus.
     * */
    final int mod(int num, int size) {
        int r = num % size;
        if (r < 0) {
            r += size;
        }
        return r;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int intOfChar = wrap(c);
        char letter = _alphabet.toChar(intOfChar);
        char newLetter;

        for (int i = 0; i < _cycles.length; i++) {
            for (int j = 0; j < _cycles[i].length(); j++) {
                if (_cycles[i].charAt(j) == letter) {
                    newLetter = _cycles[i].charAt(mod((j - 1),
                            _cycles[i].length()));
                    return _alphabet.toInt(newLetter);
                }


            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(
                permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {

        return _cyclesInString.length() == _alphabet.size();


    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Array of strings, each string is a cycle. */
    private String[] _cycles;

    /** Cycle in String. */
    private String _cyclesInString;

}
