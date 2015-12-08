package ua.com.juja.sqlcmd_homework.webunit;
import org.junit.Before;
import org.junit.Test;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
/**
 * Created by Sims on 07/12/2015.
 */
public class JSPTest {

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

    @Test
    public void testHelp() {
        clickLinkWithText("help");
        assertTextPresent("Existing commands:");
        assertTextPresent("'connect|databaseName|userName|password' - to connect to your database");
        assertTextPresent("'list' - to show all existing table names");
        assertTextPresent("'clear|tableName' - to clear whole table");
        assertTextPresent("'create|tableName|column1|value1|column2|value2|...|columnN|ValueN' - to create record in table");
        assertTextPresent("'find|tableName' - to show table 'tableName' content");
        assertTextPresent("'help' - to show help for this project");
        assertTextPresent("'exit' - to exit the program");
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }

    @Test
    public void testClear() {
        clickLinkWithText("clear");
        assertTextPresent("Table name");
        assertTextPresent("Back to menu");
        setTextField("tableName", "user");
        submit();

        assertTextPresent("Success!");
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }

    @Test
    public void testList() {
        clickLinkWithText("list");
        assertTextPresent("Available tables:");
        assertTableEquals("", new String[][]{{"1", "user"}, {"2", "test"}});
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }

    @Test
    public void testFind() {
        clickLinkWithText("find");
        assertTextPresent("Table name");
        setTextField("tableName", "user");
        assertTextPresent("Back to menu");
        submit();

        assertTableEquals("", new String[][]{{"name", "password", "id"}});
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }

    @Test
    public void testAddRecord() {
        clickLinkWithText("create");
        assertTextPresent("Table name");
        setTextField("tableName", "user");
        submit();

        assertTextPresent("1 Type column name");
        assertTextPresent("1 Type column value");
        assertTextPresent("2 Type column name");
        assertTextPresent("2 Type column value");
        assertTextPresent("3 Type column name");
        assertTextPresent("3 Type column value");

        setTextField("columnName1", "name");
        setTextField("columnValue1", "user");

        setTextField("columnName2", "password");
        setTextField("columnValue2", "password");

        setTextField("columnName3", "id");
        setTextField("columnValue3", "2");


        assertTextPresent("Back to menu");
        submit();


        assertTextPresent("Success!");
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }

    @Test
    public void testDeleteRecord() {
        clickLinkWithText("deleteRecord");
        assertTextPresent("Table name");
        assertTextPresent("Back to menu");
        setTextField("tableName", "user");
        setTextField("keyName", "password");
        setTextField("keyValue", "St2");
        submit();

        assertTextPresent("Success!");
        assertTextPresent("Back to menu");
        assertLinkPresentWithText("menu");
    }


}
