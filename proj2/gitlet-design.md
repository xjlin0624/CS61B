# Gitlet Design Document

**Name**: Joanna Lin, Lulu Wu
**Last Edited**: Jul 13, 2021 by Joanna + Lulu

## Classes and Data Structures

### Main (copied from capers, prob need to reword)

The entry point to the program. It takes in arguments from the command line and based on the command (the first element of the `args` array) calls the corresponding command in `Repository` which will actually execute the logic of the command. It also validates the arguments based on the command to ensure that enough arguments were passed in.

#### Fields

This class has no fields and hence no associated state: it simply validates arguments and defers the execution to the `Repository` class.


### Repository

This class will handle all of the actual gitlet commands by setting up persistence, which includes creating the `.gitlet` folder as well as all the files and directories within; reading/writing from/to the correct file according to the commands; and additional error checking.

There will be one `Repository` object saved in `.gitlet` and getting updated everytime a gitlet command is called. 

The `Repository` class will also defers all `Commit` specific logic to `Commit` class. For example, instead of having the `Repository` class handle `Commit` serialization and deserialization, we will have the `Commit` class contain the logic for that. 

#### Fields

1. `static final File GITLET_FOLDER = Utils.join(CWD, ".gitlet")`
2. `static final File BLOB_FOLDER = Utils.join(GITLET_FOLDER, "blobs")`
2. `static final File REFS_FOLDER = Utils.join(GITLET_FOLDER, "refs")`
3. `static String HEAD`
4. `static String curBranch`
5. `static HashSet to_add` a set with file names that are being staged for addition
6. `static HashSet to_rm` a set with file names that are being staged for removal


### Commit

This class represents a `Commit` that will be stored in a file. Each `Commit` will have a unique name represented by its SHA-1 id.

All `Commit` objects are serialized within the `COMMIT_FOLDER` which is within the `GITLET_FOLDER`. The `Commit` class has helpful methods that will return the `Commit` object corresponding to its SHA-1 id, or write that `Commit` to a file to persist its changes. 

#### Fields

1. `static final File COMMIT_FOLDER = Utils.join(Repository.GITLET_FOLDER, "commits")`
2. `private String message` the commit message input by user
3. `private Date time` the current time at the moment of the commit in UTD
4. `private String parent` the SHA-1 id of the parent commit
5. `private HashMap blobs` a map with file names as keys and their corresponding SHA-1 ids as values, under the current commit


## Algorithms

In `Main`, there will only be a function to deal with the user input and call the corresponding methods in the `Repository` and `Commit` classes.

In `Repository`, the persistence is setup, and the majority of the methods will be in this class with some helper methods: 

1. `public static void setupPersistence()`*
2. `public static void init()`*
3. `public static void add(String file)`*
4. `public static void commit(String message)`*
5. `public static void checkout(String file)`*
6. `public static void checkout(String id, String file)`*
7. `public static void rm(String file)`*
8. `public static void checkout(String branch)`*
9. `public static void branch(String branch)`*
10. `public static void rmBranch(String branch)`*
11. `public static void reset(String id)`*
12. `public static void log()` returns a log with all commits in the current branch, with the most recent commit on the top; it prints the commit + its id, the date, and the commit message; each commit print block starts with === and ends with \n
13. `public static void globalLog()` 
14. `public static void find(String message)` 
15. `public static void status()` 
16. some helper methods...
*see **Persistence**

In `Commit`, there will be:

1. `public Commit()` constructor that creates the initial commit
2. `public Commit(String message)` constructor that creates any new commit; the constructor sets the `message` and `date`, leaving the instance variables `parent` and `blobs` to commands in `Repository` class
3. `public T getterMethods()` returns the `message`, `date`, `parent` **(maybe)** and `blobs` **(also maybe)** since they are private
4. `public void saveCommit()`*
5. `public static Commit fromFile()`*
6. some helper methods...
*see **Persistence**


## Persistence

The directory structure looks like this: 
 ````
CWD                           
└── .gitlet
    ├── commits                 <== saves the commit objects
        ├── Commit0 SHA-1
        ├── ...
        └── CommitN SHA-1
    ├── blobs                   <== saves the byte[] blobs ever created
        ├── Blob0 SHA-1
        ├── ...
        └── BlobN SHA-1
    ├── refs
        ├── master.txt          <== saves SHA-1 of commit at the head of this branch
        ├── branch1.txt
        ├── ...
        └── branchN.txt
    └── repository              <== an object saving the current state; 
                                    read at the beginning of every command 
                                    and save at the end of every command
````

### WHAT NEEDS TO BE RECORDED:

1. past commits and their variables
2. what has been staged (added/removed) and their associate blobs 
3. what has been modified and deleted in CWD but not staged
4. what has been created in CWD but not tracked
5. branches

### METHODS THAT WILL CHANGE THE STATE OF FILES

The `Repository` will set up all persistence with `init`; when `init` command is ran: 

* PRINT ERROR MESSAGE if there is already a version-control directory on disk
1. Create `.gitlet`, `commits`, `blobs`, `refs` directories and the initial `Repository` object
2. Create commit 0 with "initial commit" `message`, the starting `date`, and null `parent` and `blobs`
3. Save commit 0 to `.gitlet/commits`
4. Assign SHA-1 of commit 0 to `HEAD`
5. Create `master.txt` in `.gitlet/refs` and save the SHA-1 of commit 0 to the content of `master.txt` with `Utils.writeContent`
6. Serialize and overwrite `Repository` object to `.gitlet`

When `add [file name]` command is ran: 

* PRINT ERROR MESSAGE if no such file exists
1. Add `[file name]` to the `to_add` set in `Repository`
2. If not already exists, create a byte array blob corresponding to the file content; save blob to `.gitlet/blobs` with its file name as the SHA-1 id
3. Serialize and overwrite `Repository` object to `.gitlet`

When `commit [message]` command is ran: 

* PRINT ERROR MESSAGE if no files in `to_add`
* PRINT ERROR MESSAGE if no message was passed in
1. Read the `HEAD` commit and create new `Commit` object in java
2. Change the `message` to input, `date` to current time, `parent` to `HEAD`
3. Update `blobs` HashMap
* If `blobs == null`, create new key with the file names in `to_add`, and their corresponding SHA-1 as values
* If `blobs != null`, use `keySet()` to get the keys in `blobs`; iterate through `to_add`: if the item is in the keys, replace its value w/ the new SHA-1, if not in key set, create new key and value
4. Set `to_add` to null
5. Get SHA-1 of the current `commit`
* Set `HEAD` to the new SHA-1
* `readContent` of the `current branch.txt` in `.gitlet/refs`, and overwrite with SHA-1 of the new commit
6. Serialize and overwrite `Repository` object to `.gitlet`

When `checkout -- [file name]` command is ran: 

1. With the SHA-1 id stored in `HEAD`, use `readObject` to get the `Commit` object from `COMMIT_FOLDER`
2. With `keySet()`, get the keys in `blobs` HashMap
* PRINT ERROR MESSAGE if no such file exists in `HEAD` commit
3. Use `[file name]` as key to find the SHA-1 id of its blob
4. Use `writeContent([file name], blob)` to overwrite the file in CWD

When `checkout [commit id] -- [file name]` command is ran: 

1. With some `Utils` function, iterate through `Commit` objects in `COMMIT_FOLDER` and find the `Commit` object with the given SHA-1 id
* PRINT ERROR MESSAGE if no such commit exists in `COMMIT_FOLDER`
2. With `keySet()`, get the keys in `blobs` HashMap
* PRINT ERROR MESSAGE if no such file exists
3. Use `[file name]` as key to find the SHA-1 id of its blob
4. Use `writeContent([file name], blob)` to overwrite the file in CWD

=======Checkpoint 1========

When `rm [file name]` command is ran:

1. If the file name is in `to_add` set, delete from `to_add` 
        if the file is in the previous commit, add it to `to_rm` set, and run `Utils.restrictedDelete`
        if the file is not in the previous commit, status: untrack (might not need to run here)
2. If the file name is not in `to_add` or not tracked in HEAD, print error message

When `checkout [branch name]` command is ran: 

1. Make a `String[]` of all files currently in CWD (????)
2. Look in the `branches` HashMap, use `[branch name]` as the key and find the latest commit in that branch
3. Point `HEAD` to that commit
4. Iterate thru all the blobs, use `Utils.writeContent(blob.parent_file, blob)` to overwrite all the files in CWD, and remove the item from the array of files created in step 1
5. Whatever files are left in the array, use  `Utils.restrictedDelete` to delete from CWD

When `branch [branch name]` command is ran: 

1. Creats a new key in the `branches` HashMap, and pass in the commit at `HEAD` as the value
2. If a branch with the given name already exists, print error message

When `rm-branch [branch name]` command is ran: 

1. Delete the key with the target branch name and its associate value from `branches` HashMap
2. If no branch with the given name exists, print error message
3. If `HEAD` is currently on the target branch, print error message

When `reset [commit id]` command is ran: 

1. Make a `String[]` of all files currently in CWD (????)
2. From `COMMIT_FOLDER`, find the commit with the given id
3. Point `HEAD` to that commit
4. Iterate thru all the blobs, use `Utils.writeContent(blob.parent_file, blob)` to overwrite all the files in CWD, and remove the item from the array of files created in step 1
5. Whatever files are left in the array, use  `Utils.restrictedDelete` to delete from CWD
6. Clear staging area


The `Commit` class will handle the serialization and deserialization of `Commit` objects. 

1. `public void saveCommit()` serializes `Commit` objects to `COMMIT_FOLDER` with `Utils.writeObject`
2. `public static Commit fromFile(String id)` deserializes `Commit` objects from file with `Utils.readObject`
