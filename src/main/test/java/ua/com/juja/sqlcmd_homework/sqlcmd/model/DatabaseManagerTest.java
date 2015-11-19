package ua.com.juja.sqlcmd_homework.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DataSetImplemented;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sims on 03.09.2015.
 */
public abstract class DatabaseManagerTest {
    private DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup(){
        manager = getDatabaseManager();
        manager.connect("mydb_home","postgres", "postgres");
        /*manager.getTableData("user");
        manager.getTableData("test");*/
    }

    @Test
    public void testGetAllTableNames(){
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[user, test]", tableNames.toString());
    }

//    @Test
//    public void testGetTableData(){
//        //given
//        manager.clear("user");
//        //when
//        // add new user
//        DataSet input = new DataSetImplemented();
//        input.put("name", "John");
//        input.put("password", "pass");
//        input.put("id", 13);
//
//        manager.create("user", input);
//        //then
//        List<DataSet> users = manager.getTableData("user");
//        assertEquals(1, users.size());
//
//
//        DataSet user = users.get(0);
//        assertEquals("[name, password, id]", user.getNames().toString());
//        assertEquals("[John, pass, 13]", user.getValues().toString());
//    }

//    @Test
//    public void testUpdateTableData() {
//        // given
//        manager.clear("user");
//
//        DataSet input = new DataSetImplemented();
//
//        input.put("name", "Stiven");
//        input.put("password", "pass");
//        input.put("id", 13);
//
//        manager.create("user", input);
//
//        // when
//        DataSet newValue = new DataSetImplemented();
//        newValue.put("password", "pass2");
//        newValue.put("name", "Pup");
//        manager.update("user", 13, newValue);
//
//        // then
//        List<DataSet> users = manager.getTableData("user");
//        assertEquals(1, users.size());
//
//        DataSet user = users.get(0);
//        assertEquals("[name, password, id]", user.getNames().toString());
//        assertEquals("[Pup, pass2, 13]", user.getValues().toString());
//    }
//    @Test
//    public void testGetColumnNames() {
//        // given
//        manager.clear("user");
//
//        // when
//        Set<String> columnNames = manager.getTableColumns("user");
//
//        //then
//        assertEquals("[name, password, id]", columnNames.toString());
//    }

    @Test
    public void testIsConnected() {
        // given
        // when
        //then
        assertTrue(manager.isConnected());
    }
}
