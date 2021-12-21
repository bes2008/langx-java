package com.jn.langx.test.security.key;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.stream.ByteArrayOutputStream;
import com.jn.langx.security.crypto.key.spec.KeyEncoding;
import com.jn.langx.security.crypto.key.spec.pem.PemFileIOs;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.junit.Test;

import java.security.KeyPair;

public class KeyFileIOTests {

    @Test
    public void test0() throws Throwable{
        KeyPair keyPair = PKIs.createKeyPair("RSA", null, 1024, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PemFileIOs.writeKey(keyPair.getPublic().getEncoded(), byteArrayOutputStream, KeyEncoding.BASE64, "------ PUBLIC KEY ------", "------ END PUBLIC KEY -------");
        System.out.println(byteArrayOutputStream.toString(Charsets.UTF_8));
        IOs.close(byteArrayOutputStream);

        byteArrayOutputStream = new ByteArrayOutputStream();
        PemFileIOs.writeKey(keyPair.getPrivate(), byteArrayOutputStream, KeyEncoding.BASE64,null,null);
        System.out.println(byteArrayOutputStream.toString(Charsets.UTF_8));
        IOs.close(byteArrayOutputStream);


        System.out.println("====================================================================");

        KeyPair keyPair2 = PKIs.createKeyPair("RSA", null, 1024, null);
        PemFileIOs.writeKey(keyPair2.getPublic().getEncoded(), System.out, KeyEncoding.BASE64, "------ PUBLIC KEY ------", "------ END PUBLIC KEY -------");
    }

    @Test
    public void pkcs1_vs_pkcs8(){
        String pkcs1_base64ed="MIICWwIBAAKBgQC9XrJWcWbj0LhDBzN4uwEOLA/UJKmCkkbvlVgN/qei3e/jVFpx" +
                "R6D3fzshnv5QNB4+BJ/rjRWbbxCJ0djzPxsLS1dJ+bDwagZWZ9hNXARTq4K0uxw6" +
                "Ol5jGD9Od6w5n5uxyaEk9/edvYwMhthIxC/uADRp2pNSutwyLX3bUJnHZwIDAQAB" +
                "AoGANN3S+7788my6hDvmarYKPWKfqKHzkLg1hX0z7/Q/6H/9EIHkHevZTD8AywoQ" +
                "BWQHbVjtLF1ewt3myBMFdiMP8UOx0WVErcyuVRh8AUcRZIEwz73jmLmpRd8fVAzy" +
                "8uoijKvExt/fdu9aIfVmV4nXvL5dDpsoL/mVRDgNCZ+9mMECQQDzWLnqty25mgEs" +
                "73rJ8mhehifwblg44uO+9xpmKZhG3NFZW+beG1iPZklBVlaQ6m53e77VbVotC+LF" +
                "efsaOtU7AkEAxzd3q0REhF/FaFcq9TV3Eu3C4B/aqARKgkpJKiaCC4tnAqny7Rvd" +
                "/anxLBf8DFPYjPMkPrNqXoDA8rAC9TwDxQJBAPF6mHOMdvl5E7WNp6GCxYMXScbT" +
                "GQTKUgoMl8vNdujK84vjIMRDCqyyaftGO/zuRdSXnZWZQCT3aH9iPoWW4EUCQB1r" +
                "NYLXK/8YXYCRDsjzQkhLUDHkwld5er9O1QsicKXfyjB8hGE7ckbZZ8IJMLFpWFtI" +
                "NJwFxrl57gRotacdW7kCP2r3MkJqtHdrjUbaCJJCnHmX9BhYcBhaYS2yGFW9uyNT" +
                "5TGOrrzjz+CXBNrif3JkDbDYv2z/cCgd7kqV1kPl/g==";

        String pkcs8_base64ed="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL1eslZxZuPQuEMH" +
                "M3i7AQ4sD9QkqYKSRu+VWA3+p6Ld7+NUWnFHoPd/OyGe/lA0Hj4En+uNFZtvEInR" +
                "2PM/GwtLV0n5sPBqBlZn2E1cBFOrgrS7HDo6XmMYP053rDmfm7HJoST39529jAyG" +
                "2EjEL+4ANGnak1K63DItfdtQmcdnAgMBAAECgYA03dL7vvzybLqEO+Zqtgo9Yp+o" +
                "ofOQuDWFfTPv9D/of/0QgeQd69lMPwDLChAFZAdtWO0sXV7C3ebIEwV2Iw/xQ7HR" +
                "ZUStzK5VGHwBRxFkgTDPveOYualF3x9UDPLy6iKMq8TG399271oh9WZXide8vl0O" +
                "mygv+ZVEOA0Jn72YwQJBAPNYueq3LbmaASzvesnyaF6GJ/BuWDji4773GmYpmEbc" +
                "0Vlb5t4bWI9mSUFWVpDqbnd7vtVtWi0L4sV5+xo61TsCQQDHN3erRESEX8VoVyr1" +
                "NXcS7cLgH9qoBEqCSkkqJoILi2cCqfLtG939qfEsF/wMU9iM8yQ+s2pegMDysAL1" +
                "PAPFAkEA8XqYc4x2+XkTtY2noYLFgxdJxtMZBMpSCgyXy8126Mrzi+MgxEMKrLJp" +
                "+0Y7/O5F1JedlZlAJPdof2I+hZbgRQJAHWs1gtcr/xhdgJEOyPNCSEtQMeTCV3l6" +
                "v07VCyJwpd/KMHyEYTtyRtlnwgkwsWlYW0g0nAXGuXnuBGi1px1buQI/avcyQmq0" +
                "d2uNRtoIkkKceZf0GFhwGFphLbIYVb27I1PlMY6uvOPP4JcE2uJ/cmQNsNi/bP9w" +
                "KB3uSpXWQ+X+";

        byte[] pkcs1_bytes= Base64.decodeBase64(pkcs1_base64ed);
        byte[] pkcs8_bytes= Base64.decodeBase64(pkcs8_base64ed);

        // 在 pkcs1_bytes 前面 放26个 byte，就变成了 ： pkcs8_bytes
        // [48, -126, 2, 117, 2, 1, 0, 48, 13, 6,9, 42, -122, -9, 13, 1,1,1,504, -126, 2,95]

        byte[] diff = new byte[26];
        System.arraycopy(pkcs8_bytes, 0, diff, 0, 26);
        System.out.println(new String(diff, Charsets.UTF_8));
        System.out.println(pkcs8_base64ed.length()-pkcs1_base64ed.length());
    }


}
