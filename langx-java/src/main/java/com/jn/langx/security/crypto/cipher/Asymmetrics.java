package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;

import java.security.Provider;
import java.security.SecureRandom;

public class Asymmetrics extends Ciphers {
    protected Asymmetrics() {
    }

    public static byte[] encrypt(byte[] bytes, byte[] pubKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, pubKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPublicKeySupplier());
    }


    public static byte[] encryptUsePriKey(byte[] bytes, byte[] priKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, priKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPrivateKeySupplier());
    }


    public static byte[] decrypt(byte[] bytes, byte[] priKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, priKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPrivateKeySupplier());
    }

    public static byte[] decryptUsePubKey(byte[] bytes, byte[] pubKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, pubKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPublicKeySupplier());
    }

}
