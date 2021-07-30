package com.jn.langx.security.gm.tests.bc;

import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.security.crypto.signature.Signatures;
import com.jn.langx.security.gm.bc.crypto.asymmetric.sm2.SM2ParameterSpec;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

import java.security.KeyPair;
import java.security.SecureRandom;

public class SM2Tests {
    private String string = "abcde_12345";

    @Test
    public void test_encrypt_decrypt() throws Throwable {
        //SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        SecureRandom secureRandom = null;
        KeyPair keyPair = PKIs.createKeyPair("SM2", null, 256, secureRandom);
        byte[] encryptedBytes = Asymmetrics.encrypt(string.getBytes(Charsets.UTF_8), keyPair.getPublic().getEncoded(), "SM2", null, null, secureRandom, new BytesBasedPublicKeySupplier());
        byte[] decryptedBytes = Asymmetrics.decrypt(encryptedBytes, keyPair.getPrivate().getEncoded(), "SM2", null, null, secureRandom, new BytesBasedPrivateKeySupplier());
        System.out.println(new String(decryptedBytes, Charsets.UTF_8));
    }

    @Test
    public void test_sign_verify() {
        KeyPair keyPair = PKIs.createKeyPair("SM2", null, 256, null);
        SM2ParameterSpec parameterSpec = new SM2ParameterSpec();
        byte[] digitSignature = Signatures.sign(string.getBytes(Charsets.UTF_8), keyPair.getPrivate().getEncoded(), "SM3WithSM2", null, null, parameterSpec);
        boolean verified = Signatures.verify(string.getBytes(Charsets.UTF_8), digitSignature, keyPair.getPublic().getEncoded(), "SM3WithSM2", null, parameterSpec);
        System.out.println("verified: " + verified);
    }
}
