package ua.com.juja.sqlcmd_homework.model;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;

import java.sql.SQLException;
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
    public void setup() throws SQLException {
        manager = getDatabaseManager();
        manager.connect("mydb_home","postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames() throws SQLException {
        // given
        manager.getTableData("user");
        manager.getTableData("test");

        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals("[user, test]", tableNames.toString());
    }

    @Test
    public void testIsConnected() {
        // given
        // when
        //then
        assertTrue(manager.isConnected());
    }
}
