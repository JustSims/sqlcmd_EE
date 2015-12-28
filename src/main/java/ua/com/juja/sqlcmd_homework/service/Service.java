package ua.com.juja.sqlcmd_homework.service;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

import java.util.List;
import java.util.Set;

/**
 * Created by Sims on 05/11/2015.
 */
public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException;

    Set<String> tables(DatabaseManager manager);
}
