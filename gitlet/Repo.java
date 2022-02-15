package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author Lyna Jiang
 */
public class Repo {


    /** Gitlet directory. */
    File GITLET_FILE;

    /** blob folder. */
    private final File blobs = Utils.join(GITLET_FILE, "blobs");

    /** branches folder. */
    private final File branches = Utils.join(GITLET_FILE, "branches");

    /** staging folder. */
    private final File stagingArea = Utils.join(GITLET_FILE, "staging");

    /** commits folder. */
    private final File commits = Utils.join(GITLET_FILE, "commits");

    /** Keeping track of current branch. */
    private final File activeFile = Utils.join(GITLET_FILE, "active");

    /** Working Directory. */
    private final File workingDir = new File(System.getProperty("user.dir"));

    /** Staging area. */
    private Staging _stage;

    /** Length of SHA1Code. */
    static final int SHALENGTH = 40;

    /**
     * All the commands are in this repo.
     */
    public Repo(File file) {
        this.GITLET_FILE = file;
        if (Utils.join(stagingArea, "stage").exists()) {
            _stage = Utils.readObject(Utils.join(stagingArea,
                    "stage"), Staging.class);
        }
    }
    /** Create all the directories, files. */
    public void setUpPersistence() throws IOException {
        GITLET_FILE.mkdir();
        stagingArea.mkdir();
        branches.mkdir();
        blobs.mkdir();
        commits.mkdir();
        activeFile.createNewFile();

    }

    /**
     * Get the current Stage.
     * @return stage
     */
    public Staging getCurrentStage() {
        return _stage;
    }

    /**
     * Get the filepath of the head file.
     * @return headFile
     */
    public File headFile() {
        return Utils.join(branches, "head");
    }

    /**
     * Get the file path of the given branch.
     * @param branch given branch name
     * @return File
     */
    private File getBranchPath(String branch) {
        return Utils.join(branches, branch);
    }

    /**
     * Create a .gitlet directory with stagingArea, pointers.
     * @param args "init"
     * @throws IOException
     */
    public void init(String[] args) throws IOException {
        validateNumArgs("init", args, 1);
        if (GITLET_FILE.exists()) {
            System.out.println("Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        } else {
            setUpPersistence();
            Commit initial = new Commit("initial commit", new HashMap<>(),
                    null);
            Utils.writeObject(Utils.join(commits,
                    initial.getOwnHashCode()), initial);

            File master = getBranchPath("master");
            Utils.writeContents(master, initial.getOwnHashCode());

            Utils.writeContents(activeFile, "master");

            File head = getBranchPath("head");
            Utils.writeContents(head, initial.getOwnHashCode());

            _stage = new Staging();
            Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);


        }

    }

    /**
     * Add file to the stagingArea. If it is on track to be
     * removed, remove it from the removeList.
     * @param args Command expected [add, fileName]
     */
    public void add(String[] args) {
        validateNumArgs("add", args, 2);
        File toAddFile = new File(args[1]);
        if (!toAddFile.exists()) {
            System.out.println("File does not exist.");
        } else {
            byte[] blob = Utils.readContents(toAddFile);
            String addFileHash = Utils.sha1(blob);
            String valueOfFile = getCurrentCommit().getBlobInfo().get(args[1]);

            if (valueOfFile != null) {
                if (addFileHash.equals(valueOfFile)) {
                    if (_stage.getRemovedFiles().contains(args[1])) {
                        _stage.getRemovedFiles().remove(args[1]);
                        Utils.writeObject(Utils.join(stagingArea,
                                "stage"), _stage);
                    }
                    return;
                }
            }

            if (_stage.getRemovedFiles().contains(args[1])) {
                _stage.getRemovedFiles().remove(args[1]);
            }

            Utils.writeContents(Utils.join(blobs, addFileHash), blob);
            _stage.addFile(args[1], addFileHash);
            Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);

        }

    }

    /**
     *
     * @return current commit
     */
    private Commit getCurrentCommit() {
        String currentHashCode = Utils.readContentsAsString(Utils.join
                (branches, "head"));
        return Utils.readObject(Utils.join(commits,
                currentHashCode), Commit.class);
    }

    /**
     *
     * @param args ["commit", fileName]
     */
    public void commit(String[] args) {
        validateNumArgs("commit", args, 2);
        commit(args[1]);
    }

    /**
     *
     * @param args fileName
     */
    private void commit(String args) {

        if (args.equals("")) {
            System.out.print("Please enter a commit message.");
            System.exit(0);
        }


        if (_stage.getAddedFiles().isEmpty()
                && _stage.getRemovedFiles().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        Commit currentCommit = getCurrentCommit();

        HashMap<String, String> toCommitHashMap = new HashMap<>();
        toCommitHashMap.putAll(currentCommit.getBlobInfo());
        ArrayList<String> addFiles = new ArrayList<>(_stage.
                getAddedFiles().keySet());
        for (String file: _stage.getRemovedFiles()) {
            toCommitHashMap.remove(file);
        }
        for (String file: addFiles) {
            toCommitHashMap.put(file, _stage.getAddedFiles().get(file));
        }

        Commit toCommit = new Commit(args, toCommitHashMap,
                currentCommit.getOwnHashCode());
        String newCommitHashCode = toCommit.getOwnHashCode();
        toCommit.setLength(currentCommit.getLength() + 1);

        Utils.writeObject(Utils.join(commits, newCommitHashCode), toCommit);


        Utils.writeContents(getBranchPath("head"),
                newCommitHashCode);


        _stage.clearStage();
        Utils.writeObject(Utils.join(stagingArea, "stage"),
                _stage);

        String currentBranch = Utils.readContentsAsString(activeFile);


        Utils.writeContents(getBranchPath(currentBranch),
                newCommitHashCode);



    }

    /**
     * Method to remove a file from working directory/
     * addition stage.
     * @param args ["rm", fileName]
     */
    public void rm(String[] args) {
        validateNumArgs("rm", args, 2);
        Boolean fileExists = false;
        Commit currentCommit = getCurrentCommit();

        if (_stage.getAddedFiles().containsKey(args[1])) {
            _stage.getAddedFiles().remove(args[1]);
            Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);
            fileExists = true;
        }

        if (currentCommit.getBlobInfo().containsKey(args[1])) {
            _stage.removeFile(args[1]);
            Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);
            Utils.restrictedDelete(Utils.join(workingDir.getPath(), args[1]));
            fileExists = true;
        }
        if (!fileExists) {
            System.out.println("No reason to remove the file.");
        }
    }

    /**
     * List all the commits in branch.
     * @param args ["log"]
     */
    public void log(String[] args) {
        validateNumArgs("log", args, 1);
        Commit currentCommit = getCurrentCommit();
        while (currentCommit != null) {
            outputLog(currentCommit);
            if (currentCommit.getParentHash() != null) {
                currentCommit = Utils.readObject(Utils.join(commits,
                        currentCommit.getParentHash()), Commit.class);
            } else {
                break;
            }
        }
    }

    /**
     * Helper method for log and global-log.
     * @param specificCommit Output that commit
     */
    public void outputLog(Commit specificCommit) {
        System.out.println("===");
        System.out.println("commit " + specificCommit.getOwnHashCode());
        System.out.println("Date: " + specificCommit.getDate());
        System.out.println(specificCommit.getMessage());
        System.out.println();
    }

    /**
     * Print out logs of all branches.
     * @param args ["global-log"]
     */
    public void globalLog(String[] args) {
        validateNumArgs("global-log", args, 1);
        List<String> allCommits = Utils.plainFilenamesIn(commits);
        for (String commitCode : allCommits) {
            Commit currentCommit = Utils.readObject(Utils.join(commits,
                    commitCode), Commit.class);
            outputLog(currentCommit);
        }

    }

    /**
     * Purpose is to find the commit with the particular message.
     * @param args ["find", message]
     */
    public void find(String[] args) {
        validateNumArgs("find", args, 2);
        boolean changes = false;
        for (String commit : Utils.plainFilenamesIn(commits)) {
            File toCheck = Utils.join(commits, commit);
            Commit currentCheck = Utils.readObject(toCheck, Commit.class);
            if (currentCheck.getMessage().equals(args[1])) {
                System.out.println(currentCheck.getOwnHashCode());
                changes = true;
            }
        }
        if (!changes) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     *
     * @param msg Message
     * @return commit of given msg
     */
    private Commit findCommit(String msg) {
        for (String commit : Utils.plainFilenamesIn(commits)) {
            File toCheck = Utils.join(commits, commit);
            Commit currentCheck = Utils.readObject(toCheck, Commit.class);
            if (currentCheck.getMessage().equals(msg)) {
                return currentCheck;
            }
        }
        return null;
    }

    /**
     * Status of the files. Whether or not
     * it is staged, modified, removed, untracked.
     * @param args ["status"]
     */
    public void status(String[] args) {
        validateNumArgs("status", args, 1);
        List<String> workingDirFile = Utils.plainFilenamesIn(
                workingDir.getPath());
        Collections.sort(workingDirFile);
        LinkedHashSet<String> deletedHash = new LinkedHashSet<>();

        System.out.println("=== Branches ===");
        List<String> branchList = Utils.plainFilenamesIn(branches);
        ArrayList<String> branchFiles = new ArrayList<>();
        branchFiles.addAll(branchList);
        branchFiles.remove("head");
        branchFiles.remove(Utils.readContentsAsString(activeFile));
        branchFiles.add("*" + Utils.readContentsAsString(activeFile));
        Collections.sort(branchFiles);
        for (String branchFile : branchFiles) {
            System.out.println(branchFile);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        ArrayList<String> stagedFiles = new ArrayList<>(_stage.getAddedFiles()
                .keySet());
        Collections.sort(stagedFiles);
        if (stagedFiles != null) {
            for (String file : stagedFiles) {
                System.out.println(file);
                if (workingDirFile != null && !workingDirFile.contains(file)) {
                    deletedHash.add(file);
                }
            }
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        ArrayList<String> removedFiles = _stage.getRemovedFiles();
        Collections.sort(removedFiles);
        if (removedFiles != null) {
            for (String file : removedFiles) {
                System.out.println(file);
            }
        }
        System.out.println();

        ecStatus(deletedHash);

    }

    /**
     * Extra Credit Method Status.
     * @param deletedHash To keep track of what happened in status
     */
    private void ecStatus(LinkedHashSet<String> deletedHash) {
        List<String> workingDirFile = Utils.plainFilenamesIn(
                workingDir.getPath());
        Collections.sort(workingDirFile);
        ArrayList<String> currentCommitKeys = new ArrayList<>(
                getCurrentCommit().getBlobInfo().keySet());
        ArrayList<String> untrackedFiles = new ArrayList<>();
        ArrayList<String> modified = new ArrayList<>();
        LinkedHashSet<String> modifiedHash = new LinkedHashSet<>();
        ArrayList<String> deleted = new ArrayList<>();
        ArrayList<String> stagedFiles = new ArrayList<>(
                _stage.getAddedFiles().keySet());
        Collections.sort(stagedFiles);
        ArrayList<String> removedFiles = _stage.getRemovedFiles();
        Collections.sort(removedFiles);
        if (workingDirFile != null) {
            for (String file : workingDirFile) {
                String fileHash = Utils.sha1(Utils.readContents(
                        Utils.join(workingDir, file)));
                if (stagedFiles.contains(file)) {
                    if (!fileHash.equals(_stage.
                            getAddedFiles().get(file))) {
                        modifiedHash.add(file);
                    }
                }
                if (currentCommitKeys.contains(file)) {
                    if (!fileHash.equals(getCurrentCommit()
                            .getBlobInfo().get(file))) {
                        modifiedHash.add(file);
                    }
                }
                if (!stagedFiles.contains(file)
                        && !currentCommitKeys.contains(file)) {
                    untrackedFiles.add(file);
                }
            }
            for (String file : currentCommitKeys) {
                if (!workingDirFile.contains(file)
                        && !removedFiles.contains(file)) {
                    deletedHash.add(file);
                }
            }
        }
        modified.addAll(modifiedHash);
        deleted.addAll(deletedHash);
        Collections.sort(modified);
        Collections.sort(deleted);
        ecStatusModifiedPrint(modified, deleted);
        System.out.println("=== Untracked Files ===");
        if (untrackedFiles != null) {
            for (String file : untrackedFiles) {
                System.out.println(file);
            }
        }
        System.out.println();
    }

    /**
     *
     * @param modified sorted ArrayList of modified files
     * @param deleted sorted ArrayList of deleted files
     */
    private void ecStatusModifiedPrint(ArrayList<String> modified,
                                       ArrayList<String> deleted) {
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String modifiedFile : modified) {
            System.out.println(modifiedFile + " (modified)");
        }

        for (String deletedFile : deleted) {
            System.out.println(deletedFile + " (deleted)");
        }
        System.out.println();

    }
    /**
     * Three different checkout methods.
     * args[2] Checkout branch.
     * args[3] Checkout file in current branch.
     * args[4] Checkout particular file in CommitID.
     * @param args ["checkout", branch, commitID...]
     * @throws IOException
     */
    public void checkout(String[] args) throws IOException {
        if (args.length == 2) {
            if (!getBranchPath(args[1]).exists()) {
                System.out.println("No such branch exists.");
                System.exit(0);
            }
            if (Utils.readContentsAsString(activeFile).equals(args[1])) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            } else {
                checkoutBranch(args[1]);
                Utils.writeContents(headFile(), Utils.readContentsAsString
                        (getBranchPath(args[1])));
                Utils.writeContents(activeFile, args[1]);
                _stage.clearStage();
                Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);
            }
        }
        if (args.length == 3) {
            String headCommitID = Utils.readContentsAsString(headFile());
            checkoutFile(headCommitID, args[2]);
        }
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkoutFile(args[1], args[3]);
        }
    }

    /**
     * Find short UID.
     * @param commitID short UID
     * @return full elngth commitID
     */
    private String findCommitShort(String commitID) {
        List<String> filesOfCommits = Utils.plainFilenamesIn(commits);
        ArrayList<String> shortCommitID = new ArrayList<>();
        for (String file : filesOfCommits) {
            if ((file.substring(0, commitID.length())).equals(commitID)) {
                return file;
            }
        }
        return null;
    }
    /**
     * Helper function for Checkout methods 1 and 2.
     * @param commitID SHA1Code of Commit
     * @param fileName name of file
     */
    private void checkoutFile(String commitID, String fileName) {
        if (commitID.length() < SHALENGTH) {
            commitID = findCommitShort(commitID);
            if (commitID == null) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        }
        File commitPath = Utils.join(commits, commitID);
        if (!commitPath.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit toLookAt = Utils.readObject(commitPath, Commit.class);
        if (!toLookAt.getBlobInfo().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
        } else {
            File pathOfFile = Utils.join(workingDir.getPath(), fileName);
            if (pathOfFile.exists()) {
                Utils.restrictedDelete(pathOfFile);
            }
            String blobID = toLookAt.getBlobInfo().get(fileName);
            File blobContents = Utils.join(blobs, blobID);
            byte[] blobBytes = Utils.readContents(blobContents);
            Utils.writeContents(pathOfFile, blobBytes);
        }


    }

    /**
     * Helper function for checkout method 3.
     * @param branchName name of checkout branch
     * @throws IOException
     */
    private void checkoutBranch(String branchName) throws IOException {
        File branchPath = getBranchPath(branchName);
        String commitID = Utils.readContentsAsString(branchPath);
        File commitFile = Utils.join(commits, commitID);
        checkUntracked(commitFile);
        Commit toLookAt = Utils.readObject(commitFile, Commit.class);
        Utils.writeContents(activeFile, branchName);
        ArrayList<String> listOfFiles = new ArrayList<>(toLookAt.
                getBlobInfo().keySet());
        deleteFiles(commitFile);
        for (String newFile : listOfFiles) {
            String blobID = toLookAt.getBlobInfo().get(newFile);
            byte[] blobBytes = Utils.readContents(Utils.join(blobs, blobID));
            Utils.join(workingDir, newFile).createNewFile();
            Utils.writeContents(Utils.join(workingDir, newFile), blobBytes);
        }


    }

    /**
     * Delete all files in working Directory.
     * @param commitWanted wanted Commit
     */
    private void deleteFiles(File commitWanted) {
        Commit checkoutCommit = Utils.readObject(commitWanted, Commit.class);
        ArrayList<String> files = new ArrayList<>(checkoutCommit.
                getBlobInfo().keySet());
        List<String> filesInDirectory = Utils.plainFilenamesIn(workingDir.
                getPath());
        for (String file : filesInDirectory) {
            if (!checkoutCommit.getBlobInfo().containsKey(file)
                    && getCurrentCommit().getBlobInfo().containsKey(file)) {
                Utils.restrictedDelete(file);
            }
        }
    }

    /**
     * Make a new branch pointer and point it at the current
     * commit. Do NOT checkout (move the head pointer).
     * @param args ["branch", branchName]
     * @throws IOException
     */
    public void branch(String[] args) throws IOException {
        validateNumArgs("branch", args, 2);
        File branch = getBranchPath(args[1]);
        if (!branch.exists()) {
            branch.createNewFile();
            String shaCode = Utils.readContentsAsString(headFile());
            Utils.writeContents(branch, shaCode);
        } else {
            System.out.println("A branch with that name already exists.");
        }

    }

    /**
     * Remove the branch pointer.
     * Do NOT need to get rid of commits.
     * @param args ["rm-branch", branchName]
     */
    public void rmBranch(String[] args) {
        validateNumArgs("rm-branch", args, 2);
        File toDelete = getBranchPath(args[1]);
        if (!toDelete.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        String current = Utils.readContentsAsString(activeFile);
        if (current.equals(args[1])) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else {
            toDelete.deleteOnExit();
        }

    }

    /**
     * Reset to a particular commit.
     * Move the head and current branch pointer to the commit.
     * @param args ["reset", SHA1Code of commit]
     * @throws IOException
     */
    public void reset(String[] args) throws IOException {
        validateNumArgs("reset", args, 2);
        File commitWanted = Utils.join(commits, args[1]);
        if (!commitWanted.exists()) {
            System.out.println("No commit with that id exists");
            System.exit(0);
        }

        checkUntracked(commitWanted);

        Commit checkoutCommit = Utils.readObject(commitWanted, Commit.class);
        ArrayList<String> files = new ArrayList<>(checkoutCommit.
                getBlobInfo().keySet());
        List<String> filesInDirectory = Utils.plainFilenamesIn(workingDir.
                getPath());
        for (String file : filesInDirectory) {
            if (!checkoutCommit.getBlobInfo().containsKey(file)
                    && getCurrentCommit().getBlobInfo().containsKey(file)) {
                Utils.restrictedDelete(file);
            }
        }
        for (String file : files) {
            checkoutFile(args[1], file);
        }


        String currentBranch = Utils.readContentsAsString(activeFile);
        Utils.writeContents(getBranchPath(currentBranch), args[1]);

        _stage.clearStage();
        Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);


        Utils.writeContents(headFile(), args[1]);

        Utils.writeContents(activeFile, currentBranch);
    }

    /**
     * Helper function for reset.
     * @param newCommitName commit needed to check
     */
    private void checkUntracked(File newCommitName) {
        List<String> filesInDirectory = Utils.plainFilenamesIn(
                workingDir.getPath());
        Commit newCommit = Utils.readObject(newCommitName, Commit.class);
        ArrayList<String> newCommitKeys = new ArrayList<>(newCommit.
                getBlobInfo().keySet());
        for (String file : filesInDirectory) {
            if (newCommit.getBlobInfo().containsKey(file)
                    && !getCurrentCommit().getBlobInfo().containsKey(file)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    /** Get current branch name.
     *
     * @return branch name
     */
    private String currentBranch() {
        return Utils.readContentsAsString(activeFile);
    }

    /** check if branch is a ancestor of current branch.
     *
     * @param otherBranch branch to check with
     */
    private void checkIfGivenIsAncestor(String otherBranch) {
        File branchPath = getBranchPath(otherBranch);
        Commit currentCommit = getCurrentCommit();
        String branchCommit = Utils.readContentsAsString(branchPath);
        Commit otherBranchCommit = Utils.readObject(Utils.join(commits,
                branchCommit), Commit.class);
        while (!currentCommit.getOwnHashCode().equals(otherBranchCommit.
                getOwnHashCode())) {
            if (currentCommit.getOwnHashCode().equals(findCommit
                    ("initial commit").getOwnHashCode())) {
                return;
            }
            currentCommit = currentCommit.previousParent(1);
        }

        System.out.println("Given branch is an ancestor "
                + "of the current branch.");
        System.exit(0);
    }

    /**
     * Check if merge causes failures.
     * @param otherBranch other branch name
     * @throws IOException
     */
    private void mergeFailureCases(String otherBranch) throws IOException {
        File branchPath = getBranchPath(otherBranch);


        if (!getCurrentStage().getAddedFiles().isEmpty()
                || !getCurrentStage().getRemovedFiles().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);

        }

        if (!branchPath.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        if (otherBranch.equals(currentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        checkIfGivenIsAncestor(otherBranch);
        File checkCommit = Utils.join(commits,
                Utils.readContentsAsString(branchPath));
        checkUntracked(checkCommit);

    }

    /**
     *
     * @param currentCommit current commit
     * @param otherCommit other commit
     * @return Latest Common Ancestor
     */
    private Commit getSplitPoint(Commit currentCommit, Commit otherCommit) {
        int currentLength = currentCommit.getLength();
        int otherLength = otherCommit.getLength();
        int diff = currentLength - otherLength;
        if (diff > 0) {
            currentCommit = currentCommit.previousParent(diff);
        } else {
            otherCommit = otherCommit.previousParent(-diff);
        }

        while (!currentCommit.getOwnHashCode().
                equals(otherCommit.getOwnHashCode())) {
            currentCommit = currentCommit.previousParent(1);
            otherCommit = otherCommit.previousParent(1);
        }
        return currentCommit;

    }


    /**
     *
     * @param currentCommit current commit
     * @param otherCommit other commit
     * @return HashSet of possible splitPoints
     */
    public HashSet<Commit> getSplitPoints(Commit currentCommit,
                                          Commit otherCommit) {
        HashSet<Commit> allSplitPoints = new HashSet<>();
        Commit knownSP = getSplitPoint(currentCommit, otherCommit);
        allSplitPoints.add(knownSP);
        while (!currentCommit.getOwnHashCode().
                equals(knownSP.getOwnHashCode())) {
            if (currentCommit.getMergedParent() != null) {
                Commit parentCommit = Utils.readObject(Utils.
                        join(commits, currentCommit.getMergedParent()),
                        Commit.class);
                while (!otherCommit.getOwnHashCode().
                        equals(knownSP.getOwnHashCode())) {
                    if (parentCommit.getOwnHashCode().
                            equals(otherCommit.getOwnHashCode())) {
                        allSplitPoints.add(parentCommit);
                    }
                    otherCommit = otherCommit.previousParent(1);
                }
            }
            currentCommit = currentCommit.previousParent(1);
        }


        while (!otherCommit.getOwnHashCode().equals(knownSP.getOwnHashCode())) {
            if (otherCommit.getMergedParent() != null) {
                Commit parentCommit = Utils.readObject(Utils.join(commits,
                        currentCommit.getMergedParent()), Commit.class);
                while (!currentCommit.getOwnHashCode().
                        equals(knownSP.getOwnHashCode())) {
                    if (parentCommit.getOwnHashCode().
                            equals(otherCommit.getOwnHashCode())) {
                        allSplitPoints.add(parentCommit);
                    }
                    currentCommit = otherCommit.previousParent(1);
                }
            }
            otherCommit = otherCommit.previousParent(1);
        }

        return allSplitPoints;
    }

    /**
     * Find the Lowest Common Ancestor.
     * @param allSplitPoints HashSet of all split points
     * @return loweest common ancestor
     */
    private Commit findLCA(HashSet<Commit> allSplitPoints) {
        ArrayList<Commit> allSplitPointList = new ArrayList<>();
        allSplitPointList.addAll(allSplitPoints);
        Collections.sort(allSplitPointList, compareCount);
        return allSplitPointList.get(0);

    }

    /**
     * Compare through the splitpoints hashset.
     */
    private Comparator<Commit> compareCount = new Comparator<Commit>() {

        @Override
        public int compare(Commit o1, Commit o2) {
            return o2.getLength() - o1.getLength();
        }
    };

    /**
     * Merge method.
     * @param args ["merge", branch]
     * @throws IOException
     */
    public void merge(String[] args) throws IOException {
        validateNumArgs("merge", args, 2);
        File otherBranch = getBranchPath(args[1]);
        mergeFailureCases(args[1]);
        Commit currentCommit = getCurrentCommit();
        String sha1codeOther = Utils.readContentsAsString(otherBranch);
        Commit otherCommit = Utils.readObject(Utils.join(
                commits, sha1codeOther), Commit.class);
        Commit splitCommit = findLCA(getSplitPoints(
                currentCommit, otherCommit));
        if (splitCommit.getOwnHashCode().
                equals(currentCommit.getOwnHashCode())) {
            checkoutBranch(args[1]);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        HashMap<String, String> mergeBlob = new HashMap<>();
        HashMap<String, String> currentBlob = currentCommit.getBlobInfo();
        HashMap<String, String> otherBlob = otherCommit.getBlobInfo();
        HashMap<String, String> splitBlob = splitCommit.getBlobInfo();
        HashSet<String> allFiles = new HashSet<>();
        allFiles.addAll(currentBlob.keySet());
        allFiles.addAll(otherBlob.keySet());
        allFiles.addAll(splitBlob.keySet());
        for (String file : allFiles) {
            String otherFileHash = otherBlob.getOrDefault(file, "");
            String currentFileHash = currentBlob.getOrDefault(file, "");
            String splitFileHash = splitBlob.getOrDefault(file, "");
            File currentFile = Utils.join(blobs, currentFileHash);
            File otherFile = Utils.join(blobs, otherFileHash);
            File filePath = Utils.join(workingDir, file);
            if (otherFileHash.equals(currentFileHash)
                    && splitFileHash.equals("")) {
                mergeBlob.put(file, currentFileHash);
            } else if (splitFileHash.equals(currentFileHash)
                    && !otherFileHash.equals(splitFileHash)
                    && !otherFileHash.equals("")) {
                mergeBlob.put(file, otherFileHash);
                _stage.addFile(file, otherFileHash);
                filePath.createNewFile();
                Utils.writeContents(filePath, Utils.readContents(otherFile));
            } else if (currentFileHash.equals(splitFileHash)
                    && otherFileHash.equals("")) {
                Utils.restrictedDelete(Utils.join(workingDir.getPath(), file));
                _stage.removeFile(file);
            } else if (!currentFileHash.equals(otherFileHash)
                    && !currentFileHash.equals(splitFileHash)
                    && !otherFileHash.equals(splitFileHash)) {
                mergeConflict(currentFile, currentFileHash,
                        otherFile, otherFileHash, file);
            }
        }
        String commitMsg = "Merged " + args[1]
                + " into " + currentBranch() + ".";
        mergeCommit(commitMsg, sha1codeOther);
    }

    /**
     * helper function for conflict.
     * @param currentFile filepath for currentBranch
     * @param currentFileHash Sha1code of file
     * @param otherFile filepath for otherBranch
     * @param otherFileHash sha1code of other file
     * @param file name of file
     */
    private void mergeConflict(File currentFile, String currentFileHash,
                               File otherFile, String otherFileHash,
                               String file) {
        System.out.println("Encountered a merge conflict.");
        String currentContent = "";
        String otherContent = "";
        if (currentFile.exists() && currentFileHash.length() > 0) {
            currentContent = Utils.readContentsAsString(currentFile);
        }
        if (otherFile.exists() && otherFileHash.length() > 0) {
            otherContent = Utils.readContentsAsString(otherFile);
        }
        File newFile = Utils.join(workingDir, file);
        String fileContent = "<<<<<<< HEAD\n" + currentContent
                + "=======\n" + otherContent + ">>>>>>>\n";
        Utils.writeContents(newFile, fileContent);
        String newFileHash = Utils.sha1(Utils.readContents(newFile));
        _stage.addFile(file, newFileHash);
        Utils.writeObject(Utils.join(stagingArea, "stage"), _stage);
    }

    /**
     * Commit for merge.
     * @param msg Message
     * @param mergeParent Merge Parent pointer
     */
    private void mergeCommit(String msg, String mergeParent) {
        commit(msg);
        Commit currentCommit = getCurrentCommit();
        currentCommit.changeMergedParent(mergeParent);
        Utils.writeObject(Utils.join(commits, currentCommit.
                getOwnHashCode()), currentCommit);

    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }


}
