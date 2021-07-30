package com.jn.langx.security.gm.tests.bc;

import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.gm.bc.GmBCInitializer;
import com.jn.langx.util.io.Charsets;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.junit.Test;


public class SM3Tests {
    private String string = "abcde_12345";

    @Test
    public void test() {
        new GmBCInitializer();
        System.out.println(MessageDigests.getDigestHexString("SM3", string));
        System.out.println(MessageDigests.getDigestHexString("SM3", string));
        System.out.println(Hex.encodeHexString(new SM3.Digest().digest(string.getBytes(Charsets.UTF_8))));

    }
}
