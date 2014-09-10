package ponkin.bencode.api;

import java.io.IOException;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */

interface BencodeWriteBuffer {

    void addToBuffer(byte[] data) throws IOException;

    void addToBuffer(byte b) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}
