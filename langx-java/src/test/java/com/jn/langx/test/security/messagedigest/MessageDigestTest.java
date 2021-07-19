package com.jn.langx.test.security.messagedigest;

import com.jn.langx.security.crypto.messagedigest.MessageDigests;
import org.junit.Test;

import java.io.*;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestTest {
    private String filename="MessageDigestTest.txt";
    private final static String SHA="SHA";

    //	@Test
    public void saveDigestToFile(){
        try {
            File file=new File(filename);
            file.deleteOnExit();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            MessageDigest md = MessageDigest.getInstance(SHA);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String data = "This have I thought good to deliver thee, that thou mightst not lose the dues of rejoicing " +
                    "by being ignorant of what greatness is promised thee.";
            byte buf[] = data.getBytes();
            md.update(buf);
            oos.writeObject(data);
            oos.writeObject(md.digest());

            oos.close();
            validDigestFromFile();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void validDigestFromFile(){
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject(); // String data: original message
            if (!(o instanceof String)) {
                System.out.println("Unexpected data in file");
                System.exit(-1);
            }
            String data = (String) o;
            System.out.println("Got message : " + data);
            o = ois.readObject();   // byte[] : digest
            if (!(o instanceof byte[])) {
                System.out.println("Unexpected data in file");
                System.exit(-1);
            }
            byte origDigest[] = (byte []) o;
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(data.getBytes());
            if (MessageDigest.isEqual(md.digest(), origDigest))
                System.out.println("Message is valid");
            else System.out.println("Message was corrupted");

            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //	@Test
    public void md5Test(){
        System.out.println(MessageDigests.md5("hello"));
    }

    //	@Test
    public void updateMeger() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        String data = "This have I thought good to deliver thee, that thou mightst not lose the dues of rejoicing " +
                "by being ignorant of what greatness is promised thee.";
        String passphrase = "Sleep no more";
        byte dataBytes[] = data.getBytes();
        byte passBytes[] = passphrase.getBytes();
        md.update(passBytes);
        md.update(dataBytes);
        byte[] digest1 = md.digest();

        md.update(passBytes);
        md.update(dataBytes);
        byte[] digest2 = md.digest();

        byte[] digest3 = md.digest();
        System.out.println(MessageDigest.isEqual(digest1, digest2));
        System.out.println(MessageDigest.isEqual(digest1, digest3));
    }

    @Test
    public void saveWithDigestStream(){
        try {
            File file=new File(filename);
            file.deleteOnExit();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestOutputStream dos = new DigestOutputStream(fos, md);
            ObjectOutputStream oos = new ObjectOutputStream(dos);
            String data = "This have I thought good to deliver thee, "+
                    "that thou mightst not lose the dues of rejoicing " +
                    "by being ignorant of what greatness is promised thee.";
            oos.writeObject(data);	// original message
            dos.on(false);
            oos.writeObject(md.digest()); // digest

            oos.close();

            readWithDigestStream();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void readWithDigestStream(){
        try {
            File file=new File(filename);
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA");
            DigestInputStream dis = new DigestInputStream(fis, md);
            ObjectInputStream ois = new ObjectInputStream(dis);
            Object o = ois.readObject();	// original message
            if (!(o instanceof String)) {
                System.out.println("Unexpected data in file");
                System.exit(-1);
            }
            String data = (String) o;
            System.out.println("Got message : " + data);
            dis.on(false);
            o = ois.readObject(); // digest
            if (!(o instanceof byte[])) {
                System.out.println("Unexpected data in file");
                System.exit(-1);
            }
            byte origDigest[] = (byte []) o;
            if (MessageDigest.isEqual(md.digest(), origDigest))
                System.out.println("Message is valid");
            else System.out.println("Message was corrupted");

            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}