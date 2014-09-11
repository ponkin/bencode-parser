package ponkin.bencode.parser;

import ponkin.bencode.collection.LongDynamicArray;
import java.nio.LongBuffer;

/**
 *  Class represents index over bencode input stream.
 *  Index is packed in long( 3 bit for element type, 32 bit for data length, and 29 for position)
 *
 *  TODO: Need to pack index in 3 bit - type, 64 bit - length, 64 bit for position.
 *  TODO: but it will cost more memory usage.
 *
 *  @author Alexey Ponkin
 *  @version 1, 10 Sep 2014
 */
public class ElementIndex {

    private final LongDynamicArray buffer; // Buffer for storing indexes

    public static final long TYPE_UNMASK = 0xFFFFFFFFE0000000L;
    public static final long TYPE_MASK =   0x7L;
    public static final long LEN_UMASK =   0x1FFFFFFFE0000000L;
    public static final long POS_UMASK =   0x000000001FFFFFFFL;

    public ElementIndex(int indexLimit){
        buffer = new LongDynamicArray(indexLimit, indexLimit);
    }

    public int addIndex(byte type, int pos, int len){
        int currPos = buffer.position();
        buffer.append(pack(type, pos, len));
        return currPos;
    }

    public void setLen(int idx, int len){
        long value = buffer.get(idx);
        value = (value & ~LEN_UMASK);
        buffer.put(idx, (((long)len) << 29) | value );
    }

    public long getLen(int idx){
        return ulen(buffer.get(idx));
    }

    public long getPos(int idx){
        return upos(buffer.get(idx));
    }

    public long getType(int idx){
        return utype(buffer.get(idx));
    }

    private static long pack(byte type, int pos, int len){
        return (TYPE_MASK & type) << 61 | ((long)len) << 29 | (POS_UMASK & pos);
    }

    public static long utype(long index){
        return ( TYPE_UNMASK  & index ) >>> 61;
    }

    public static long ulen(long index){
        return (LEN_UMASK & index) >>> 29;
    }

    public static long upos(long index){
        return (POS_UMASK & index);
    }

    public LongBuffer getBuffer(){
		buffer.compact();
		return LongBuffer.wrap(buffer.array()).asReadOnlyBuffer();
    }

    @Override
    public String toString(){
        String result = "";
        for(int i=0;i<buffer.position();i++){
            result = result + String.format("%d - type=%d, pos=%d, len=%d\n", i, getType(i), getPos(i), getLen(i));
        }
        return result;
    }

}
