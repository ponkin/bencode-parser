package ponkin.bencode.api;

import java.nio.ByteBuffer;

/**
 * Read from underlying byte array, no buffering
 *
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeArrayReadBuffer implements BencodeReadBuffer{

    private final byte[] data_buffer;
    private int position;

    public BencodeArrayReadBuffer(byte[] data){
        this.data_buffer = data;
        this.position = 0;
    }

    @Override
    public void put(int b) {
        // No need to copy data just increment position;
        position++;
    }

    @Override
    public int position() {
        return this.position + 1;
    }

    @Override
    public ByteBuffer getData() {
        return ByteBuffer.wrap(this.data_buffer);
    }

}
