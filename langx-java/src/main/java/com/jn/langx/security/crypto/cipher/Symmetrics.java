package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;

import java.security.Provider;
import java.security.SecureRandom;

public class Symmetrics extends Ciphers {
    protected Symmetrics() {
    }

    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, symmetricKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, symmetricKey, algorithm, algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier());
    }

    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, String algorithm, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, symmetricKey, algorithm, null, provider, secureRandom);
    }

    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, symmetricKey, algorithm, algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier());
    }

    public static enum MODE {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR
    }
}
