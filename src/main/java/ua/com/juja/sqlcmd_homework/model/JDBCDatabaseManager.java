package ua.com.juja.sqlcmd_homework.model;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * Created by Sims on 02.09.2015.
 */
@Component
public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    @Override
    public void connect(String database, String userName, String password) throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project", e);
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName,
                    password);
        } catch (SQLException e) {
            connection =  null;
            throw new RuntimeException(String.format("Couldnt get connection" +
                    " for model: %s user %s", database, userName), e);
        }
    }
    @Override
    public boolean isConnected() {
        return connection!= null;
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
    public int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    @Override
    public Set<String> getTableData() {
        Set<String> tables = new LinkedHashSet<String>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables " +
                    "WHERE table_schema='public' AND table_type='BASE TABLE';");
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
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

            stmt.executeUpdate("INSERT INTO public."+ tableName + " (" + getColumnNames(inputData) + ")" +
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
    public void update(String tableName, int id, DataSet newValue) {
        try {
            String tableNames = getNameFormated(newValue, "%s = ?,");

            String sql = "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> tables = new LinkedHashSet<String>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT *\n" +
                    "FROM information_schema.columns\n" +
                    "WHERE table_schema = 'public'\n" +
                    "AND table_name = '" + tableName + "'");
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return tables;
        }
    }

    private String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
