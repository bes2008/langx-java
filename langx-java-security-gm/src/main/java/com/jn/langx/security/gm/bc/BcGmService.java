package com.jn.langx.security.gm.bc;

import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.bc.crypto.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;

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
        return sm4Encrypt(data, null, secretKey);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey) {
        return sm4Encrypt(data, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey, byte[] iv) {
        String transformation = Ciphers.createAlgorithmTransformation("SM4", Strings.isBlank(mode) ? "CBC" : mode.toUpperCase(), "PKCS7Padding");
        if (transformation.contains("CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return Symmetrics.encrypt(data, secretKey, "SM4", transformation, null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }


    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, null, secretKey);
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey, byte[] iv) {
        String transformation = Ciphers.createAlgorithmTransformation("SM4", Strings.isBlank(mode) ? "CBC" : mode.toUpperCase(), "PKCS7Padding");
        if (transformation.contains("CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return Symmetrics.decrypt(encryptedBytes, secretKey, "SM4", transformation, null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }
}
