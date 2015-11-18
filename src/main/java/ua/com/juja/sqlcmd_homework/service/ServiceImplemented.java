package ua.com.juja.sqlcmd_homework.service;

import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Sims on 05/11/2015.
 */
public class ServiceImplemented implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect", "find", "list");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = new JDBCDatabaseManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<DataSet> tableData = manager.getTableData(tableName);

        result.add(columns);
        for (DataSet dataSet: tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column: columns){
                row.add(dataSet.get(column).toString());
            }
        }
        return result;
    }

    @Override
    public Set<String> list(DatabaseManager manager) throws SQLException {
        return manager.getTableNames();
    }
}
