package ua.com.juja.sqlcmd_homework.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd_homework.model.DataSet;
import ua.com.juja.sqlcmd_homework.model.DataSetImplemented;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import static org.junit.Assert.assertEquals;

/**
 * Created by Sims on 27/10/2015.
 */
public class FindTestWithMockito {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setUp(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Find(manager, view);
    }


    @Test
    public void testPrintTableData(){
        //given
        setUpTableColumns("user", "id", "name", "password");

        DataSet user1 = new DataSetImplemented();
        user1.put("id", 12);
        user1.put("name", "userName1");
        user1.put("password", "*****");

        DataSet user2 = new DataSetImplemented();
        user2.put("id", 13);
        user2.put("name", "userName2");
        user2.put("password", "+++++");
        when(manager.getTableData("user")).thenReturn(Arrays.asList(user1, user2));

        //when
        command.process("find|user");

        //then
        String expected =
                "[-----------------," +
                " id|name|password|," +
                " -----------------," +
                " 12|userName1|*****|," +
                " 13|userName2|+++++|," +
                " -----------------]";
        print(expected);
    }

    private void setUpTableColumns(String tableName, String... columns) {
        when(manager.getTableColumns(tableName)).
                thenReturn(new LinkedHashSet<String>(Arrays.asList(columns)));
    }

    private void print(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParameters() {
        //given

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


        //when
        boolean canProcess = command.canProcess("qwe|user");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData(){
        //given
        setUpTableColumns("user", "id", "name", "password");

        when(manager.getTableData("user")).thenReturn(new ArrayList<DataSet>());

        //when
        command.process("find|user");

        //then
        print("[-----------------," +
              " id|name|password|," +
              " -----------------," +
              " -----------------]");
    }

    @Test
    public void testPrintTableDataWithOneColumn(){
        //given
        setUpTableColumns("test", "id");

        DataSet user1 = new DataSetImplemented();
        user1.put("id", 12);

        DataSet user2 = new DataSetImplemented();
        user2.put("id", 13);

        when(manager.getTableData("test")).thenReturn(Arrays.asList(user1, user2));

        //when
        command.process("find|test");

        //then
        String expected =
                "[-----------------," +
                " id|, -----------------," +
                " 12|," +
                " 13|," +
                " -----------------]";
        print(expected);
    }
}
