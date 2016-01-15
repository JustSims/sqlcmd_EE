package ua.com.juja.sqlcmd_homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.UserActionRepository;
import ua.com.juja.sqlcmd_homework.model.entity.UserAction;

import java.util.*;

/**
 * Created by Sims on 05/11/2015.
 */
@Component
public abstract class ServiceImpl implements Service {

    protected abstract DatabaseManager getManager();

    @Autowired
    private UserActionRepository userActions;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "list", "find", "clear", "create", "deleteRecord");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password){
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        userActions.save(new UserAction(userName, databaseName, "CONNECT"));
        return manager;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        UserAction action = new UserAction(manager.getUserName(),
                manager.getDatabaseName(), "TABLES");
        userActions.save(action);

        return manager.getTableNames();
    }

    @Override
    public List<UserAction> getAllFor(String userName){
        if (userName == null){
            throw new IllegalArgumentException("User name cant be null");
        }
        return userActions.findByUserName(userName);
    }
}
