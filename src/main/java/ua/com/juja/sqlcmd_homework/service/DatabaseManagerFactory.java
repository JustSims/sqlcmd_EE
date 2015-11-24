package ua.com.juja.sqlcmd_homework.service;

import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

/**
 * Created by Sims on 23/11/2015.
 */
@Component
public interface DatabaseManagerFactory {
    DatabaseManager createDatabaseManager();
}

