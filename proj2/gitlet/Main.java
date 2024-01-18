package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Joanna Lin, Lulu Wu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (firstArg.equals("init")) {
            // handles the `init` command
            Repository.init();
            return;
        }
        Repository repo = Repository.fromFile();
        if (repo == null) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch(firstArg) {
            case "add":
                // handles the `add [filename]` command
                repo.add(args);
                repo.saveRepo();
                break;
            case "commit":
                // handles the `commit [message]` command
                repo.commit(args);
                repo.saveRepo();
                break;
            case "rm":
                // TODO: handle the `rm [filename]` command
                repo.rm(args);
                repo.saveRepo();
                break;
            case "log":
                // handles the `log` command
                repo.log();
                break;
            case "global-log":
                // TODO: handle the `global-log` command
                repo.globalLog();
                break;
            case "find":
                // TODO: handle the `find [message]` command
                repo.find(args);
                break;
            case "status":
                // TODO: handle the `status` command
                repo.status();
                break;
            case "checkout":
                // TODO: handle the `checkout (3 cases)` command
                repo.checkout(args);
                repo.saveRepo();
                break;
            case "branch":
                // TODO: handle the `branch [branch name]` command
                repo.branch(args);
                repo.saveRepo();
                break;
            case "rm-branch":
                // TODO: handle the `rm-branch [branch name]` command
                repo.rmBranch(args);
                repo.saveRepo();
                break;
            case "reset":
                // TODO: handle the `reset [commit id]` command
                repo.reset(args);
                repo.saveRepo();
                break;
            default:
                System.out.println("No command with that name exists.");
                return;
        }
        return;
    }

}
