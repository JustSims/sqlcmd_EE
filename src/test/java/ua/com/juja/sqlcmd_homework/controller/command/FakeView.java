package ua.com.juja.sqlcmd_homework.controller.command;

import ua.com.juja.sqlcmd_homework.view.View;

/**
 * Created by Sims on 26/10/2015.
 */
public class FakeView implements View {

    private String consoleMessages = "";

    @Override
    public void write(String consoleMessage) {
        consoleMessages += consoleMessage + "\n";
    }

    @Override
    public String read() {
        return null;
    }

    public String getContent() {
        return consoleMessages;
    }
}
