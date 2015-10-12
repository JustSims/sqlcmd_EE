package sqlcmd_homework.controller.command;

import sqlcmd_homework.controller.command.Command;
import sqlcmd_homework.view.View;

/**
 * Created by Sims on 12/10/2015.
 */
public class Unsupported implements Command {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("Non existing command: " + command);
    }
}
