package com.jn.langx.security.gm.crypto.tongsuo;

import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.gm.AbstractGmService;
import com.jn.langx.security.gm.SM2Mode;

public class TSGmService extends AbstractGmService {
    public static final String NAME = "TS-GmService";
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey, String algorithm, SM2Mode mode) {
        return new byte[0];
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, String algorithm, SM2Mode mode) {
        return new byte[0];
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey, byte[] userId) {
        return new byte[0];
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature, byte[] userId) {
        return false;
    }

    @Override
    public byte[] sm3(byte[] data, byte[] salt, int iterations) {
        return new byte[0];
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv) {
        return new byte[0];
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv) {
        return new byte[0];
    }

    @Override
    public boolean supportedSM4Transformation(Symmetrics.MODE mode, CipherAlgorithmPadding padding) {
        return false;
    }
}
