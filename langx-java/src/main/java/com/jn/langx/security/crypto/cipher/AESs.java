package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Emptys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESs extends Symmetrics {

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
            return Emptys.EMPTY_BYTES;
        }
    }

    public static byte[] encrypt(byte[] bytes, byte[] aesKey) {
        return encrypt(bytes, aesKey, "AES", null, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] aesKey) {
        return decrypt(bytes, aesKey, "AES", null, null, null);
    }


}
