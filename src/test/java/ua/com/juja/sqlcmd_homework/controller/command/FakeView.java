package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 26/10/2015.
 */
public class FakeView implements View {

    private String consoleMessages = "";
    private String input;

    @Override
    public void write(String consoleMessage) {
        consoleMessages += consoleMessage + "\n";
    }

    @Override
    public String read() {
        if (this.input == null){
            throw new IllegalStateException("You should initialise read() method");
        }
        String result = this.input;
        this.input = null;
        return result;
    }
    public void addRead(String input){
        this.input = input;

    }

    public String getContent() {
        return consoleMessages;
    }
}
