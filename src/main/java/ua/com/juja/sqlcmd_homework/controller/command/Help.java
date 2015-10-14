package ua.com.juja.sqlcmd_homework.controller.command;
import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 12/10/2015.
 */
public class Help implements Command{
    private View view;

    public Help(View view){
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
            view.write("Existing commands:");
            view.write("\t 'connect|databaseName|userName|password - to connect to your database");
            view.write("\t 'list' - to show all existing table names");
            view.write("\t 'find|tableName' - to show table 'tableName' content");
            view.write("\t 'help' - to show help for this project");
            view.write("\t 'exit' - to exit the program");
    }
}
