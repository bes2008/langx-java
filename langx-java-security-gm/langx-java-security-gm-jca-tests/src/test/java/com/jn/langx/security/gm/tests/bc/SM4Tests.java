package com.jn.langx.security.gm.tests.bc;

import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;
import com.jn.langx.security.gm.crypto.bc.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.Provider;
import java.security.SecureRandom;


public class SM4Tests {
    @Test
    public void test() throws Throwable {
        //Securitys.addProvider(new GmJceProvider());

        String string = "abcde_12345";

        SecretKey sm4key = PKIs.createSecretKey("SM4", (String) null, 128, null);

        byte[] encryptedBytes = Symmetrics.encrypt(string.getBytes(Charsets.UTF_8),
                sm4key.getEncoded(),
                "SM4",
                "SM4/ECB/PKCS5Padding",
                (Provider) null,
                (SecureRandom) null,
                new BytesBasedSecretKeySupplier(),
                new SM4AlgorithmSpecSupplier()
        );

        byte[] decryptedBytes = Symmetrics.decrypt(encryptedBytes,
                sm4key.getEncoded(),
                "SM4",
                "SM4/ECB/PKCS5Padding",
                null, null,
                new BytesBasedSecretKeySupplier(),
                new SM4AlgorithmSpecSupplier()
        );
        System.out.println(new String(decryptedBytes));
    }

    @Test
    public void testSM4() {
        testSymmetric("SM4");
    }

    @Test
    public void testAES() {
        testSymmetric("AES");
    }
    //@Test
    public void testSALSA20() {
        testSymmetric("SALSA20");
    }

    @Test
    public void testDES() {
        testSymmetric("DES");
    }
    @Test
    public void testIDEA() {
        testSymmetric("IDEA");
    }
    @Test
    public void testSEED() {
        testSymmetric("SEED");
    }

    @Test
    public void testRC2() {
        testSymmetric("RC2");
    }

    @Test
    public void testRC5() {
        testSymmetric("RC5");
    }

    @Test
    public void testRC6() {
        testSymmetric("RC6");
    }

    private void testSymmetric(String algorithm) {
        String string = "abcde_12345";
        SecretKey sm4key = PKIs.createSecretKey(algorithm, (String) null, (Integer) null, null);
        byte[] encryptedBytes = Symmetrics.encrypt(string.getBytes(Charsets.UTF_8),
                sm4key.getEncoded(),
                algorithm
        );
        byte[] decryptedBytes = Symmetrics.decrypt(encryptedBytes,
                sm4key.getEncoded(),
                algorithm
        );
        System.out.println(new String(decryptedBytes));
    }
}
