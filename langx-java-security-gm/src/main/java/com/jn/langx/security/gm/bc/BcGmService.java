package com.jn.langx.security.gm.bc;

import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.bc.crypto.symmetric.sm4.SM4AlgorithmSpecSupplier;

public class BcGmService implements GmService {
    public static final String NAME = "BC-GmService";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] sm3(byte[] data) {
        return sm3(data, 1);
    }

    @Override
    public byte[] sm3(byte[] data, int iterations) {
        return sm3(data, null, iterations);
    }

    @Override
    public byte[] sm3(byte[] data, byte[] salt, int iterations) {
        return MessageDigests.digest("SM3", data, salt, iterations);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, byte[] secretKey) {
        return sm4Encrypt(data, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, byte[] secretKey, byte[] iv) {
        return Symmetrics.encrypt(data, secretKey, "SM4", "SM4/CBC/PKCS5Padding", null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey, byte[] iv) {
        return Symmetrics.decrypt(encryptedBytes, secretKey, "SM4", "SM4/CBC/PKCS5Padding", null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }
}
