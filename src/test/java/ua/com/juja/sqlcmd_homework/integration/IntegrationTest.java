package ua.com.juja.sqlcmd_homework.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.controller.Main;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;


/**
 * Created by Sims on 13/10/2015.
 */
public class IntegrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void setUp(){
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();


        System.setIn(in);
        System.setOut(new PrintStream(out));
    }


    @Test
    public void testExit(){
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

    public String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
