package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;

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
        return encrypt(bytes, secretKey, algorithm, algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier());
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm) {
        return decrypt(bytes, secretKey, algorithm, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, secretKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] decrypt(byte[] bytes, byte[] secretKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, secretKey, algorithm, algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier());
    }

    public static enum MODE {
        ECB(CipherAlgorithmMode.ECB),
        CBC(CipherAlgorithmMode.CBC),
        CFB(CipherAlgorithmMode.CFB),
        OFB(CipherAlgorithmMode.OFB),
        CTR(CipherAlgorithmMode.CTR);

        private CipherAlgorithmMode ref;

        MODE(CipherAlgorithmMode ref) {
            this.ref = ref;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
