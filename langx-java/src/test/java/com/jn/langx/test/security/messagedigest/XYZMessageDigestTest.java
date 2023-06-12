package com.jn.langx.test.security.messagedigest;

import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.security.MessageDigest;
import java.security.Security;

public class XYZMessageDigestTest {
    private static String filename = "MessageDigestTest.txt";

    static {
        Security.addProvider(new XYZProvider());
    }

    public static void main(String[] args) throws Exception {
        XYZMessageDigestTest test = new XYZMessageDigestTest();
        test.writeMessage();
        test.readMessage();
    }

    public void writeMessage() throws Exception {
        File file = new File(filename);
        file.deleteOnExit();
        file.createNewFile();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            MessageDigest md = MessageDigest.getInstance("XYZ");
            String data = "This have I thought good to deliver thee, " +
                    "that thou mightst not lose the dues of rejoicing " +
                    "by being ignorant of what greatness is promised thee.";
            byte[] buf = data.getBytes(Charsets.UTF_8);
            md.update(buf);
            oos.writeObject(data);  // original message
            oos.writeObject(md.digest()); // digest
        } finally {
            IOs.close(oos);
            IOs.close(fos);
        }
    }

    public void readMessage() throws Exception {
        File file = new File(filename);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

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
            MessageDigest md = MessageDigest.getInstance("XYZ");
            md.update(data.getBytes(Charsets.UTF_8));
            if (MessageDigest.isEqual(md.digest(), origDigest)) {
                System.out.println("Message is valid");
            }
            else {
                System.out.println("Message was corrupted");
            }

            MessageDigest md2 = MessageDigest.getInstance("SHA");
            md2.update(data.getBytes());
            if (MessageDigest.isEqual(md2.digest(), origDigest)) {
                System.out.println("Message is valid");
            } else {
                System.out.println("Message was corrupted");
            }
        } finally {
            IOs.close(ois);
            IOs.close(fis);
        }

    }
}