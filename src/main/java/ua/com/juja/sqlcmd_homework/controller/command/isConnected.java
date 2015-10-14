package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.controller.command.Command;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 12/10/2015.
 */
public class isConnected implements Command {
    private final DatabaseManager manager;
    private final View view;

    public isConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("You cannot use command '%s', before connecting to database with connect|databaseName|userName|password ", command));
    }
}
