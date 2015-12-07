package ua.com.juja.sqlcmd_homework.jwebunit;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.model.JDBCDatabaseManager;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
/**
 * Created by Sims on 07/12/2015.
 */
public class MainServletTest {
    DatabaseManager manager = new JDBCDatabaseManager();


    @Before
    public void prepare() {
        setBaseUrl("http://localhost:8080/sqlcmd_homework");
        beginAt("/connect");
        setTextField("dbname", "mydb_home");
        setTextField("username", "postgres");
        setTextField("password", "postgres");
        submit();
    }

    @Test
    public void testMenu() {
        assertLinkPresentWithText("connect");
        assertLinkPresentWithText("help");
        assertLinkPresentWithText("menu");
        assertLinkPresentWithText("list");
        assertLinkPresentWithText("find");
        assertLinkPresentWithText("clear");
        assertLinkPresentWithText("create");
        assertLinkPresentWithText("deleteRecord");
    }

//    @Test
//    public void testHelp() {
//        clickLinkWithText("help");
//        assertTextPresent("Existing commands: \n" +
//                "'connect|databaseName|userName|password' - to connect to your database \n" +
//                "'list' - to show all existing table names\n" +
//                "'clear|tableName' - to clear whole table\n" +
//                "'create|tableName|column1|value1|column2|value2|...|columnN|ValueN' - to create record in table\n" +
//                "'find|tableName' - to show table 'tableName' content\n" +
//                "'help' - to show help for this project\n" +
//                "'exit' - to exit the program" +
//                "You can go back to ");
//        assertLinkPresentWithText("menu");
//    }
}
