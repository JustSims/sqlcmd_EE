package sqlcmd_homework.controller.command;

/**
 * Created by Sims on 12/10/2015.
 */
public interface Command {
    boolean canProcess(String command);
    void process(String command);
}
