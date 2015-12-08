package ua.com.juja.sqlcmd_homework.model;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;

/**
 * Created by Sims on 12/09/2015.
 */
public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager();
    }

}
