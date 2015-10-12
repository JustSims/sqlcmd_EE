package sqlcmd_homework.controller;

import sqlcmd_homework.controller.command.*;
import sqlcmd_homework.model.DatabaseManager;
import sqlcmd_homework.view.View;


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
        this.commands = new Command[] {new Exit(view), new Help(view), new List(manager, view), new Find(manager, view), new Unsupported(view)};
    }

    public void run(){
        connectToDb();
        while (true) {
            view.write("Enter command (or 'help')");
            String input = view.read();
            for(Command command: commands){
                if (command.canProcess(input)){
                    command.process(input);
                    break;
                }
            }
        }
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

