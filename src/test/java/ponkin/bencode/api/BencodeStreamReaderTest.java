package ponkin.bencode.api;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class BencodeStreamReaderTest {

    @Test
    public void testContentRead() throws Exception{
        BencodeInputFactory factory = BencodeInputFactory.getInstance();
        BencodeStreamReader reader = factory.createBencodeStreamReader("di894e4:spam8:simpsons5:xpenye");

        String result = "";
        while(reader.hasNext()){
            reader.next();
            switch(reader.getEventType()){
                case  BencodeStreamReader.BYTESTRING:
                    result = result + IOUtils.toString(reader.getContent());
                    break;
            }
        }
        Assert.assertEquals("spamsimpsonsxpeny", result);

    }
}
