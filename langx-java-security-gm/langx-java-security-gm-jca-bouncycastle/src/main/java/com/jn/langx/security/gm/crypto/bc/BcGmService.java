package com.jn.langx.security.gm.crypto.bc;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.security.crypto.signature.Signatures;
import com.jn.langx.security.gm.AbstractGmService;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2ParameterSpec;
import com.jn.langx.security.gm.crypto.bc.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.util.Emptys;

public class BcGmService extends AbstractGmService {
    public static final String NAME = "BC-GmService";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey) {
        return Asymmetrics.encrypt(data, publicKey, JCAEStandardName.SM2.getName(), null, null, null, new BytesBasedPublicKeySupplier());
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey) {
        return Asymmetrics.decrypt(encryptedBytes, privateKey, JCAEStandardName.SM2.getName(), null, null, null, new BytesBasedPrivateKeySupplier());
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey) {
        SM2ParameterSpec parameterSpec = new SM2ParameterSpec();
        return Signatures.sign(data, privateKey, JCAEStandardName.SM2.getName(), null, null, parameterSpec);
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
        return sm4Encrypt(data, null, secretKey);
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
        String transformation = Ciphers.createAlgorithmTransformation(JCAEStandardName.SM4.getName(), mode.name(), "PKCS7Padding");
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        return Symmetrics.encrypt(data, secretKey, JCAEStandardName.SM4.getName(), transformation, null, null, new BytesBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
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
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String transformation = Ciphers.createAlgorithmTransformation(JCAEStandardName.SM4.getName(), mode.name(), "PKCS7Padding");
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        return Symmetrics.decrypt(encryptedBytes, secretKey, JCAEStandardName.SM4.getName(), transformation, null, null, new BytesBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }
}
