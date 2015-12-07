package ua.com.juja.sqlcmd_homework.sqlcmd.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.sqlcmd_homework.model.DataSetImplemented;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.service.Service;
import ua.com.juja.sqlcmd_homework.service.ServiceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sims on 02/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-application-context.xml"})

public class ServiceImplTest {

    @Autowired
    private Service service;


    @Test
    public void connectTest() throws ServiceException, SQLException {
        //given
        DatabaseManager manager = service.connect("mydb_home", "postgres", "postgres");

    }

    @Test
    public void listTest() throws ServiceException, SQLException {
        //given
        DatabaseManager manager = service.connect("mydb_home", "postgres", "postgres");

        //when
        Set<String> tableNames = manager.getTableData();
        //then
        assertEquals("[user, test]", tableNames.toString());
    }


    @Test
    public void findTest() throws ServiceException, SQLException {
        DatabaseManager manager = service.connect("mydb_home", "postgres", "postgres");
        DataSetImplemented input = new DataSetImplemented();

        //when
        List<String> tableData = manager.find("user");

        //then
        assertEquals("[name, password, id]", "[" + tableData.toString().substring(4));

    }

    @Test
    public void clearTest() throws ServiceException, SQLException {
        DatabaseManager manager = service.connect("mydb_home", "postgres", "postgres");

        //when
        String tableName = "user";
        manager.clear(tableName);

        //then
        List<String> tableData = manager.find("user");
        assertEquals("[name, password, id]", "[" + tableData.toString().substring(4));
    }
}
