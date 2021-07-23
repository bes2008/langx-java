package com.jn.langx.security.gm.tests;

import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.security.gm.bc.GmBCInitializer;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.security.KeyPair;
import java.security.SecureRandom;

public class SM2Tests {
    private String string = "abcde_12345";

    @Test
    public void test() throws Throwable {
        new GmBCInitializer();
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        KeyPair keyPair = PKIs.createKeyPair("SM2", null, 256, secureRandom);
        byte[] encryptedBytes = Asymmetrics.encrypt(string.getBytes(Charsets.UTF_8), keyPair.getPublic().getEncoded(), "SM2", null, null, secureRandom, new BytesBasedPublicKeySupplier());
        byte[] decryptedBytes = Asymmetrics.decrypt(encryptedBytes, keyPair.getPrivate().getEncoded(), "SM2", null, null, secureRandom, new BytesBasedPrivateKeySupplier());
        System.out.println(new String(decryptedBytes, Charsets.UTF_8));
    }
}
