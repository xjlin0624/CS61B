package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class does at a high level.
 *
 *  @author Joanna Lin, Lulu Wu
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet, blobs, refs directories. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    /** Variables that keep track of persistence across the repository. */
    private String HEAD;
    private String curBranch;
    private Set<String> to_add;
    private Set<String> removed;

    /* TODO: fill in the rest of this class. */

    /** Constructor: creates an empty / THE initial repository object. */
    public Repository() {
        HEAD = null;
        curBranch = null;
        to_add = new HashSet<>();
        removed = new HashSet<>();
    }


    /**
     * The init command.
     * First checks if there already exists a version-control system and print error message accordingly.
     * If no such directories exists, sets up the directories, creates an repo object that will
     * keep track of the state across everything, and make the initial commit.
     * Initializes the "master" branch. Serializes the initial commit to .gitlet,
     * and points the HEAD to this commit. Save a txt file with the created branch as its
     * file name, and the SHA-1 id of its head commit as its file content.
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        setupDir();
        Repository repo = new Repository();
        Commit initial = new Commit();
        initial.saveCommit();
        repo.HEAD = sha1(serialize(initial));
        repo.curBranch = "master";
        initial.setBranch(repo.curBranch);
        repo.saveBranch(repo.curBranch, repo.HEAD);
        repo.saveRepo();
    }

    /** Helper method: serializes and writes repository object to .gitlet directory. */
    public void saveRepo() {
        File repo = join(GITLET_DIR, "repository");
        writeObject(repo, this);
    }

    /** Helper method: deserializes and returns repository object from .gitlet directory. */
    public static Repository fromFile() {
        File repo = join(GITLET_DIR, "repository");
        if (!repo.exists()) {
            return null;
        }
        return readObject(repo, Repository.class);
    }

    /** Helper method: creates the directories for gitlet version-control system. */
    public static void setupDir() {
        GITLET_DIR.mkdir();
        Commit.COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        REFS_DIR.mkdir();
    }

    /**
     * Helper method: writes the id of the head of given branch onto a txt file,
     * saved in .gitlet/refs directory; if file with given branch name already exists,
     * clear content and write over it (no extra code needed).
     */
    public void saveBranch(String brInfo, String id) {
        File br = join(REFS_DIR, brInfo);
        writeContents(br, id);
    }


    /**
     * Creates and persistently saves a blob
     * If the user inputs a file not found, prints error message
     * @param args Array in format: {'add', [file name]}
     */
    public void add(String[] args) {
        if (validateNumArgs(args, 2)) {
            return;
        }
        String name = args[1];
        File file = join(CWD, name);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Commit h = Commit.fromFile(HEAD);
        byte[] blob = readContents(file);
        if (!isSame(name, h, blob)) { // Checks if its the same version
            saveBlob(blob); // new blob
            to_add.add(name);
        }
        if (removed.contains(name)) {
            removed.remove(name);
        }
    }

    /**
     * Helper method: returns true if the commit has a same entry of blob
     * as the passed in one; returns false otherwise.
     */
    public boolean isSame(String name, Commit h, byte[] b) {
        HashMap blobs = h.getBlobs();
        if (blobs.containsKey(name)) {
            return blobs.get(name).equals(sha1(b));
        }
        return false;
    }

    /** Get sha1 of file content, save the name of blob as sha1, content as byte array*/
    public void saveBlob(byte[] blob) {
        String id = sha1(blob);
        File bfile = join(BLOB_DIR, id);
        writeContents(bfile, blob);
    }


    /**
     * Clone the HEAD
     * change parent to HEAD
     * Modify its message and date according to user input
     * Use the staging area to modify the files tracked by new commit
     * Write back any new object made or any modified objects read earlier
     * reassign HEAD
     */
    public void commit(String[] args) {
        if (args.length == 1 || args[1].equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if(validateNumArgs(args, 2)){
            return;
        }
        if (to_add.isEmpty() && removed.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit c = Commit.fromFile(HEAD);
        c.setMessage(args[1]);
        c.setDate();
        c.setParent(HEAD);
        c.setBranch(curBranch);
        HashMap<String, String> blobs = c.getBlobs();
        for (String f : to_add) {
            File file = join(CWD,f);
            byte[] b = readContents(file);
            blobs.put(f, sha1(b));
        }
        c.setBlobs(blobs);
        c.saveCommit();
        HEAD = sha1(serialize(c));
        to_add = new HashSet<>();
        removed = new HashSet<>();
        saveBranch(curBranch, HEAD);
    }


    /**
     * Returns a log with all commits in the current branch, with the most recent commit
     * on the top; it prints the commit + its id, the date, and the commit message;
     * each commit print block starts with === and ends with \n
     */
    public void log() {
        Commit h = Commit.fromFile(HEAD);
        logRecurse(h);
    }

    /**
     * Helper method: recurses through all the commit following the current branch to
     * the initial commit. Uses a helper print method for formatting.
     */
    public void logRecurse(Commit commit) {
        logPrint(commit);
        if (commit.getParent() == null) {
            return;
        }
        logRecurse(Commit.fromFile(commit.getParent()));
    }

    /** Helper method: prints out the log for each commit including the commit id. date, and commit message. */
    public void logPrint(Commit commit) {
        String id = sha1(serialize(commit));
        System.out.println("===");
        System.out.println((String.format("commit %s", id)));
        // prints the date with given format
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String date = formatter.format(commit.getDate());
        System.out.println((String.format("Date: %s", date)));
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public void globalLog() {
        List<String> commits = plainFilenamesIn(Commit.COMMIT_DIR);
        for (String file : commits) {
            Commit c = Commit.fromFile(file);
            logPrint(c);
        }
    }

    public void find(String[] args) {
        if (validateNumArgs(args, 2)) {
            return;
        }
        List<String> commits = plainFilenamesIn(Commit.COMMIT_DIR);
        int ct = 0;
        for (String file : commits) {
            Commit c = Commit.fromFile(file);
            if (c.getMessage().equals(args[1])) {
                String id = sha1(serialize(c));
                System.out.println(id);
                ct++;
            }
        }
        if (ct == 0) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        System.out.println("*" + curBranch);
        List<String> branches = new ArrayList<>(plainFilenamesIn(REFS_DIR));
        branches.remove(curBranch);
        for (String b : branches){
            System.out.println(b);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> added = new ArrayList<>(to_add);
        Collections.sort(added);
        for (String file : added) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        List<String> rm = new ArrayList<>(removed);
        Collections.sort(rm);
        for (String file : rm) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ==="); //optional
        System.out.println();
        System.out.println("=== Untracked Files ==="); //optional
        System.out.println();
    }


    /** */
    public void checkout(String[] args) {
        if (args.length == 3 && args[1].equals("--")) {
            checkoutFile(args[2]);
        } else if (args.length == 4 && args[2].equals("--")) {
            checkoutID(args[1], args[3]);
        } else if (args.length == 2) {
            checkoutBranch(args[1]);
        } else {
            System.out.println("Incorrect operands.");
            return;
        }
    }

    /** */
    public void checkoutFile(String file) {
        Commit h = Commit.fromFile(HEAD);
        HashMap<String, String> blobs = h.getBlobs();
        if (!blobs.containsKey(file)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String id = blobs.get(file);
        byte[] revert = Commit.blobFromFile(id);
        File f = new File(file);
        writeContents(f, revert);
    }

    /** */
    public void checkoutID(String id, String file) { // if length <10 do short uid
        boolean check;
        if (id.length()>10) {
            File commit = join(Commit.COMMIT_DIR, id);
            check = commit.exists();
        } else {
            int len = id.length();
            List<String> names = plainFilenamesIn(Commit.COMMIT_DIR);
            check = false;
            for (String n : names){
                check = id.equals(n.substring(0, len));
                if(check){
                    id = n;
                    break;
                }
            }
        }
        if (!check) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit c = Commit.fromFile(id);
        HashMap<String, String> blobs = c.getBlobs();
        if (!blobs.containsKey(file)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String blobID = blobs.get(file);
        byte[] revert = Commit.blobFromFile(blobID);
        File f = new File(file);
        writeContents(f, revert);
    }

    /** */
    public void checkoutBranch(String branch) {
        File check = join(REFS_DIR, branch);
        if (!check.exists() || readContentsAsString(check).equals("")) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branch.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        List<String> onDisk = plainFilenamesIn(CWD);
        Commit pastHEAD = Commit.fromFile(HEAD);
        List<String> files = new ArrayList<>(pastHEAD.getBlobs().keySet());
        String newH = readContentsAsString(check);
        Commit newHEAD = Commit.fromFile(newH);
        HashMap<String, String> newBlobs = newHEAD.getBlobs();
        if (files != null) {
            if (isTracked(HEAD, newBlobs, onDisk)) {
                return;
            }
        }
        HEAD = newH; //new branch head
        curBranch = branch;
        updateCWD(files, newBlobs);
        to_add = new HashSet<>();
        removed = new HashSet<>();
    }

    /** */
    public boolean isTracked(String HEAD, HashMap<String, String> newBlobs, List<String> files) {
        Commit h = Commit.fromFile(HEAD);
        HashMap<String, String> blobs = h.getBlobs();
        for (String f : files) {
            if (!blobs.containsKey(f) && newBlobs.containsKey(f)) {
                System.out.println("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
                return true;
            }
        }
        return false;
    }


    /** */
    public void updateCWD(List<String> files, HashMap<String, String> newBlobs) {
        if (files != null) {
            for (String f : files) {
                if (!newBlobs.containsKey(f)) {
                    restrictedDelete(f);
                }
            }
        }
        Set<String> keys = newBlobs.keySet();
        for (String key : keys) {
            File file = join(CWD, key);
            String blob = newBlobs.get(key);
            File update = join(BLOB_DIR, blob);
            writeContents(file, readContentsAsString(update));
        }
    }


    /** */
    public void rm(String[] args) {
        if(validateNumArgs(args, 2)){
            return;
        }
        String name = args[1];
        Commit h = Commit.fromFile(HEAD);
        HashMap<String, String> blobs = h.getBlobs();
        if (!blobs.containsKey(name) && !to_add.contains(name)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        if (blobs.containsKey(name)) {
            restrictedDelete(name);
            blobs.remove(name);
            removed.add(name);
        }
        to_add.remove(name);
    }


    /** */
    public void branch(String[] args) {
        if(validateNumArgs(args, 2)){
            return;
        }
        File check = join(REFS_DIR, args[1]);
        if (check.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        saveBranch(args[1], HEAD);
    }


    /** */
    public void rmBranch(String[] args) {
        if(validateNumArgs(args, 2)){
            return;
        }
        File check = join(REFS_DIR, args[1]);
        if (!check.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (args[1].equals(curBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        writeContents(check, "");
    }


    /**
     * Checks out all the files tracked by the given commit. Removes tracked files
     * that are not present in that commit. Also moves the current branch’s head
     * to that commit node. See the intro for an example of what happens to the head pointer
     * after using reset. The [commit id] may be abbreviated as for checkout.
     * The staging area is cleared. The command is essentially checkout of
     * an arbitrary commit that also changes the current branch head.
     */
    public void reset(String[] args) {
        if(validateNumArgs(args, 2)){
            return;
        }
        String id = args[1];
        File check = join(Commit.COMMIT_DIR, id);
        if (!check.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        List<String> files = plainFilenamesIn(CWD);

        Commit newHEAD = Commit.fromFile(id);
        HashMap<String, String> newBlobs = newHEAD.getBlobs();
        if (files != null) {
            if (isTracked(HEAD, newBlobs, files)) {
                return;
            }
        }
        updateCWD(files, newBlobs);
        String br = newHEAD.getBranch();
        curBranch = br;
        HEAD = id;
        saveBranch(br, id);
        to_add = new HashSet<>();
        removed = new HashSet<>();
    }




    /**
     * Checks the number of arguments versus the expected number,
     * If false, prints "Incorrect operands." and return true;
     *
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static boolean validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            return true;
        }
        return false;
    }


}
