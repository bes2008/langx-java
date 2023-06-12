package com.jn.langx.test.security.messagedigest;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.stream.obj.SecureObjectInputStream;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.io.*;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestTest {
    private String filename = "MessageDigestTest.txt";
    private final static String SHA = "SHA";

    //	@Test
    public void saveDigestToFile() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(filename);
            file.deleteOnExit();
            file.createNewFile();
            fos = new FileOutputStream(file);
            MessageDigest md = MessageDigest.getInstance(SHA);
            oos = new ObjectOutputStream(fos);
            String data = "This have I thought good to deliver thee, that thou mightst not lose the dues of rejoicing " +
                    "by being ignorant of what greatness is promised thee.";
            byte[] buf = data.getBytes(Charsets.UTF_8);
            md.update(buf);
            oos.writeObject(data);
            oos.writeObject(md.digest());

            oos.close();
            validDigestFromFile();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOs.close(fos);
            IOs.close(oos);
        }
    }

    private void validDigestFromFile() {
        FileInputStream fis = null;
        SecureObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filename);
            ois = new SecureObjectInputStream(fis);
            Object o = ois.readObject(); // String data: original message
            if (!(o instanceof String)) {
                System.out.println("Unexpected data in file");
                return;
            }
            String data = (String) o;
            System.out.println("Got message : " + data);
            o = ois.readObject();   // byte[] : digest
            if (!(o instanceof byte[])) {
                System.out.println("Unexpected data in file");
                return;
            }
            byte[] origDigest = (byte[]) o;
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(data.getBytes(Charsets.UTF_8));
            if (MessageDigest.isEqual(md.digest(), origDigest)) {
                System.out.println("Message is valid");
            } else {
                System.out.println("Message was corrupted");
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOs.close(fis);
            IOs.close(ois);
        }
    }

    //	@Test
    public void md5Test() {
        System.out.println(MessageDigests.md5("hello"));
    }

    //	@Test
    public void updateMeger() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        String data = "This have I thought good to deliver thee, that thou mightst not lose the dues of rejoicing " +
                "by being ignorant of what greatness is promised thee.";
        String passphrase = "Sleep no more";
        byte[] dataBytes = data.getBytes(Charsets.UTF_8);
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
    public void saveWithDigestStream() {
        FileOutputStream fos = null;
        DigestOutputStream dos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(filename);
            file.deleteOnExit();
            file.createNewFile();
            fos = new FileOutputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA");
            dos = new DigestOutputStream(fos, md);
            oos = new ObjectOutputStream(dos);
            String data = "This have I thought good to deliver thee, " +
                    "that thou mightst not lose the dues of rejoicing " +
                    "by being ignorant of what greatness is promised thee.";
            oos.writeObject(data);    // original message
            dos.on(false);
            oos.writeObject(md.digest()); // digest


            readWithDigestStream();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOs.close(fos);
            IOs.close(dos);
            IOs.close(oos);
        }
    }

    private void readWithDigestStream() {
        FileInputStream fis = null;
        DigestInputStream dis = null;
        SecureObjectInputStream ois = null;
        try {
            File file = new File(filename);
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA");
            dis = new DigestInputStream(fis, md);
            ois = new SecureObjectInputStream(dis);
            Object o = ois.readObject();    // original message
            if (!(o instanceof String)) {
                System.out.println("Unexpected data in file");
                return;
            }
            String data = (String) o;
            System.out.println("Got message : " + data);
            dis.on(false);
            o = ois.readObject(); // digest
            if (!(o instanceof byte[])) {
                System.out.println("Unexpected data in file");
                return;
            }
            byte origDigest[] = (byte[]) o;
            if (MessageDigest.isEqual(md.digest(), origDigest)) {
                System.out.println("Message is valid");
            } else {
                System.out.println("Message was corrupted");
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOs.close(ois);
            IOs.close(dis);
            IOs.close(fis);
        }
    }

    @Test
    public void testSha256() {
        System.out.println(Base64.encodeBase64String(MessageDigests.digest(JCAEStandardName.SHA_256.getName(), "123456")));
    }
}