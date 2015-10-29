package ua.com.juja.sqlcmd_homework.model;

import java.util.Set;

/**
 * Created by Sims on 12/09/2015.
 */
public interface DatabaseManager {
    DataSet[] getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();
}
