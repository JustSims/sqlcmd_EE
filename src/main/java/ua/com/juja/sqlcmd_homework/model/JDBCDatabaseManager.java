package ua.com.juja.sqlcmd_homework.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by Sims on 02.09.2015.
 */
@Component
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;
    private JdbcTemplate template;

    @Override
    public void connect(String database, String userName, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName,
                    password);
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format("Couldnt get connection" +
                    " for model: %s user %s", database, userName), e);
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public List<String> find(String tableName) throws SQLException {

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM public. " + tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();

        List<String> tableData = new ArrayList<>();
        tableData.add(String.valueOf(rsmd.getColumnCount()));
        for (int indexColumn = 1; indexColumn <= rsmd.getColumnCount(); indexColumn++) {
            tableData.add(resultSet.getMetaData().getColumnName(indexColumn));
        }

        while (resultSet.next()) {
            for (int indexData = 1; indexData <= rsmd.getColumnCount(); indexData++) {
                if (resultSet.getString(indexData) == null) {
                    tableData.add("");
                } else {
                    tableData.add(resultSet.getString(indexData));
                }
            }
        }
        stmt.close();
        resultSet.close();
        return tableData;
    }

    @Override
    public List<List<String>> getTableData(String tableName) {

        return template.query("SELECT * FROM public." + tableName,
                new RowMapper<List<String>>() {
                    public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        List<String> row = new ArrayList<>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        for (int index = 0; index < rsmd.getColumnCount(); index++) {
                            row.add(rs.getString(index + 1));
                        }
                        return row;
                    }
                });
    }

    @Override
    public void table(String tableName, String primaryKey, Map<String, Object> columnParameters) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE " + tableName +
                "(" + primaryKey + " INT  PRIMARY KEY NOT NULL" +
                getParameters(columnParameters) + ")");
        stmt.close();
    }

    @Override
    public void deleteRecord(String tableName, String keyName, String keyValue) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public. " + tableName + " WHERE " + keyName + " = '" + keyValue + "'");
        stmt.close();
    }

    private String getParameters(Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }

    @Override
    public void clear(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public." + tableName);
        stmt.close();

    }

    @Override
    public void create(String tableName, Map<String, Object> inputData) {
        try {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + getColumnNames(inputData) + ")" +
                    "VALUES (" + getColumnValues(inputData) + ")");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getColumnNames(Map<String, Object> inputData) {
        String keys = "";
        for (Map.Entry<String, Object> pair : inputData.entrySet()) {
            keys += pair.getKey() + ", ";
        }
        return keys.substring(0, keys.length() - 2);
    }

    private String getColumnValues(Map<String, Object> inputData) {
        String values = "";
        for (Map.Entry<String, Object> pair : inputData.entrySet()) {
            values += "'" + pair.getValue() + "', ";
        }
        return values.substring(0, values.length() - 2);
    }

    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(template.query("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'",
                new RowMapper<String>() {
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("table_name");
                    }
                }
        ));
    }
}
