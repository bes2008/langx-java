package com.jn.langx.test.security.key;

import com.jn.langx.io.stream.ByteArrayOutputStream;
import com.jn.langx.security.KeyFileIOs;
import com.jn.langx.security.PKIs;
import com.jn.langx.util.io.Charsets;

import java.security.KeyPair;

public class KeyFileIOTests {
    public static void main(String[] args) throws Throwable{
        KeyPair keyPair = PKIs.createKeyPair("RSA", null, 1024, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        KeyFileIOs.writeKey(keyPair.getPublic().getEncoded(), byteArrayOutputStream, KeyFileIOs.KeyFormat.BASE64, "------ PUBLIC KEY ------", "------ END PUBLIC KEY -------");
        System.out.println(byteArrayOutputStream.toString(Charsets.UTF_8));
    }
}
