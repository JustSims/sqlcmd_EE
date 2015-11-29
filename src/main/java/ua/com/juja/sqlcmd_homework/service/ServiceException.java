package ua.com.juja.sqlcmd_homework.service;

import java.sql.SQLException;

/**
 * Created by Sims on 29/11/2015.
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
