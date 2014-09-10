package ponkin.bencode.api;


import java.io.InputStream;
import java.util.Iterator;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public interface BencodeStreamReader extends Iterator<Void> {

    byte BYTESTRING = 0;
    byte LIST_START = 1;
    byte INTEGER    = 2;
    byte DICTIONARY_START = 3;
    byte DICTIONARY_END = 4;
    byte LIST_END = 5;


    byte getEventType();


    InputStream getContent();



}
