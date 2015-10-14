package integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sims on 13/10/2015.
 */
public class LogOutputStream extends OutputStream {
    private String log;
    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[] {(byte) (b & 0xFFFF00 >> 8),(byte) (b & 0x00FF)};
        log += new String (bytes, "UTF-8");
    }

}
