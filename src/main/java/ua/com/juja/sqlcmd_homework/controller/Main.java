package ua.com.juja.sqlcmd_homework.controller;

import ua.com.juja.sqlcmd_homework.controller.command.*;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd_homework.view.Console;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 06/10/2015.
 */
public class Main {

    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager, new Command[]{
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new isConnected(manager, view),
                new Tables(manager, view),
//                new Clear(manager, view),
                new Unsupported(view)
        });
        controller.run();
    }
}
