package com.jn.langx.security.gm;

import com.jn.langx.Named;

public interface GmService extends Named {
    String getName();

    byte[] sm3(byte[] data);

    byte[] sm3(byte[] data, int iterations);

    byte[] sm3(byte[] data, byte[] salt, int iterations);

    public static final byte[] SM4_IV_DEFAULT = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

    byte[] sm4Encrypt(byte[] data, byte[] secretKey);

    byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey);

    byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey, byte[] iv);

    byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey);

    byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey);

    byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey, byte[] iv);
}
