package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * To help with the adding, removing files before fully committing them.
 * @author Lyna Jiang
 */
public class Staging implements Serializable {

    /** HashMap to keep track of file addition. */
    private HashMap<String, String> addedFiles;

    /** ArrayList to keep track of file removal. */
    private ArrayList<String> removedFiles;

    /**
     * Staging to keep track of addition and removal of files.
     */
    public Staging() {
        addedFiles = new HashMap<>();
        removedFiles = new ArrayList<>();
    }

    /** Get list of files to add.
     *
     * @return hashMap of fileName string and sha1Code of contents
     */
    public HashMap<String, String> getAddedFiles() {
        return addedFiles;
    }

    /** Get list of files to remove.
     *
     * @return arrayList of fileNames to remove
     */
    public ArrayList<String> getRemovedFiles() {
        return removedFiles;
    }

    /** Add file.
     *
     * @param name name of File
     * @param sha1Code contents of File
     */
    public void addFile(String name, String sha1Code) {
        addedFiles.put(name, sha1Code);
    }

    /** Remove file.
     *
     * @param name name of File
     */
    public void removeFile(String name) {
        removedFiles.add(name);
    }

    /** Clear stage. */
    public void clearStage() {
        addedFiles.clear();
        removedFiles.clear();
    }

}
