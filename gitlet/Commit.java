package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Serializable method that keeps track of the commit.
 * @author Lyna Jiang
 */
public class Commit implements Serializable {

    /**
     *
     * @param msg Message of commit
     * @param blob Map of txts
     * @param parent sha1code of parent commit
     */
    public Commit(String msg, HashMap<String, String> blob, String parent) {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat(
                "EEE MMM d HH:mm:ss yyyy Z");
        commitDate = outFormat.format(now);
        blobInfo = blob;
        parentHash = parent;
        message = msg;
        hashCode = serializeCommit();
        if (parent == null) {
            length = 0;
        }
    }

    /**
     *
     * @return length from root
     */
    public int getLength() {
        return length;
    }

    /**
     * Set new distance from root.
     * @param newLength new length from root
     */
    public void setLength(int newLength) {
        length = newLength;
    }

    /**
     *
     * @param k number to go back on
     * @return previous parent commit
     */
    public Commit previousParent(int k) {
        Commit commit = this;
        String x = parentHash;
        for (int i = 0; i < k; i++) {
            File parentFile = Utils.join(".gitlet/commits", parentHash);
            commit = Utils.readObject(parentFile, Commit.class);
        }
        return commit;
    }

    /**
     * Take the sha1Code of the commit.
     * @return sha1Code
     */
    public String serializeCommit() {
        byte[] serializedCommit = Utils.serialize(this);
        return Utils.sha1(serializedCommit);
    }

    /**
     *
     * @return commit sha1code
     */
    public String getOwnHashCode() {
        return hashCode;
    }

    /**
     *
     * @return commit message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return parent commit sha1code
     */
    public String getParentHash() {
        return parentHash;
    }

    /**
     *
     * @return commit date
     */
    public String getDate() {
        return commitDate;
    }

    /**
     *
     * @return blob map
     */
    public HashMap<String, String> getBlobInfo() {
        return blobInfo;
    }

    /**
     *
     * @param setString merged parent commit sha1code
     */
    public void changeMergedParent(String setString) {
        mergedParent = setString;
    }

    /**
     *
     * @return merged parent commit sha1code
     */
    public String getMergedParent() {
        return mergedParent;
    }

    /** sha1code of commit. */
    private String hashCode;

    /** message of commit. */
    private String message;

    /** sha1code of parent commit. */
    private String parentHash;

    /** date of commit. */
    private String commitDate;

    /** mergedParent commit. */
    private String mergedParent;

    /** Hashmap of commit info. */
    private HashMap<String, String> blobInfo;

    /** Distance from root. */
    private int length;
}
