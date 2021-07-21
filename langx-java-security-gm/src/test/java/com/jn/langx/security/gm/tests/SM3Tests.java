package com.jn.langx.security.gm.tests;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.gm.crypto.sm3.SM3MessageDigest;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;


public class SM3Tests {
    private String string = "abcde_12345";

    @Test
    public void test() {
        System.out.println(MessageDigests.getDigestHexString("SM3", string));
        System.out.println(Hex.encodeHexString(new SM3MessageDigest().digest(string.getBytes(Charsets.UTF_8))));
    }
}
