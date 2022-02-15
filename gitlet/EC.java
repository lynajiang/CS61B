package gitlet;

import java.io.File;
import java.io.IOException;

public class EC {
    /**
     * Extra Credit Method.
     * Add file remotely.
     * @param args ["add-remote", fileName]
     */
    public static void addRemote(String[] args) throws IOException {
        validateNumArgs("add-remote", args, 3);
        String dir = args[2];
        String dirName = dir.replace(".gitlet", "");
        File remote = new File(args[1]);
        String remoteName = args[1];

        if (remote.exists()) {
            System.out.println("A remote with that name already exists.");
            System.exit(0);
        } else {
            File fileToLookAt = new File(args[2]);
            Repo newRepo = new Repo(fileToLookAt);
            newRepo.init(new String[] {"init"});
        }




    }

    /**
     * Extra Credit Method.
     * Remove file remotely.
     * @param args ["rm-remote", fileName]
     */
    public static void rmRemote(String[] args) {
        validateNumArgs("rm-remote", args, 2);
        File remote = new File(args[1]);
        if (!remote.exists()) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        } else {
            Utils.restrictedDelete(remote);
        }

    }

    /**
     * Extra Credit Method.
     *
     * @param args ["push"]
     */
    public static void push(String[] args) {

    }

    /**
     * Extra Credit Method.
     * @param args ["fetch", fileName]
     */
    public static void fetch(String[] args) {


    }

    /**
     * Extra Credit Method.
     * @param args ["pull"]
     */
    public static void pull(String[] args) {

    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
