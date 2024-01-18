package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Repository.BLOB_DIR;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class does at a high level.
 *
 *  @author Joanna Lin, Lulu Wu
 */

public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    public static final File COMMIT_DIR = join(Repository.GITLET_DIR, "commits");

    /** The message, timestamp, parent and blobs of this Commit. */
    private String message;
    private Date date;
    private String parent;
    private String branch;
    private HashMap<String, String> blobs; //file name(string) mapped to a blob (byte[])

    /* TODO: fill in the rest of this class. */

    /**
     * Creates the initial commit with "initial commit" message, the current date, and null as the parent commit
     * point HEAD and branch to the commit.
     */

    public Commit() {
        this.message = "initial commit";
        this.date = new Date(0); // UTC time
        this.parent = null;
        this.branch = null;
        this.blobs = new HashMap<>();
    }


    /**
     * Getter methods: return the private variables of Commit instances
     * for later use in call to gitlet.Main log.
     */
    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getParent() {
        return parent;
    }

    public String getBranch() { return branch; }

    public HashMap<String, String> getBlobs() { // FIXME: don't know yet if returning the whole map or just the keys / just the values
        return blobs;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate() {
        this.date = new Date();
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setBranch(String branch) { this.branch = branch; }

    public void setBlobs(HashMap<String, String> blobs) {
        this.blobs = blobs;
    }

    /**
     * Serialization helper method:
     * serializes and writes repository object to .gitlet/commits directory.
     */
    public void saveCommit() {
        String id = sha1(serialize(this));
        File commit = join(COMMIT_DIR, id);
        writeObject(commit, this);
    }

    /**
     * Serialization helper method:
     * deserializes and returns repository object from .gitlet/commits directory.
     */
    public static Commit fromFile(String id) {
        File commit = join(COMMIT_DIR, id);
        return readObject(commit, Commit.class);
    }

    /**
     * Serialization helper method:
     * deserializes and returns repository object from .gitlet/commits directory.
     */
    public static byte[] blobFromFile(String id) {
        File blob = join(BLOB_DIR, id);
        return readContents(blob);
    }

}
