package com.jn.langx.security.gm;

import com.jn.langx.Named;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Symmetrics;

public interface GmService extends Named {
    String getName();

    byte[] sm2Encrypt(byte[] data, byte[] publicKey);

    byte[] sm2Encrypt(byte[] data, byte[] publicKey, SM2Mode mode);

    byte[] sm2Encrypt(byte[] data, byte[] publicKey, String algorithm, SM2Mode mode);

    byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey);

    byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, SM2Mode mode);

    byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, String algorithm, SM2Mode mode);

    byte[] sm2Sign(byte[] data, byte[] privateKey);

    byte[] sm2Sign(byte[] data, byte[] privateKey, byte[] userId);

    boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature);

    boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature, byte[] userId);

    byte[] sm3(byte[] data);

    byte[] sm3(byte[] data, int iterations);

    byte[] sm3(byte[] data, byte[] salt, int iterations);

    byte[] SM4_IV_DEFAULT = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

    byte[] sm4Encrypt(byte[] data, byte[] secretKey);


    byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey);

    byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey, byte[] iv);

    byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv);

    byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey);

    byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey);

    byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey, byte[] iv);

    byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv);
    byte[] createSM4Key(int ivBitLength);

    byte[] createSM4Key();

    byte[] createSM4IV(int ivBitLength);

    byte[] createSM4IV();

    boolean supportedSM4Transformation(Symmetrics.MODE mode, CipherAlgorithmPadding padding);
}
