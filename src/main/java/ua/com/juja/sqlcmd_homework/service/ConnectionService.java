package ua.com.juja.sqlcmd_homework.service;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

import java.util.List;

/**
 * Created by Sims on 05/11/2015.
 */
public interface ConnectionService {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException;
}
