package sqlcmd_homework.controller.command;

import sqlcmd_homework.controller.command.Command;
import sqlcmd_homework.model.DataSet;
import sqlcmd_homework.model.DatabaseManager;
import sqlcmd_homework.view.View;

/**
 * Created by Sims on 12/10/2015.
 */
public class Find implements Command {
    private final DatabaseManager manager;
    private final View view;

    public Find(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
            String[] data = command.split("\\|");
            String tableName = data[1];
            String[] tableColumns = manager.getTableColumns(tableName);
            printHeader(tableColumns);
            DataSet[] tableData = manager.getTableData(tableName);
            printTable(tableData);
        }

    private void printTable(DataSet[] tableData) {
        for (DataSet row: tableData){
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "";
        for (Object value: values){
            result += value + "|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColumns) {
        String result = "";
        for (String name: tableColumns){
            result += name + "|";
        }
        view.write("-----------------");
        view.write(result);
        view.write("-----------------");
    }
    }


