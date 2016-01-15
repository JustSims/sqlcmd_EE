package ua.com.juja.sqlcmd_homework.model;

import org.springframework.context.annotation.Scope;
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
    private String database;
    private String userName;

    @Override
    public void connect(String database, String userName, String password){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName,
                    password);
            this.database = database;
            this.userName = userName;
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
        } catch (SQLException e) {
            connection = null;
            template = null;
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
        template.execute(String.format("CREATE TABLE IF NOT EXISTS public.%s " +
                        "(%s INT  PRIMARY KEY NOT NULL %s)",
                tableName, primaryKey, getParameters(columnParameters)));
    }

    @Override
    public void deleteRecord(String tableName, String keyName, String keyValue) throws SQLException {
        template.update(String.format("DELETE FROM public.%s WHERE %s = '%s'",
                tableName, keyName, keyValue));
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
        template.execute("DELETE FROM public." + tableName);
    }

    @Override
    public void create(String tableName, Map<String, Object> inputData) {
        StringJoiner keyJoiner = new StringJoiner(", ");
        StringJoiner valueJoiner = new StringJoiner("', '", "'", "'");

        for (Map.Entry<String, Object> pair : inputData.entrySet()) {
            keyJoiner.add(pair.getKey());
            valueJoiner.add(pair.getValue().toString());
        }
        template.update(String.format("INSERT INTO public.%s(%s) values (%s)",
                tableName, keyJoiner.toString(), valueJoiner.toString()));
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

    @Override
    public String getDatabaseName() {
        return database;
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
