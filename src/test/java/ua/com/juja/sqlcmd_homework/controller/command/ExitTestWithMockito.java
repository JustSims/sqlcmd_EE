package ua.com.juja.sqlcmd_homework.controller.command;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd_homework.view.View;

import static junit.framework.Assert.*;


/**
 * Created by Sims on 26/10/2015.
 */
public class ExitTestWithMockito {

    private View view = Mockito.mock(View.class);

    @Test
     public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("exit");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessQweString() {
        //given
        Command command = new Exit(view);

        //when
        boolean canProcess = command.canProcess("qwe");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitStringThrowsExitException() {
        //given
        Command command = new Exit(view);

        //when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e){
            //do nothing
        }

        //then
        Mockito.verify(view).write("Bye");
    }
}
