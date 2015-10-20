package ua.com.juja.sqlcmd_homework.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.controller.Main;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;

/**
 * Created by Sims on 13/10/2015.
 */
public class IntegrationTest {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                "Existing commands:\r\n" +
                "\t 'connect|databaseName|userName|password - to connect to your database\r\n" +
                "\t 'list' - to show all existing table names\r\n" +
                "\t 'find|tableName' - to show table 'tableName' content\r\n" +
                "\t 'help' - to show help for this project\r\n" +
                "\t 'exit' - to exit the program\r\n" +
                "Enter command (or 'help')\r\n" +
                "Bye\r\n", getData());

    }

    @Test
    public void testExit() {
        //given
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //exit
                "Bye\r\n", getData());

    }

    @Test
    public void testListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //list
                "You cannot use command 'list', before connecting. To enter database type: connect|databaseName|userName|password\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        //given
        in.add("find|user");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //find|user
                "You cannot use command 'find|user', before connecting. To enter database type: connect|databaseName|userName|password\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        in.add("assas");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //unsupported command
                "You cannot use command 'assas', before connecting. To enter database type: connect|databaseName|userName|password\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add("connect|mydb_home|postgres|postgres");
        in.add("assas");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //connect
                "Success!\r\n" +
                "Enter command (or 'help')\r\n" +
                //unsupported
                "Non existing command: assas\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add("connect|mydb_home|postgres|postgres");
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //connect
                "Success!\r\n" +
                "Enter command (or 'help')\r\n" +
                //list
                "[user, test]\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        //given
        in.add("connect|mydb_home|postgres|postgres");
        in.add("find|user");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //connect
                "Success!\r\n" +
                "Enter command (or 'help')\r\n" +
                //find|user
                "-----------------\r\n" +
                "name|password|id|\r\n" +
                "-----------------\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add("connect|mydb_home|postgres|postgres");
        in.add("list");
        in.add("connect|test|postgres|postgres");
        in.add("list");
        in.add("exit");
        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hi, welcome to the database manager.\r\n" +
                "Enter 'connect|database|userName|password' to connect to database:\r\n" +
                //connect mydb
                "Success!\r\n" +
                "Enter command (or 'help')\r\n" +
                //list
                "[user, test]\r\n" +
                "Enter command (or 'help')\r\n" +
                //connect test
                "Success!\r\n" +
                //list
                "Enter command (or 'help')\r\n" +
                "[qwe]\r\n" +
                "Enter command (or 'help')\r\n" +
                //exit
                "Bye\r\n", getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
