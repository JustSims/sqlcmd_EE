package ua.com.juja.sqlcmd_homework.service;

import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sims on 05/11/2015.
 */
public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<String> find(DatabaseManager manager, String tableName) throws SQLException;

    Set<String> list(DatabaseManager manager) throws SQLException;

    void clear(DatabaseManager manager, String tableName) throws SQLException;

    void create(DatabaseManager manager, String tableName, Map<String, Object> inputData);

    void table(DatabaseManager manager, String tableName, String keyName, Map<String, Object> columnParameters)
            throws SQLException;
}
