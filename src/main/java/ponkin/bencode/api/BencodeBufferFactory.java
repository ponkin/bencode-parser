package ponkin.bencode.api;

import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeBufferFactory {

    public static BencodeWriteBuffer createBencodeWriteBuffer(FileChannel channel) {
        return new BencodeFileWriteBuffer(channel);
    }

    public static BencodeWriteBuffer createBencodeWriteBuffer(Writer out) {
        return new BencodeByteArrayWriteBuffer(out);
    }

    public static BencodeReadBuffer createBencodeReadBuffer(int dataLimit){
        return new BencodeCopyReadBuffer(dataLimit);
    }

    public static BencodeReadBuffer createBencodeReadBuffer(byte[] data){
        return new BencodeArrayReadBuffer(data);
    }
}