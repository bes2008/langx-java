package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedSecretKeySupplier;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Provider;
import java.security.SecureRandom;

public class Symmetrics extends Ciphers {
    protected Symmetrics() {
    }

    public static byte[] encrypt(byte[] bytes, byte[] secretKey, String algorithm) {
        return encrypt(bytes, secretKey, algorithm, null, null);
    }

    public static byte[] encrypt(byte[] bytes, byte[] secretKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, secretKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] encrypt(byte[] bytes, byte[] secretKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, secretKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedSecretKeySupplier());
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm) {
        return decrypt(bytes, secretKey, algorithm, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, secretKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, secretKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedSecretKeySupplier());
    }

    public enum MODE {
        ECB(CipherAlgorithmMode.ECB, false),
        CBC(CipherAlgorithmMode.CBC, true),
        CFB(CipherAlgorithmMode.CFB, true),
        OFB(CipherAlgorithmMode.OFB, true),
        CTR(CipherAlgorithmMode.CTR, true);

        private CipherAlgorithmMode ref;
        private boolean hasIV;

        MODE(CipherAlgorithmMode ref) {
            this(ref, true);
        }

        MODE(CipherAlgorithmMode ref, boolean hasIV) {
            this.ref = ref;
            this.hasIV = hasIV;
        }

        public boolean hasIV() {
            return hasIV;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
