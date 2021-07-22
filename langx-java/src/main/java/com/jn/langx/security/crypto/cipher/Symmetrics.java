package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Symmetrics {
    private static final Map<String, String> algorithmToTransformationMapping = new HashMap<String, String>();

    static {
        algorithmToTransformationMapping.put("AES", "AES/ECB/PKCS5Padding");
        algorithmToTransformationMapping.put("SM4", "SM4/ECB/PKCS5Padding");
    }

    protected Symmetrics() {
    }

    public static String getDefaultTransformation(String algorithm) {
        return algorithmToTransformationMapping.get(algorithm);
    }

    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, symmetricKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(symmetricKey, "Symmetric key is empty");
        Preconditions.checkArgument(!Emptys.isAllEmpty(algorithm, algorithmTransformation), "the algorithm and algorithmTransformation is empty");
        if (Emptys.isEmpty(algorithm)) {
            algorithm = Ciphers.extractAlgorithm(algorithmTransformation);
        }
        if (Emptys.isEmpty(algorithmTransformation)) {
            algorithmTransformation = getDefaultTransformation(algorithm);
            if (Emptys.isEmpty(algorithmTransformation)) {
                algorithmTransformation = Ciphers.createAlgorithmTransformation(algorithm, "ECB", "PKCS5Padding");
            }
        }
        try {
            SecretKey secretKey = new SecretKeySpec(symmetricKey, algorithm);
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.ENCRYPT_MODE, secretKey, secureRandom);
            return Ciphers.encrypt(cipher, bytes);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, symmetricKey, algorithm, null, provider, secureRandom);
    }


    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        Preconditions.checkNotEmpty(symmetricKey, "Symmetric key is empty");
        Preconditions.checkArgument(!Emptys.isAllEmpty(algorithm, algorithmTransformation), "the algorithm and algorithmTransformation is empty");

        if (Emptys.isEmpty(algorithm)) {
            algorithm = Ciphers.extractAlgorithm(algorithmTransformation);
        }
        if (Emptys.isEmpty(algorithmTransformation)) {
            algorithmTransformation = getDefaultTransformation(algorithm);
            if (Emptys.isEmpty(algorithmTransformation)) {
                algorithmTransformation = Ciphers.createAlgorithmTransformation(algorithm, "ECB", "PKCS5Padding");
            }
        }

        try {
            SecretKey secretKey = new SecretKeySpec(symmetricKey, algorithm);
            Cipher cipher = Ciphers.createCipher(algorithmTransformation, provider, Cipher.DECRYPT_MODE, secretKey, secureRandom);
            return Ciphers.decrypt(cipher, bytes);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
