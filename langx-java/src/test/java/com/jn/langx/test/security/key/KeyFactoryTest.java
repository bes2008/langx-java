package com.jn.langx.test.security.key;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.DSAPublicKeySpec;

public class KeyFactoryTest {
    private static final String DSA="DSA";
    private static final String keyspecFile="keyspec.text";

    @Test
    public void genenatePublicKey() throws Exception{
        writeKeySpec();
        readKeySpec();
    }

    private void writeKeySpec() throws Exception {
        File file=new File(keyspecFile);
        file.deleteOnExit();
        file.createNewFile();

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(DSA);
        keyGen.initialize(512, new SecureRandom());
        KeyPair keyPair=keyGen.generateKeyPair();

        KeyFactory factory=KeyFactory.getInstance(DSA);
        DSAPublicKeySpec keySpec=factory.getKeySpec(keyPair.getPublic(), DSAPublicKeySpec.class);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(keySpec.getY());
        oos.writeObject(keySpec.getP());
        oos.writeObject(keySpec.getQ());
        oos.writeObject(keySpec.getG());
        oos.flush();
        oos.close();
    }

    private void readKeySpec() throws Exception {
        KeyFactory factory=KeyFactory.getInstance(DSA);
        FileInputStream fis = new FileInputStream(keyspecFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        DSAPublicKeySpec keySpec = new DSAPublicKeySpec(
                (BigInteger) ois.readObject(),
                (BigInteger) ois.readObject(),
                (BigInteger) ois.readObject(),
                (BigInteger) ois.readObject());
        ois.close();
        PublicKey puk=factory.generatePublic(keySpec);
        System.out.println("Got private key:\n"+puk);
    }
}