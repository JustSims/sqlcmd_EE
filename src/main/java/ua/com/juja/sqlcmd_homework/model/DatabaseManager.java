package ua.com.juja.sqlcmd_homework.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sims on 12/09/2015.
 */
public interface DatabaseManager {
    List<String> find(String tableName) throws SQLException;

    int getSize(String tableName) throws SQLException;

    Set<String> getTableData() throws SQLException;

    void connect(String database, String userName, String password) throws SQLException;

    void clear(String tableName) throws SQLException;

    void create(String tableName, Map<String, Object> inputData) throws SQLException;

    void update(String tableName, int id, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void table(String tableName, String keyName, Map<String, Object> columnParameters) throws SQLException;

    void deleteRecord(String tableName, String keyName, String keyValue) throws SQLException;

}
