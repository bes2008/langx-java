package com.jn.langx.test.security.ciphers;

import com.jn.langx.security.crypto.cipher.RSAs;
import com.jn.langx.security.crypto.key.PKIs;
import org.junit.Test;

import java.security.KeyPair;

public class CipherTests {

    @Test
    public void testRSA(){
        KeyPair keyPair = PKIs.createKeyPair("RSA",null,1024,null);

        byte[] raw= "123456".getBytes();
        byte[] encrypted = RSAs.encrypt(raw, keyPair.getPublic().getEncoded());
        byte[] message=RSAs.decrypt(encrypted, keyPair.getPrivate().getEncoded());
        System.out.println(new String(message));

    }


}
