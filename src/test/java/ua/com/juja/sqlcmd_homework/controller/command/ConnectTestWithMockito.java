package ua.com.juja.sqlcmd_homework.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Sims on 27/10/2015.
 */
public class ConnectTestWithMockito {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setUp(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(manager, view);
    }


    @Test
    public void testConnectToDb(){
        //given

        command.process("connect|mydb_home|postgres|postgres");
        //when
        verify(manager).connect("mydb_home", "postgres", "postgres");
        verify(view).write("Success!");
    }

    @Test
     public void testCantProcessConnectWithoutParameters() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("connect");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testConnectErrorWhenCountParametersLessThan4() {
        //given


        //when
        try {
            command.process("connect|");
            fail();
        }catch (IllegalArgumentException e){
            // then
            assertEquals("Invalid amount of parameters, separated by '|'," +
                    " expected 4, but you've entered: 1", e.getMessage());
        }

    }

    @Test
    public void testCanProcessFindWithParameters() {
        //given

        //when
        boolean canProcess = command.canProcess("connect|");

        //then
        assertTrue(canProcess);
    }


}
