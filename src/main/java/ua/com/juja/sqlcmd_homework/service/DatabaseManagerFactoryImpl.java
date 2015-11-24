package ua.com.juja.sqlcmd_homework.service;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

/**
 * Created by Sims on 24/11/2015.
 */
public class DatabaseManagerFactoryImpl implements DatabaseManagerFactory {

    private String className;

    @Override
    public DatabaseManager createDatabaseManager() {
        try {
            return (DatabaseManager)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }
}