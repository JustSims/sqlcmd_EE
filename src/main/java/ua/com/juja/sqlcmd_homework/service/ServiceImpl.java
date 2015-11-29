package ua.com.juja.sqlcmd_homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
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
    public DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException {
        try {
            DatabaseManager manager = factory.createDatabaseManager();
            manager.connect(databaseName, userName, password);
            return manager;
        } catch (Exception e) {
            throw new ServiceException("Connection error", e);
        }
    }

    @Override
    public List<String> find(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            return manager.getTableData(tableName);
        } catch (Exception e) {
            throw new ServiceException("Find error", e);
        }
    }

    @Override
    public Set<String> list(DatabaseManager manager) throws ServiceException {
        try {
            return manager.getTableNames();
        } catch (Exception e) {
            throw new ServiceException("Clear error", e);
        }
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            manager.clear(tableName);
        } catch (Exception e) {
            throw new ServiceException("Clear error", e);
        }
    }

    @Override
    public void create(DatabaseManager manager, String tableName, Map<String, Object> inputData) throws ServiceException {
        try {
            manager.create(tableName, inputData);
        } catch (Exception e) {
            throw new ServiceException("Create error", e);
        }
    }

    @Override
    public void table(DatabaseManager manager, String tableName, String keyName,
                      Map<String, Object> columnParameter) throws ServiceException {
        try {
            manager.table(tableName, keyName, columnParameter);
        } catch (Exception e) {
            throw new ServiceException("Table error", e);
        }
    }

    @Override
    public void deleteRecord(DatabaseManager manager, String tableName, String keyName, String keyValue) throws ServiceException {
        try {

            manager.deleteRecord(tableName, keyName, keyValue);
        } catch (Exception e) {
            throw new ServiceException("Delete record error", e);
        }
    }
}
