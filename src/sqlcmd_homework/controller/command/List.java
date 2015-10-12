package sqlcmd_homework.controller.command;


import sqlcmd_homework.model.DatabaseManager;
import sqlcmd_homework.view.View;

import java.util.Arrays;

/**
 * Created by Sims on 12/10/2015.
 */
public class List implements Command{

    private View view;
    private DatabaseManager manager;

    public List(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
    }
}
