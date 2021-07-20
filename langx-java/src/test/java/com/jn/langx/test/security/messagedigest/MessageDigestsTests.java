package com.jn.langx.test.security.messagedigest;

import com.jn.langx.security.crypto.digest.MessageDigests;
import org.junit.Test;


public class MessageDigestsTests {
    @Test
    public void test(){
        String str = MessageDigestsTests.class.getName();
        System.out.println(MessageDigests.getDigestHexString("MD2", str));
        System.out.println(MessageDigests.getDigestHexString("MD4", str));
        System.out.println(MessageDigests.getDigestHexString("MD5", str));
        System.out.println(MessageDigests.getDigestHexString("SM3", str));
    }
}
