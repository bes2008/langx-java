package com.jn.langx.security.gm;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;

public abstract class AbstractGmService implements GmService {

    public byte[] sm2Encrypt(byte[] data, byte[] publicKey) {
        return sm2Encrypt(data, publicKey, null);
    }

    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey, SM2Mode mode) {
        return sm2Encrypt(data, publicKey, JCAEStandardName.SM2.getName(), mode);
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey) {
        return sm2Decrypt(encryptedBytes, privateKey, null);
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, SM2Mode mode) {
        return sm2Decrypt(encryptedBytes, privateKey, JCAEStandardName.SM2.getName(), mode);
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey) {
        return sm2Sign(data, privateKey, null);
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature) {
        return sm2Verify(data, publicKey, signature, null);
    }


    public byte[] sm3(byte[] data) {
        return sm3(data, 1);
    }

    @Override
    public byte[] sm3(byte[] data, int iterations) {
        return sm3(data, null, iterations);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, byte[] secretKey) {
        return sm4Encrypt(data, null, secretKey);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Encrypt(data, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        return sm4Encrypt(data, mode, null, secretKey, iv);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, null, secretKey);
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        return sm4Decrypt(encryptedBytes, mode, null, secretKey, iv);
    }

    @Override
    public byte[] createSM4Key(int bitLength) {
        return PKIs.createSecretKey("SM4","BC",bitLength,null).getEncoded();
    }

    @Override
    public byte[] createSM4Key() {
        return PKIs.createSecretKeyBytes(128);
    }

    @Override
    public byte[] createSM4IV(int ivBitLength) {
        return Ciphers.createIvParameterSpec(128).getIV();
    }

    @Override
    public byte[] createSM4IV() {
        return createSM4IV(128);
    }
}
