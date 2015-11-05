package ua.com.juja.sqlcmd_homework.service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sims on 05/11/2015.
 */
public class ServiceImplemented implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }
}
