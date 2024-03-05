package com.jn.langx.security.gm.crypto.bc;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.*;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.security.crypto.signature.Signatures;
import com.jn.langx.security.gm.AbstractGmService;
import com.jn.langx.security.gm.GMs;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.security.gm.SM2Mode;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2SignParameterSpec;
import com.jn.langx.security.gm.crypto.bc.asymmetric.sm2.SM2xCipherSpi;
import com.jn.langx.security.gm.crypto.bc.symmetric.sm4.SM4AlgorithmSpecSupplier;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.LinkedCaseInsensitiveMap;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class BcGmService extends AbstractGmService {
    public static final String NAME = "BC-GmService";

    private static Map<String,Class> sm2xCiphersMap = new LinkedCaseInsensitiveMap<Class>();
    private static final String PROVIDER_NAME="BC";
    static {
        Map<String, Class> sm2Map= Maps.newMap();
        sm2Map.put("SM2withSm3", SM2xCipherSpi.SM2withSm3.class);
        sm2Map.put("SM2withBlake2b", SM2xCipherSpi.SM2withBlake2b.class);
        sm2Map.put("SM2withBlake2s", SM2xCipherSpi.SM2withBlake2s.class);
        sm2Map.put("SM2withWhirlpool", SM2xCipherSpi.SM2withWhirlpool.class);
        sm2Map.put("SM2withMD5", SM2xCipherSpi.SM2withMD5.class);
        sm2Map.put("SM2withRMD", SM2xCipherSpi.SM2withRMD.class);
        sm2Map.put("SM2withSha1", SM2xCipherSpi.SM2withSha1.class);
        sm2Map.put("SM2withSha224", SM2xCipherSpi.SM2withSha224.class);
        sm2Map.put("SM2withSha256", SM2xCipherSpi.SM2withSha256.class);
        sm2Map.put("SM2withSha384", SM2xCipherSpi.SM2withSha384.class);
        sm2Map.put("SM2withSha512", SM2xCipherSpi.SM2withSha512.class);
        sm2xCiphersMap.putAll(sm2Map);
    }
    @Override
    public String getName() {
        return NAME;
    }
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey) {
        return sm2Encrypt(data,publicKey,null);
    }
    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey,  SM2Mode mode){
        return sm2Encrypt(data, publicKey, JCAEStandardName.SM2.getName(),mode);
    }

    public byte[] sm2Encrypt(byte[] data, byte[] publicKey, String algorithm, SM2Mode mode) {
        boolean globalScopeC1c3c2ModeEnabled= GMs.sm2DefaultC1C3C2ModeEnabled();
        SM2Mode defaultMode= globalScopeC1c3c2ModeEnabled? SM2Mode.C1C3C2:SM2Mode.C1C2C3;
        if(mode==null){
            mode = defaultMode;
        }
        if(mode == defaultMode){
            return Asymmetrics.encrypt(data, publicKey, algorithm, null, null, null, new BytesBasedPublicKeySupplier());
        }
        else{
            try {
                if (Strings.equalsAnyIgnoreCase(algorithm, "SM2")) {
                    algorithm = "sm2withsm3";
                }
                Class cipherClass = sm2xCiphersMap.get(algorithm);
                SM2xCipherSpi cipher = Reflects.<SM2xCipherSpi>newInstance(cipherClass, new Class[]{SM2Mode.class}, mode);
                PublicKey key = new BytesBasedPublicKeySupplier().get(publicKey, "SM2", null);
                cipher.engineInit(Cipher.ENCRYPT_MODE, key, null);
                return cipher.engineDoFinal(data, 0, data.length);
            }catch (Throwable e){
                throw Throwables.wrapAsRuntimeException(e);
            }
        }
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey) {
        return sm2Decrypt(encryptedBytes, privateKey, null);
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, SM2Mode mode) {
        return sm2Decrypt(encryptedBytes,  privateKey, JCAEStandardName.SM2.getName(), mode);
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey, String algorithm, SM2Mode mode) {
        boolean globalScopeC1c3c2ModeEnabled= GMs.sm2DefaultC1C3C2ModeEnabled();
        SM2Mode defaultMode= globalScopeC1c3c2ModeEnabled? SM2Mode.C1C3C2:SM2Mode.C1C2C3;
        if(mode==null){
            mode = defaultMode;
        }
        if(mode == defaultMode){
            return Asymmetrics.decrypt(encryptedBytes, privateKey, algorithm, null, null, null, new BytesBasedPrivateKeySupplier());
        }
        else{
            try {
                if (Strings.equalsAnyIgnoreCase(algorithm, "SM2")) {
                    algorithm = "sm2withsm3";
                }
                Class cipherClass = sm2xCiphersMap.get(algorithm);
                SM2xCipherSpi cipher = Reflects.<SM2xCipherSpi>newInstance(cipherClass, new Class[]{SM2Mode.class}, mode);
                PrivateKey key = new BytesBasedPrivateKeySupplier().get(privateKey, "SM2", null);
                cipher.engineInit(Cipher.DECRYPT_MODE, key, null);
                return cipher.engineDoFinal(encryptedBytes, 0, encryptedBytes.length);
            }catch (Throwable e){
                throw Throwables.wrapAsRuntimeException(e);
            }
        }
    }


    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey) {
        return sm2Sign(data,privateKey,null);
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey, byte[] userId) {
        return Signatures.sign(data, privateKey, "SM3WithSM2", PROVIDER_NAME, null, new SM2SignParameterSpec(userId));
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature) {
        return sm2Verify(data,publicKey,signature,null);
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature, byte[] userId) {
        boolean verified = Signatures.verify(data, signature, publicKey, "SM3WithSM2", PROVIDER_NAME, new SM2SignParameterSpec(userId));
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
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode,  byte[] secretKey, byte[] iv) {
        return sm4Encrypt(data, mode, CipherAlgorithmPadding.PKCS7Padding, secretKey,iv);
    }
    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String transformation = Ciphers.createAlgorithmTransformation(JCAEStandardName.SM4.getName(), mode.name(), padding);
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
        return sm4Decrypt(encryptedBytes, mode, CipherAlgorithmPadding.PKCS7Padding,secretKey, iv);
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, CipherAlgorithmPadding padding, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String transformation = Ciphers.createAlgorithmTransformation(JCAEStandardName.SM4.getName(), mode.name(), padding);
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        return Symmetrics.decrypt(encryptedBytes, secretKey, JCAEStandardName.SM4.getName(), transformation, null, null, new BytesBasedSecretKeySupplier(), new SM4AlgorithmSpecSupplier(iv));
    }
}
