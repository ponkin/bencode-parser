package ponkin.bencode.parser;

import ponkin.bencode.api.BencodeStreamReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Actual parser.
 * It copies input stream into internal buffer and build element index over that data.
 *
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeParser{


    private final ByteBuffer data_buffer;// copy of bencode stream - with limit
    private final ByteBuffer cmd_buffer; // internal command buffer
    private final ElementIndex index; // index of elements over the data_buffer
    private final ElementIndexStack objStack; // temporary index stack stack

    private byte dataType = -1; // current element`s data type
    private int byteStringLen = -1; // if element is byte string, variable holds its length

    public BencodeParser(int dataLimit, int commandLimit, int indexLimit, int elementStackLimit){
        data_buffer = ByteBuffer.allocate(dataLimit);
        cmd_buffer =  ByteBuffer.wrap(new byte[commandLimit]);
        index = new ElementIndex(indexLimit);
        objStack = new ElementIndexStack(elementStackLimit);
    }

    protected void startDictionary(Reader in) throws IOException {
        cmd_buffer.clear();
        dataType = BencodeStreamReader.DICTIONARY_START;
        objStack.push(addToIndex(in));
    }

    protected void startList(Reader in) throws IOException{
        cmd_buffer.clear();
        dataType = BencodeStreamReader.LIST_START;
        objStack.push(addToIndex(in));
    }

    protected void startInteger(Reader in) throws IOException{
        cmd_buffer.clear();
        dataType = BencodeStreamReader.INTEGER;
        addToIndex(in);
    }

    protected void startByteString(Reader in) throws IOException{
        String cmd = getString(cmd_buffer);
        if(cmd != null) cmd = cmd.trim();
        cmd_buffer.clear();
        dataType = BencodeStreamReader.BYTESTRING;
        byteStringLen = Integer.parseInt(cmd);
        addToIndex(in);
    }

    protected void endElement() throws IOException{
        int obj_idx = objStack.pop();
        index.setLen(obj_idx, data_buffer.position());
        switch((int)index.getType(obj_idx)){
            case BencodeStreamReader.LIST_START:
                index.addIndex(BencodeStreamReader.LIST_END, data_buffer.position(), 0);
                break;
            case BencodeStreamReader.DICTIONARY_START:
                index.addIndex(BencodeStreamReader.DICTIONARY_END, data_buffer.position(), 0);
                break;
        }
    }


    public void parse(Reader in) throws IOException{
        int b = 0;
        while( (b = in.read()) != -1 ){
            data_buffer.put((byte)b);
            switch(b){
                case 'l':
                    startList(in);
                    break;
                case 'd':
                    startDictionary(in);
                    break;
                case 'i':
                    startInteger(in);
                    break;
                case ':':
                    startByteString(in);
                    break;
                case 'e':
                    endElement();
                    break;
                default: cmd_buffer.put((byte)b);

            }

        }
    }


    protected int addToIndex(Reader in) throws IOException{
        int pos = data_buffer.position();
        int len = parseDataType(in);
        return index.addIndex(dataType, pos, len);
    }

    /**
     * Parse forcomming scalar types such as numbers and byte strings.
     * Other types don`t have scalar content
     */
    protected int parseDataType(Reader in) throws IOException{
        int b = 0;
        if(dataType == BencodeStreamReader.INTEGER){
            int k = 0;
            while( (b = in.read()) != 'e' ){
                data_buffer.put((byte)b);
                k++;
            }
            return k;
        }else if(dataType == BencodeStreamReader.BYTESTRING && byteStringLen > 0){
            int i = 0;
            while( i++ < byteStringLen){
                byte с = (byte)in.read();
                data_buffer.put(с);
            }
            return byteStringLen;
        }
        return 0;
    }

    public ElementIndex getElementIndex(){
        return this.index;
    }

    public ByteBuffer getData(){
        return this.data_buffer.asReadOnlyBuffer();
    }


    public static String getString(ByteBuffer buffer){
        return new String(buffer.array(), 0, buffer.position()).intern();
    }

    private class ElementIndexStack{

        private final IntBuffer buffer;

        public ElementIndexStack(int limit){
            buffer = IntBuffer.allocate(limit);
        }

        public void push(int i){
            buffer.put(i);
        }

        public int pop(){
            int pos = buffer.position();
            int i = buffer.get(pos-1);
            buffer.position(pos-1);
            return i;
        }
    }
}