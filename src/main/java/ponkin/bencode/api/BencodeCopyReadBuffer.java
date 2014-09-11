package ponkin.bencode.api;

import ponkin.bencode.collection.ByteDynamicArray;
import java.nio.ByteBuffer;

/**
 * Copy Input stream in memory
 *
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeCopyReadBuffer implements BencodeReadBuffer {

    final ByteDynamicArray data_buffer;

    public BencodeCopyReadBuffer(int dataLimit){
        this.data_buffer  = new ByteDynamicArray( 8024, dataLimit);//TODO remove hardcode
    }

    @Override
    public void put(int b) {
        this.data_buffer.append((byte)b);
    }

    @Override
    public int position() {
        return this.data_buffer.position();
    }

    @Override
    public ByteBuffer getData() {
        return ByteBuffer.wrap(this.data_buffer.array(), 0, this.data_buffer.position()).asReadOnlyBuffer();
    }
}
