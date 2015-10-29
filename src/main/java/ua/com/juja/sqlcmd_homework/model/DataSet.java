package ua.com.juja.sqlcmd_homework.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Sims on 29/10/2015.
 */
public interface DataSet {
    void put(String name, Object value);

    List<Object> getValues();

    Set<String> getNames();

    Object get(String name);

    void updateFrom(DataSet newValue);
}
