package sqlcmd_homework.controller;

import sqlcmd_homework.controller.command.Command;
import sqlcmd_homework.controller.command.Exit;
import sqlcmd_homework.controller.command.Help;
import sqlcmd_homework.model.DataSet;
import sqlcmd_homework.model.DatabaseManager;
import sqlcmd_homework.view.View;

import java.util.Arrays;

/**
 * Created by Sims on 12/09/2015.
 */
public class MainController {

    private final Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager){
        this.view = view;
        this.manager = manager;
        this.commands = new Command[] {new Exit(view), new Help(view)};
    }

    public void run(){
        connectToDb();
        while (true) {
            view.write("Enter command (or 'help')");
            String command = view.read();

            if (command.equals("list")) {
                showList();
            } else if (commands[1].canProcess("help")) {
                commands[1].process(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else if (command.startsWith("find")) {
                findTable(command);
            } else {
                view.write("Non existing command: " + command);
            }
        }
    }

    private void findTable(String command) {
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

    private void showList() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);

    }

    private void connectToDb() {
        view.write("Hi, welcome to the database manager.");
        view.write("Enter 'database|userName|password' to connect to database:");
        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3){
                    throw new IllegalArgumentException("Invalid amount of parameters, separated by '|', expected 3, but you've entered: " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Success!");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (e.getCause() != null) {
            message += " " + cause.getMessage();
        }
        view.write("Something went wrong: " + message);
        view.write("Try again:");
    }
}

