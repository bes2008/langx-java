package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.SecureRandom;

public class AESs {
    private static final String algorithmTransformation = "AES/ECB/PKCS5Padding";


    public static SecretKey buildAesKey(byte[] aesKey) {
        return buildAesKeySpec(aesKey);
    }

    public static SecretKeySpec buildAesKeySpec(byte[] aesKey) {
        return new SecretKeySpec(aesKey, "AES");
    }

    public static byte[] newAesKey() {
        try {
            SecretKey secretKey = PKIs.createSecretKey("AES", null, 128, null);
            return secretKey.getEncoded();
        } catch (Throwable ex) {
            return null;
        }
    }

    public static byte[] encrypt(byte[] bytes, byte[] aesKey) {
        return encrypt(bytes, aesKey, algorithmTransformation, null, null);
    }

    public static byte[] encrypt(byte[] bytes, byte[] aesKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(aesKey, "AES key is empty");
        try {
            SecretKey secretKey = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.ENCRYPT_MODE, secretKey, secureRandom);
            return Ciphers.encrypt(cipher, bytes);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static byte[] decrypt(byte[] bytes, byte[] aesKey) {
        return decrypt(bytes, aesKey, algorithmTransformation, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] aesKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(aesKey, "AES key is empty");
        try {
            Preconditions.checkNotEmpty(aesKey, "AES key is empty");
            SecretKey secretKey = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.DECRYPT_MODE, secretKey, secureRandom);
            return Ciphers.decrypt(cipher, bytes);
        } catch (Throwable ex) {
             throw new SecurityException(ex.getMessage(),ex);
        }
    }

}
