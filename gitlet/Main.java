package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Lyna Jiang
 */
public class Main {
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** Main metadata folder. */
    static final File GITLET_FILE = new File(".gitlet");



    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        Repo theRepo = new Repo(new File(".gitlet"));
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
        case "init":
            theRepo.init(args);
            break;
        case "add":
            theRepo.add(args);
            break;
        case "commit":
            theRepo.commit(args);
            break;
        case "rm":
            theRepo.rm(args);
            break;
        case "log":
            theRepo.log(args);
            break;
        case "global-log":
            theRepo.globalLog(args);
            break;
        case "find":
            theRepo.find(args);
            break;
        case "status":
            if (!GITLET_FILE.exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                System.exit(0);
            }
            theRepo.status(args);
            break;
        case "checkout":
            theRepo.checkout(args);
            break;
        case "branch":
            theRepo.branch(args);
            break;
        case "rm-branch":
            theRepo.rmBranch(args);
            break;
        case "reset":
            theRepo.reset(args);
            break;
        case "merge":
            theRepo.merge(args);
            break;
        default:
            extraCreditMethods(args);
        }
    }

    /**
     *
     * @param args Commands
     * @throws IOException
     */
    public static void extraCreditMethods(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
        case "add-remote":
            EC.addRemote(args);
            break;
        case "rm-remote":
            EC.rmRemote(args);
            break;
        case "push":
            EC.push(args);
            break;
        case "fetch":
            EC.fetch(args);
            break;
        case "pull":
            EC.pull(args);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }


    /**
     * Prints out MESSAGE and exits with error code 0.
     *
     */
    public static void exitIncorrectOperand() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }
}
