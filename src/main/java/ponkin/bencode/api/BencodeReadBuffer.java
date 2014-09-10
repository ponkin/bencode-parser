package ponkin.bencode.api;

import java.nio.ByteBuffer;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public interface BencodeReadBuffer {

    void put(int b);

    int position();

    ByteBuffer getData();

}
