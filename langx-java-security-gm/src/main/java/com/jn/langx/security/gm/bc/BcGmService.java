package com.jn.langx.security.gm.bc;

import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.security.crypto.signature.Signatures;
import com.jn.langx.security.gm.AbstractGmService;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.bc.crypto.asymmetric.sm2.SM2ParameterSpec;
import com.jn.langx.security.gm.bc.crypto.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;

public class BcGmService extends AbstractGmService {
    public static final String NAME = "BC-GmService";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey) {
        return Asymmetrics.encrypt(data, publicKey, "SM2", null, null, null, new BytesBasedPublicKeySupplier());
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey) {
        return Asymmetrics.decrypt(encryptedBytes, privateKey, "SM2", null, null, null, new BytesBasedPrivateKeySupplier());
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey) {
        SM2ParameterSpec parameterSpec = new SM2ParameterSpec();
        return Signatures.sign(data, privateKey, "SM3WithSM2", null, null, parameterSpec);
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature) {
        SM2ParameterSpec parameterSpec = new SM2ParameterSpec();
        boolean verified = Signatures.verify(data, signature, publicKey, "SM3WithSM2", null, parameterSpec);
        return verified;
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
        return sm4Encrypt(data, (Symmetrics.MODE) null, secretKey);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Encrypt(data, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String transformation = Ciphers.createAlgorithmTransformation("SM4", mode.name(), "PKCS7Padding");
        if (transformation.contains("CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return Symmetrics.encrypt(data, secretKey, "SM4", transformation, null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }


    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, (Symmetrics.MODE) null, secretKey);
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, mode, secretKey, GmService.SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String transformation = Ciphers.createAlgorithmTransformation("SM4", mode.name(), "PKCS7Padding");
        if (transformation.contains("CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return Symmetrics.decrypt(encryptedBytes, secretKey, "SM4", transformation, null, null, new ByteBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }
}
