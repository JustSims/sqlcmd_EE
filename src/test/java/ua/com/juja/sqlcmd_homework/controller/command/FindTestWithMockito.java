package ua.com.juja.sqlcmd_homework.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sims on 27/10/2015.
 */
public class FindTestWithMockito {

    private DatabaseManager manager;
    private View view;

    @Before
    public void setUp(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
    }

    @Test
    public void testPrintTableData(){
        //given
        Command command = new Find(manager, view);
        when(manager.getTableColumns("user")).thenReturn(new String[]{"id", "name", "password"} );

        DataSet user1 = new DataSet();
        user1.put("id", 12);
        user1.put("name", "userName1");
        user1.put("password", "*****");

        DataSet user2 = new DataSet();
        user2.put("id", 13);
        user2.put("name", "userName2");
        user2.put("password", "+++++");

        DataSet[] data = new DataSet[]{user1, user2};
        when(manager.getTableData("user")).thenReturn(data);

        //when
        command.process("find|user");

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[-----------------, id|name|password|," +
                     " -----------------, 12|userName1|*****|," +
                     " 13|userName2|+++++|, -----------------]", captor.getAllValues().toString());
    }
    @Test
    public void testCanProcessFindWithParameters() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("find|user");

        //then
        assertTrue(canProcess);
    }

    @Test
     public void testCantProcessFindWithoutParameters() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("find");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessFindWithQweParameters() {
        //given
        Command command = new Find(manager, view);

        //when
        boolean canProcess = command.canProcess("qwe|user");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData(){
        //given
        Command command = new Find(manager, view);
        when(manager.getTableColumns("user")).thenReturn(new String[]{"id", "name", "password"} );

        DataSet[] data = new DataSet[0];
        when(manager.getTableData("user")).thenReturn(data);

        //when
        command.process("find|user");

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[-----------------, id|name|password|," +
                " -----------------," +
                " -----------------]", captor.getAllValues().toString());
    }
}
