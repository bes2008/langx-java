package com.jn.langx.security.gm.tests;

import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.text.StringTemplates;
import org.junit.Test;


public class MessageDigestsTests {
    @Test
    public void test(){
        String str = MessageDigestsTests.class.getName();
        digestAndPrint("MD2", str);
        digestAndPrint("MD4", str);
        digestAndPrint("MD5", str);

        digestAndPrint("SM3", str);
        // sha1
        digestAndPrint("SHA1", str);
        digestAndPrint("SHA-1", str);

        // sha2
        digestAndPrint("SHA-224", str);
        digestAndPrint("SHA-256", str);
        digestAndPrint("SHA-384", str);
        digestAndPrint("SHA-512", str);

        // sha3
        digestAndPrint("SHA3", str);
        digestAndPrint("SHA-3", str);
        digestAndPrint("SHA3-224", str);
        digestAndPrint("SHA3-256", str);
        digestAndPrint("SHA3-384", str);
        digestAndPrint("SHA3-512", str);

    }

    private void digestAndPrint(String algorithm, String content){
        System.out.println(StringTemplates.formatWithPlaceholder("{}\t\t: {}",algorithm, MessageDigests.getDigestHexString(algorithm, content)));
    }
}
