package com.jn.langx.gmssl.tests;

import org.gmssl.GmSSL;
import org.junit.Test;

public class GmsslSM4Tests {
    @Test
    public void test() {
        GmSSL gmSSL = new GmSSL();

        // SMS4加解密，替换des..
        byte[] key = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] s1 = gmSSL.symmetricEncrypt("SMS4", "1234566".getBytes(), key, iv);
        byte[] s2 = gmSSL.symmetricDecrypt("SMS4", s1, key, iv);
    }
}
