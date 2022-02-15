package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.error;

/** Enigma simulator.
 *  @author Lyna Jiang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        String nextLine = _input.nextLine();
        if (nextLine.charAt(0) != '*') {
            throw new EnigmaException("Invalid start of Input");
        }
        while (_input.hasNextLine()) {
            String setting = nextLine;
            setting = setting.replace("* ", "");
            setUp(enigma, setting);
            nextLine = _input.nextLine();
            while (!nextLine.contains("*")) {
                String messageToConvert = enigma.convert
                        (nextLine.replaceAll(" ", ""));
                if (nextLine.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(messageToConvert);
                }
                if (!_input.hasNextLine()) {
                    nextLine = "*";
                } else {
                    nextLine = _input.nextLine();
                }

            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNextLine()) {
                throw new EnigmaException("No configuration");
            }
            String alphabet = _config.nextLine();
            _alphabet = new Alphabet(alphabet);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of Motors Not Here");
            }
            int numRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Number of Pawls Not Here");
            }
            int numPawls = _config.nextInt();
            tempRotor = _config.next();
            while (_config.hasNext()) {
                nameOfRotor = tempRotor;
                notches = _config.next();
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            perm = "";
            tempRotor = _config.next();
            while ((tempRotor.charAt(0)) == '(' && _config.hasNext()) {
                perm = perm + " " + tempRotor + " ";
                tempRotor = _config.next();
            }
            if (!_config.hasNext()) {
                perm = perm + " " + tempRotor + " ";
            }

            if (notches.charAt(0) == 'M') {
                return new MovingRotor(nameOfRotor,
                        new Permutation(perm, _alphabet), notches.substring(1));
            } else if (notches.charAt(0) == 'N') {
                return new FixedRotor(nameOfRotor,
                        new Permutation(perm, _alphabet));
            } else {
                return new Reflector(nameOfRotor,
                        new Permutation(perm, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] array = settings.split(" ");
        if (array.length - 1 < M.numRotors()) {
            throw new EnigmaException("Not enough info");
        }
        String[] rotors = new String[M.numRotors()];
        System.arraycopy(array, 0, rotors, 0, M.numRotors());
        String settingsOnRotors = array[M.numRotors()];
        String[] permutation;
        String stringPerm = "";
        if (array.length > (M.numRotors() + 1)) {
            permutation = new String[array.length - M.numRotors() - 1];
            System.arraycopy(array, M.numRotors() + 1, permutation,
                    0, permutation.length);
            for (int i = 0; i < permutation.length; i++) {
                stringPerm += permutation[i] + " ";
            }
        }
        if (settingsOnRotors.length() != M.numRotors() - 1) {
            throw new EnigmaException("Wrong info on settings of rotors");
        }

        for (int i = 0; i < rotors.length - 1; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("There are repetitive rotors");
                }
            }
        }
        M.insertRotors(rotors);
        if (!M.returnRotor()[0].reflecting()) {
            throw new EnigmaException("No Reflector");
        }
        int numMovingRotors = 0;
        int numFixedRotors = 0;
        int numReflectors = 0;
        for (int i = 0; i < rotors.length; i++) {
            Rotor lookingAt = M.returnRotor()[i];
            if (lookingAt.rotates()) {
                numMovingRotors += 1;
            } else if (lookingAt.reflecting()) {
                numReflectors += 1;
            } else {
                numFixedRotors += 1;
            }
        }
        if (numMovingRotors != M.numPawls()) {
            throw new EnigmaException("Wrong settings");
        }
        if (numMovingRotors + numFixedRotors != M.numRotors() - 1) {
            throw new EnigmaException("Wrong settings");
        }
        if (numReflectors > 1) {
            throw new EnigmaException("More than one reflector");
        }
        M.setRotors(settingsOnRotors);
        M.setPlugboard(new Permutation(stringPerm, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int wordCount = msg.length();
        for (int i = 0; i < wordCount; i += 5) {
            int limit = wordCount - i;
            if (limit <= 5) {
                _output.println(msg.substring(i, i + limit));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Permutation of Machine. */
    private String perm;

    /** Name of Rotor. */
    private String nameOfRotor;

    /** Notches on Rotor. */
    private String notches;

    /** Temporary placement holder. */
    private String tempRotor;

    /** To input into plugboard permutation. */
    private String plugBoard;

    /** Keep track of all possible rotors. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();


}
