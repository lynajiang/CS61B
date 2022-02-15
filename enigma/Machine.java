package enigma;

import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Lyna Jiang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        if (numRotors <= pawls || 1 > numRotors) {
            throw new EnigmaException("Rotor count off");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors.toArray();
        _rotors = new Rotor[numRotors];


    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != _rotors.length) {
            throw new EnigmaException("Incorrect name of rotor");
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.length; j++) {
                String nameOfRotor = ((Rotor) _allRotors[j]).name();
                if (rotors[i].equals(nameOfRotor)) {
                    _rotors[i] = ((Rotor) _allRotors[j]);

                }
            }
        }




    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Wrong number of settings");
        }
        for (int i = 1; i < setting.length() + 1; i++) {
            char letter = setting.charAt(i - 1);
            if (!_alphabet.contains(letter)) {
                throw new EnigmaException("Char does not exist in Alphabet");
            }
            _rotors[i].set(setting.charAt(i - 1));
        }



    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] rotorMobility = new boolean[_numRotors];
        int newInt = _plugBoard.permute(c);
        for (int i = 0; i < _numRotors; i++) {

            if ((i == _numRotors - 1) || (_rotors[i + 1].atNotch()
                    && _rotors[i].rotates())) {
                rotorMobility[i] = true;
            } else {
                rotorMobility[i] = false;
            }
        }
        for (int i = 0; i < _numRotors; i++) {
            if (rotorMobility[i]) {
                _rotors[i].advance();
                if (i < _numRotors - 1) {
                    _rotors[i + 1].advance();
                    i += 1;
                }
            }
        }
        for (int i = _numRotors - 1; i >= 0; i--) {
            newInt = _rotors[i].convertForward(newInt);

        }
        for (int j = 1; j < _numRotors; j++) {
            newInt = _rotors[j].convertBackward(newInt);
        }
        newInt = _plugBoard.invert(newInt);
        return newInt;



    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String resultMsg = "";
        for (int i = 0; i < msg.length(); i++) {
            if (!_alphabet.contains(msg.charAt(i))) {
                throw new EnigmaException("Machine does not recognize the msg");
            }

            resultMsg += _alphabet.toChar
                    (convert(_alphabet.toInt(msg.charAt(i))));
        }

        return resultMsg;
    }

    /**
     *
     * Returns the array of Rotors.
     */
    Rotor[] returnRotor() {
        return _rotors;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Total number of Rotors. */
    private int _numRotors;

    /** Total number of pawls. */
    private int _numPawls;

    /** Array of possible rotors. */
    private Object[] _allRotors;

    /** Keeps track of all rotors in Enigma. */
    private Rotor[] _rotors;

    /** Keeps plugboard. */
    private Permutation _plugBoard;
}
