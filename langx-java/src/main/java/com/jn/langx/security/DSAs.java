package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DSAs {
    public static byte[] sign(byte[] privateKey, @NonNull byte[] data) {
        PrivateKey privKey = PKIs.createPrivateKey("DSA", null, new PKCS8EncodedKeySpec(privateKey));
        return sign((String) null, privKey, null, data);
    }

    public static byte[] sign(@Nullable String provider, byte[] privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        PrivateKey privKey = PKIs.createPrivateKey("DSA", provider, new PKCS8EncodedKeySpec(privateKey));
        return sign(provider, privKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        return Signatures.sign("DSA", provider, privateKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable Provider provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        return Signatures.sign("DSA", provider == null ? null : provider.getName(), privateKey, secureRandom, data);
    }

    public static boolean verify(@NonNull byte[] publicKey, byte[] data, byte[] signature) {
        PublicKey pubKey = PKIs.createPublicKey("RSA", null, new X509EncodedKeySpec(publicKey));
        return verify((String) null, pubKey, data, signature);
    }

    public static boolean verify(@Nullable String provider, @NonNull byte[] publicKey, byte[] data, byte[] signature) {
        PublicKey pubKey = PKIs.createPublicKey("RSA", null, new X509EncodedKeySpec(publicKey));
        return verify((String) null, pubKey, data, signature);
    }

    public static boolean verify(@NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        return Signatures.verify("DSA", null, publicKey, data, signature);
    }

    public static boolean verify(@NonNull String provider, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        return Signatures.verify("DSA", provider, publicKey, data, signature);
    }

    public static boolean verify(@NonNull Provider provider, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        return Signatures.verify("DSA", provider == null ? null : provider.getName(), publicKey, data, signature);
    }
}
