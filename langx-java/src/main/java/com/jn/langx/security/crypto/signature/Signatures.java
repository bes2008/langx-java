package com.jn.langx.security.crypto.signature;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.security.*;
import java.security.cert.Certificate;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
 */
public class Signatures {
    /**
     *
     * @param algorithm 算法名称，要根据标准规范来，不能只是 DSA或者 RSA
     * @param provider 算法提供商
     * @return Signature对象
     */
    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider) {
        try {
            return Strings.isEmpty(provider) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom) {
        try {
            Signature signature = createSignature(algorithm, provider);
            if (secureRandom == null) {
                signature.initSign(privateKey);
            } else {
                signature.initSign(privateKey, secureRandom);
            }
            return signature;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PublicKey publicKey) {
        try {
            Signature signature = createSignature(algorithm, provider);
            signature.initVerify(publicKey);
            return signature;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }


    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull Certificate certificate) {
        try {
            Signature signature = createSignature(algorithm, provider);
            signature.initVerify(certificate);
            return signature;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static boolean verify(Signature initedSignature, byte[] data, byte[] signature) {
        try {
            Preconditions.checkNotNull(initedSignature);
            initedSignature.update(data);
            return initedSignature.verify(signature);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static boolean verify(@NonNull String algorithm, @Nullable String provider, @NonNull PublicKey publicKey, byte[] data, byte[] signature) {
        try {
            return verify(createSignature(algorithm, provider, publicKey), data, signature);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static byte[] sign(Signature initedSignaturer, byte[] data) {
        try {
            Preconditions.checkNotNull(initedSignaturer);
            initedSignaturer.update(data);
            return initedSignaturer.sign();
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static byte[] sign(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        try {
            return sign(createSignature(algorithm, provider, privateKey, secureRandom), data);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
