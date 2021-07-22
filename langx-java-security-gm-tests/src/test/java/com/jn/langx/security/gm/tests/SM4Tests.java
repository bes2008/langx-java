package com.jn.langx.security.gm.tests;

import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class SM4Tests {
    @Test
    public void test() throws NoSuchAlgorithmException {
        Securitys.addProvider(new GmJceProvider());

        String string = "abcde_12345";
        SecretKey sm4key = PKIs.createSecretKey("SM4", (String)null, 128, null);

        byte[] encryptedBytes = Symmetrics.encrypt(string.getBytes(Charsets.UTF_8),
                sm4key.getEncoded(),
                "SM4",
                null,
                null,
                SecureRandom.getInstance("SHA1PRNG")
        );

        byte[] decryptedBytes = Symmetrics.decrypt(encryptedBytes,
                sm4key.getEncoded(),
                "SM4",
                null,
                null,
                SecureRandom.getInstance("SHA1PRNG")
                );
        System.out.println(new String(decryptedBytes));
    }
}
