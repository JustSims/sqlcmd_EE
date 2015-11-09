package ua.com.juja.sqlcmd_homework.service;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sims on 05/11/2015.
 */
public class ServiceImplemented implements Service {

    private DatabaseManager manager;

    public ServiceImplemented(){
        manager = new JDBCDatabaseManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        manager.connect(databaseName, userName, password);
    }
}
