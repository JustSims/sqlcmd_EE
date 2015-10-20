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

    public MainController(View view, DatabaseManager manager){
        this.view = view;
        this.commands = new Command[] {
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new isConnected(manager, view),
                new Create(manager, view),
                new List(manager, view),
                new Clear(manager, view),
                new Find(manager, view),
                new Unsupported(view)};
    }

    public void run(){
        view.write("Hi, welcome to the database manager.");
        view.write("Enter 'connect|database|userName|password' to connect to database:");
        try {
            doWork();
        } catch (ExitException e){
            //do nothing
        }
    }

    private void doWork() {
        while (true) {
            String input = view.read();
            if (input == null) {
                new Exit(view).process("");
            }
            for (Command command : commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Enter command (or 'help')");
        }
    }

}

