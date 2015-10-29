package ua.com.juja.sqlcmd_homework.model;

/**
 * Created by Sims on 29/10/2015.
 */
public interface DataSet {
    void put(String name, Object value);

    Object[] getValues();

    String[] getNames();

    Object get(String name);

    void updateFrom(DataSet newValue);
}
