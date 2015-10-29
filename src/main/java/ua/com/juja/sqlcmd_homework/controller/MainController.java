package ua.com.juja.sqlcmd_homework.controller;

import ua.com.juja.sqlcmd_homework.controller.command.*;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;


/**
 * Created by Sims on 12/09/2015.
 */
public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager, Command... commands){
        this.view = view;
        this.manager = manager;
        this.commands = commands;
    }

    public void run(){
        try {
            doWork();
        } catch (ExitException e){
            //do nothing
        }
    }

    private void doWork() {
        view.write("Hi, welcome to the database manager.");
        view.write("Enter 'connect|database|userName|password' to connect to database:");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try{
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e){
                    if (e instanceof ExitException) {
                            throw e;
                        }
                    printError(e);
                    break;
                }
            }
            view.write("Enter command (or 'help')");
        }
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

