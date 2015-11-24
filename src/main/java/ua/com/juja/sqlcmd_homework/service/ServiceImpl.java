package ua.com.juja.sqlcmd_homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Sims on 05/11/2015.
 */
@Component
public class ServiceImpl implements Service {

    @Autowired
    private DatabaseManagerFactory factory;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("connect", "help", "menu", "list", "find", "clear", "create", "deleteRecord");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = factory.createDatabaseManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<String> find(DatabaseManager manager, String tableName) throws SQLException {
        return manager.getTableData(tableName);
    }

    @Override
    public Set<String> list(DatabaseManager manager) throws SQLException {
        return manager.getTableNames();
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) throws SQLException {
        manager.clear(tableName);
    }

    @Override
    public void create(DatabaseManager manager, String tableName, Map<String, Object> inputData)
            {
        manager.create(tableName, inputData);
    }

    @Override
    public void table(DatabaseManager manager, String tableName, String keyName,
                      Map<String, Object> columnParameter) throws SQLException {
        manager.table(tableName, keyName, columnParameter);
    }

    @Override
    public void deleteRecord(DatabaseManager manager, String tableName, String keyName, String keyValue) throws SQLException {
        manager.deleteRecord(tableName, keyName, keyValue);
    }
}
