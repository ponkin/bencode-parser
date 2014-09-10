package ponkin.bencode.api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeFileWriteBuffer implements BencodeWriteBuffer {

    private final FileChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(10240);

    public BencodeFileWriteBuffer(FileChannel channel) {
        this.channel = channel;
    }

    @Override
    public void addToBuffer(byte[] data) throws IOException {
        if (data.length + buffer.position() >= buffer.limit()) {
            flush();
        }
        buffer.put(data);
    }

    @Override
    public void addToBuffer(byte b) throws IOException {
        if (1 + buffer.position() >= buffer.limit()) {
            flush();
        }
        buffer.put(b);
    }

    @Override
    public void flush() throws IOException {
        buffer.flip();
        channel.write(buffer);
    }

    @Override
    public void close() throws IOException {
        flush();
        channel.close();
    }
}
