package sqlcmd_homework.view;

import java.util.Scanner;

/**
 * Created by Sims on 12/09/2015.
 */
public class Console implements View {
    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner (System.in);
        return scanner.nextLine();
    }
}
