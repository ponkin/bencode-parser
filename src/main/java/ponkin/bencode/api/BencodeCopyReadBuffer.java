package ponkin.bencode.api;

import java.nio.ByteBuffer;

/**
 * Copy Input stream in memory
 *
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeCopyReadBuffer implements BencodeReadBuffer {

    final ByteBuffer data_buffer;

    public BencodeCopyReadBuffer(int dataLimit){
        this.data_buffer  = ByteBuffer.allocate(dataLimit);
    }

    @Override
    public void put(int b) {
        this.data_buffer.put((byte)b);
    }

    @Override
    public int position() {
        return this.data_buffer.position();
    }

    @Override
    public ByteBuffer getData() {
        return this.data_buffer.asReadOnlyBuffer();
    }
}
