package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 20/10/2015.
 */
public class Clear implements Command {

    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] userData = command.split("\\|");
        if (userData.length != 2){
            throw new IllegalArgumentException("Command format 'clear|tableName', and you've entered: " + command);
        }
        manager.clear(userData[1]);
        view.write(String.format("Table %s was successfully cleared", userData[1]));
    }
}
