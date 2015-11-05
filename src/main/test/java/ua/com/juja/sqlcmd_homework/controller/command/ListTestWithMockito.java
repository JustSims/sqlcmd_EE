package ua.com.juja.sqlcmd_homework.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sims on 27/10/2015.
 */
public class ListTestWithMockito {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setUp(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Tables(manager, view);
    }


    @Test
    public void testCanProcessList() {
        //given

        //when
        boolean canProcess = command.canProcess("list");

        //then
        assertTrue(canProcess);
    }


}
