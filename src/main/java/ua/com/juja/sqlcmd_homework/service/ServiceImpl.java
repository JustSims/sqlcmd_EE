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
        return Arrays.asList("help", "menu", "list", "find", "clear", "create", "deleteRecord");
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
}
