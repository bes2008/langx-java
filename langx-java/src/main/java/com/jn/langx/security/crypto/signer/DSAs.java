package com.jn.langx.security.crypto.signer;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Strings;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DSAs {
    public static final String DSA_INSTANCE_ALGORITHM = "SHA1withDSA";

    public static byte[] sign(byte[] privateKey, @NonNull byte[] data) {
        PrivateKey privKey = PKIs.createPrivateKey("DSA", null, new PKCS8EncodedKeySpec(privateKey));
        return sign(null, (String) null, privKey, null, data);
    }

    public static byte[] sign(@Nullable String signatureInstanceAlgorithm, byte[] privateKey, @NonNull byte[] data) {
        PrivateKey privKey = PKIs.createPrivateKey("DSA", null, new PKCS8EncodedKeySpec(privateKey));
        return sign(signatureInstanceAlgorithm, (String) null, privKey, null, data);
    }

    public static byte[] sign(@Nullable String signatureInstanceAlgorithm, @Nullable String provider, byte[] privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        PrivateKey privKey = PKIs.createPrivateKey("DSA", provider, new PKCS8EncodedKeySpec(privateKey));
        return sign(signatureInstanceAlgorithm, provider, privKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable String signatureInstanceAlgorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        signatureInstanceAlgorithm = Strings.useValueIfBlank(signatureInstanceAlgorithm, DSA_INSTANCE_ALGORITHM);
        return Signatures.sign(signatureInstanceAlgorithm, provider, privateKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable String signatureInstanceAlgorithm, @Nullable Provider provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        signatureInstanceAlgorithm = Strings.useValueIfBlank(signatureInstanceAlgorithm, DSA_INSTANCE_ALGORITHM);
        return Signatures.sign(signatureInstanceAlgorithm, provider == null ? null : provider.getName(), privateKey, secureRandom, data);
    }

    public static boolean verify(@NonNull byte[] publicKey, byte[] data, byte[] signature) {
        return verify(null, (String) null, publicKey, data, signature);
    }

    public static boolean verify(@Nullable String signatureInstanceAlgorithm, @NonNull byte[] publicKey, byte[] data, byte[] signature) {
        PublicKey pubKey = PKIs.createPublicKey("DSA", null, new X509EncodedKeySpec(publicKey));
        return verify(signatureInstanceAlgorithm, (String) null, pubKey, data, signature);
    }

    public static boolean verify(@Nullable String signatureInstanceAlgorithm, @Nullable String provider, @NonNull byte[] publicKey, byte[] data, byte[] signature) {
        PublicKey pubKey = PKIs.createPublicKey("DSA", null, new X509EncodedKeySpec(publicKey));
        return verify(signatureInstanceAlgorithm, (String) null, pubKey, data, signature);
    }

    public static boolean verify(@Nullable String signatureInstanceAlgorithm, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        return verify(signatureInstanceAlgorithm, (String) null, publicKey, data, signature);
    }

    public static boolean verify(@Nullable String signatureInstanceAlgorithm, @NonNull String provider, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        signatureInstanceAlgorithm = Strings.useValueIfBlank(signatureInstanceAlgorithm, DSA_INSTANCE_ALGORITHM);
        return Signatures.verify(signatureInstanceAlgorithm, provider, publicKey, data, signature);
    }

    public static boolean verify(@Nullable String signatureInstanceAlgorithm, @NonNull Provider provider, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        signatureInstanceAlgorithm = Strings.useValueIfBlank(signatureInstanceAlgorithm, DSA_INSTANCE_ALGORITHM);
        return Signatures.verify(signatureInstanceAlgorithm, provider == null ? null : provider.getName(), publicKey, data, signature);
    }
}
