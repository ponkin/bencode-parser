package ponkin.bencode.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeOutputFactory {


    public static BencodeStreamWriter createBencodeStreamWriter(final File file) throws IOException {
        return new BencodeStreamWriterImpl(BencodeBufferFactory.createBencodeWriteBuffer(new FileOutputStream(file).getChannel()));
    }

    public static BencodeStreamWriter createBencodeStreamWriter(final Writer out) throws IOException {
        return new BencodeStreamWriterImpl(BencodeBufferFactory.createBencodeWriteBuffer(out));
    }

}

class BencodeStreamWriterImpl implements BencodeStreamWriter {

    private final BencodeWriteBuffer buffer;

    public BencodeStreamWriterImpl(BencodeWriteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void writeDictionaryStart() throws IOException {
        buffer.addToBuffer((byte) 'd');
    }

    @Override
    public void writeDictionaryEnd() throws IOException {
        writeObjectEnd();
    }

    @Override
    public void writeListStart() throws IOException {
        buffer.addToBuffer((byte) 'l');
    }

    @Override
    public void writeListEnd() throws IOException {
        writeObjectEnd();
    }

    @Override
    public void writeInteger(int value) throws IOException {
        buffer.addToBuffer((byte) 'i');
        buffer.addToBuffer(Integer.toString(value).getBytes());
        writeObjectEnd();
    }

    @Override
    public void writeByteString(byte[] value) throws IOException {
        buffer.addToBuffer(Integer.toString(value.length).getBytes());
        buffer.addToBuffer((byte) ':');
        buffer.addToBuffer(value);
    }

    private void writeObjectEnd() throws IOException {
        buffer.addToBuffer((byte) 'e');
    }

    @Override
    public void flush() throws IOException {
        buffer.flush();
    }

    @Override
    public void close() throws IOException {
        buffer.close();
    }


}



