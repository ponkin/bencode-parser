package ponkin.bencode.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexey Ponkin
 * @version 1, 10 Sep 2014
 */
public class ObjectSerializationTest {

    @Test
    public void testSerialization() throws Exception{
        // me want new job)
        Person me = new Person(
                "Alexey",
                "Ponkin",
                "+79859909188",
                Arrays.asList("java", "scala"),
                new Job("Yandex", "Java developer"));


        me.writeObject(new File("ponkin.dat"));

        BencodeStreamReader reader =
                BencodeInputFactory.getInstance()
                        .createBencodeStreamReader(new BufferedReader(new FileReader("ponkin.dat")));

        Person copyOfMe = new Person();
        Job copyOfJob = new Job();
        List<String> skills = null;
        String currentDictkey = null;
        while(reader.hasNext()){
            reader.next();
            switch(reader.getEventType()){
                case BencodeStreamReader.BYTESTRING:
                    String value = IOUtils.toString(reader.getContent());
                    if(currentDictkey == null){
                        currentDictkey = value;
                    }else{
                        if(currentDictkey.equals("firstName")){
                            copyOfMe.setFirstName(value);
                            currentDictkey = null;
                        }else if(currentDictkey.equals("lastName")){
                            copyOfMe.setLastName(value);
                            currentDictkey = null;
                        }else if(currentDictkey.equals("phone")){
                            copyOfMe.setPhone(value);
                            currentDictkey = null;
                        }else if(currentDictkey.equals("job.company")){
                            copyOfJob.setCompany(value);
                            currentDictkey = null;
                        }else if(currentDictkey.equals("job.position")){
                            copyOfJob.setPosition(value);
                            currentDictkey = null;
                        }else if(currentDictkey.equals("skills")){
                            skills.add(value);
                        }
                    }
                    break;
                case BencodeStreamReader.LIST_START:
                    skills = new LinkedList<String>();
                    break;
                case BencodeStreamReader.LIST_END:
                    copyOfMe.setSkills(skills);
                    currentDictkey = null;
                    break;
                case BencodeStreamReader.DICTIONARY_END:
                    copyOfMe.setJob(copyOfJob);
                    break;
            }
        }

        Assert.assertEquals(me, copyOfMe);


    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {

    private String firstName;

    private String lastName;

    private String phone;

    private List<String> skills;

    private Job job;


    public void writeObject(File out) throws IOException {

        BencodeStreamWriter writer = BencodeOutputFactory.createBencodeStreamWriter(out);
        writer.writeDictionaryStart();

        writer.writeByteString("firstName".getBytes()); // add value;
        writer.writeByteString(firstName.getBytes()); // add value;

        writer.writeByteString("lastName".getBytes()); // add value;
        writer.writeByteString(lastName.getBytes()); // add value;

        writer.writeByteString("phone".getBytes()); // add value;
        writer.writeByteString(phone.getBytes()); // add value;

        writer.writeByteString("skills".getBytes()); // add value;
        writer.writeListStart();
          for(String skill:skills){
            writer.writeByteString(skill.getBytes()); // add value;
          }
        writer.writeListEnd();

        writer.writeByteString("job.company".getBytes());
        writer.writeByteString(job.getCompany().getBytes());

        writer.writeByteString("job.position".getBytes());
        writer.writeByteString(job.getPosition().getBytes());


        writer.writeDictionaryEnd();
        writer.close();

    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Job {

    private String company;

    private String position;
}

