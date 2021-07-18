package com.jn.langx.java8.tests.security.gmssl;


import com.github.gitveio.GmSSL;

public class GmSSLTests {

    public static void main(String[] args) {
        final GmSSL gmssl = new GmSSL();

        /*
        String[] versions = gmssl.getVersions();
        if (versions != null) {
            for (String v : versions) {
                System.out.println(v);
            }
        } else {
            System.out.println("no versions");
        }
        */
        byte[] key = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};
        byte[] iv = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};
        byte[] s1 = gmssl.symmetricEncrypt("SMS4", "1234566".getBytes(), key, iv);
        byte[] s2 = gmssl.symmetricDecrypt("SMS4", s1, key, iv);

// SM3哈希，替换md5/sha128...
        byte[] s3 = gmssl.digest("SM3", "adfd".getBytes());
        for (int i = 0; i < s3.length; i++) {
            System.out.printf("%02X", s3[i]);
        }
    }

}
