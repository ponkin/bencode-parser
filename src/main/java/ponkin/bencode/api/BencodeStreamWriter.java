package ponkin.bencode.api;

import java.io.IOException;

/**
 * Simple writer interface
 *
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public interface BencodeStreamWriter {


    void writeDictionaryStart() throws IOException;

    void writeDictionaryEnd() throws IOException;

    void writeListStart() throws IOException;

    void writeListEnd() throws IOException;

    void writeInteger(int value) throws IOException;

    void writeByteString(byte[] value) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;

}
