package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 12/10/2015.
 */
public class Connect implements Command {
    private static String COMMAND_EXAMPLE = "connect|mydb_home|postgres|postgres";
    private final DatabaseManager manager;
    private final View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
                String[] data = command.split("\\|");

                if (data.length != parametersLength()){
                    throw new IllegalArgumentException(String.format("Invalid amount of parameters, separated by '|'," +
                        " expected %s, but you've entered: %s", parametersLength(), data.length));
                }
                String databaseName = data[1];
                String userName = data[2];
                String password = data[3];

                manager.connect(databaseName, userName, password);
                view.write("Success!");
    }

    private int parametersLength() {
        return COMMAND_EXAMPLE.split("\\|").length;
    }

}
