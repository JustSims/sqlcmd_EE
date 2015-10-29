package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DataSetImplemented;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 20/10/2015.
 */
public class Create implements Command {
    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] userData = command.split("\\|");
        if (userData.length % 2 != 0){
            throw new IllegalArgumentException("Should be even amount of parameter's in 'create|tableName|column1|value1|column2|value2|...|columnN|ValueN' format, and you sent: " + command);
        }

        String tableName = userData[1];
        DataSet dataSet = new DataSetImplemented();

        for (int index = 1; index < userData.length / 2; index++) {
            String columnName = userData[index*2];
            String value = userData[index*2 + 1];
            dataSet.put(columnName, value);
        }
        manager.create(tableName, dataSet);
        view.write(String.format("Record %s was successfully created in table '%s'", dataSet, tableName));
    }
}
