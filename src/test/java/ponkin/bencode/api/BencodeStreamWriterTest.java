package ponkin.bencode.api;

import org.apache.commons.io.output.StringBuilderWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeStreamWriterTest {

    @Test
    public void testWrite() throws Exception{
        StringBuilderWriter out = new StringBuilderWriter();
        BencodeStreamWriter writer = BencodeOutputFactory.createBencodeStreamWriter(out);

        writer.writeDictionaryStart();
        writer.writeInteger(1); // add key to dictionary
        writer.writeByteString("VALUE".getBytes()); // add value;
        writer.writeDictionaryEnd();
        writer.close();

        Assert.assertEquals(out.toString(), "di1e5:VALUEe");
    }
}
