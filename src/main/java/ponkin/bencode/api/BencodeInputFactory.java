package ponkin.bencode.api;

import ponkin.bencode.parser.BencodeParser;
import ponkin.bencode.parser.ElementIndex;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.Properties;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeInputFactory {

    /**
     * Limit maximum data size in bytes
     */
    public static final String DATA_LIMIT = "bencode.data.limit";

    /**
     * Limit maximum command length i.e. if you have large byte strings
     */
    public static final String COMMAND_LIMIT = "bencode.command.max.length";

    /**
     * Limit size of index6 if you have large amount of objects encoded
     */
    public static final String INDEX_LIMIT = "bencode.index.limit";

    /**
     * Limit size of internal element stack - if you have very deep object tree inside encoded data
     */
    public static final String STACK_LIMIT = "bencode.stack.limit";


    private static BencodeInputFactory factory;

    private final int dataLimit;
    private final int commandLimit;
    private final int indexLimit;
    private final int stackLimit;


    public static BencodeInputFactory getInstance(){
        return getInstance(System.getProperties());
    }

    public static BencodeInputFactory getInstance(Properties properties){
        if(factory == null){
            factory = new BencodeInputFactory(
                    Integer.parseInt(properties.getProperty(DATA_LIMIT, "83886")),
                    Integer.parseInt(properties.getProperty(COMMAND_LIMIT, "32")),
                    Integer.parseInt(properties.getProperty(INDEX_LIMIT, "10000")),
                    Integer.parseInt(properties.getProperty(STACK_LIMIT, "5120"))
            );
        }
        return factory;
    }

    private BencodeInputFactory(int dataLimit, int commandLimit, int indexLimit, int stackLimit){
        this.dataLimit = dataLimit;
        this.stackLimit = stackLimit;
        this.commandLimit = commandLimit;
        this.indexLimit = indexLimit;

    }


    public BencodeStreamReader createBencodeStreamReader(String bencodedText) throws IOException{
        return createBencodeStreamReader(new StringReader(bencodedText));
    }


    public BencodeStreamReader createBencodeStreamReader(Reader reader) throws IOException{
        final BencodeParser parser = new BencodeParser(dataLimit, commandLimit, indexLimit, stackLimit); //TODO set properties
        parser.parse(reader);
        final ElementIndex index = parser.getElementIndex();
        return new BencodeStreamReader(){

            final ByteBuffer data = parser.getData();
            final LongBuffer arrayIndex = index.getBuffer();

            long currentIndex = 0L;

            @Override
            public byte getEventType() {
                return (byte)ElementIndex.utype(currentIndex);
            }

            @Override
            public InputStream getContent() {
                byte[] content = new byte[(int)ElementIndex.ulen(currentIndex)];
                data.position((int)ElementIndex.upos(currentIndex));
                data.get(content);
                return new ByteArrayInputStream(content);
            }

            @Override
            public boolean hasNext() {
                return arrayIndex.hasRemaining();
            }

            @Override
            public Void next() {
                currentIndex = arrayIndex.get();
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can not remove index.");
            }
        };
    }
}
