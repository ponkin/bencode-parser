package ponkin.bencode.api;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeByteArrayWriteBuffer implements BencodeWriteBuffer {

    private final Writer out;

    public BencodeByteArrayWriteBuffer(Writer out) {
        this.out = out;
    }

    @Override
    public void addToBuffer(byte[] data) throws IOException {
        for (int i = 0; i < data.length; i++)
            addToBuffer(data[i]);
    }

    @Override
    public void addToBuffer(byte b) throws IOException {
        out.write(b);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
