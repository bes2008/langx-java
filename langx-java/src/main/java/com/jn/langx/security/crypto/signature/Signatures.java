package com.jn.langx.security.crypto.signature;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.cipher.AlgorithmParameterSupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
 */
public class Signatures extends Securitys {
    /**
     * @param algorithm 算法名称，要根据标准规范来，不能只是 DSA或者 RSA
     * @param provider  算法提供商
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
        return createSignature(algorithm, provider, privateKey, secureRandom, (AlgorithmParameterSpec) null);
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, final AlgorithmParameterSpec cipherAlgoParameterSpec) {
        return createSignature(algorithm, provider, privateKey, secureRandom, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return cipherAlgoParameterSpec;
            }
        });
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, AlgorithmParameterSupplier cipherAlgoParameterSupplier) {
        AlgorithmParameterSpec parameterSpec = null;
        if (cipherAlgoParameterSupplier != null) {
            Object parameters = cipherAlgoParameterSupplier.get(privateKey, algorithm, null, Securitys.getProvider(provider), secureRandom);
            if (parameters instanceof AlgorithmParameterSpec) {
                parameterSpec = (AlgorithmParameterSpec) parameters;
            }
        }
        try {
            Signature signature = createSignature(algorithm, provider);
            if (parameterSpec != null) {
                signature.setParameter(parameterSpec);
            }
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

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PublicKey publicKey, final AlgorithmParameterSpec algorithmParameterSpec) {
        return createSignature(algorithm, provider, publicKey, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return algorithmParameterSpec;
            }
        });
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PublicKey publicKey, AlgorithmParameterSupplier cipherAlgoParameterSupplier) {
        AlgorithmParameterSpec parameterSpec = null;
        if (cipherAlgoParameterSupplier != null) {
            Object parameters = cipherAlgoParameterSupplier.get(publicKey, algorithm, null, Securitys.getProvider(provider), null);
            if (parameters instanceof AlgorithmParameterSpec) {
                parameterSpec = (AlgorithmParameterSpec) parameters;
            }
        }
        try {
            Signature signature = createSignature(algorithm, provider);
            if (parameterSpec != null) {
                signature.setParameter(parameterSpec);
            }
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
        return verify(data, signature, publicKey, algorithm, provider, null);
    }

    public static boolean verify(byte[] data, byte[] signature, @NonNull PublicKey publicKey, @NonNull String algorithm, @Nullable String provider, AlgorithmParameterSupplier parameterSupplier) {
        try {
            return verify(createSignature(algorithm, provider, publicKey, parameterSupplier), data, signature);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static boolean verify(byte[] data, byte[] signature, @NonNull byte[] publicKeyBytes, @NonNull String algorithm, @Nullable String provider) {
        PublicKey publicKey = new BytesBasedPublicKeySupplier().get(publicKeyBytes, algorithm, Securitys.getProvider(provider));
        return verify(data, signature, publicKey, algorithm, provider, null);
    }

    public static boolean verify(byte[] data, byte[] signature, @NonNull byte[] publicKeyBytes, @NonNull String algorithm, @Nullable String provider, final AlgorithmParameterSpec parameterSpec) {
        PublicKey publicKey = new BytesBasedPublicKeySupplier().get(publicKeyBytes, algorithm, Securitys.getProvider(provider));
        return verify(data, signature, publicKey, algorithm, provider, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return parameterSpec;
            }
        });
    }

    public static boolean verify(byte[] data, byte[] signature, @NonNull byte[] publicKeyBytes, @NonNull String algorithm, @Nullable String provider, AlgorithmParameterSupplier parameterSupplier) {
        PublicKey publicKey = new BytesBasedPublicKeySupplier().get(publicKeyBytes, algorithm, Securitys.getProvider(provider));
        return verify(data, signature, publicKey, algorithm, provider, parameterSupplier);
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

    /**
     * @deprecated
     */
    @Deprecated
    public static byte[] sign(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        return sign(data, privateKey, algorithm, provider, secureRandom, null);
    }

    public static byte[] sign(@NonNull byte[] data, @NonNull byte[] privateKeyBytes, @NonNull String algorithm, @Nullable String provider, @Nullable SecureRandom secureRandom, @Nullable AlgorithmParameterSupplier cipherAlgoParameterSupplier) {
        PrivateKey privateKey = new BytesBasedPrivateKeySupplier().get(privateKeyBytes, algorithm, Securitys.getProvider(provider));
        return sign(data, privateKey, algorithm, provider, secureRandom, cipherAlgoParameterSupplier);
    }

    public static byte[] sign(@NonNull byte[] data, @NonNull byte[] privateKeyBytes, @NonNull String algorithm, @Nullable String provider, @Nullable SecureRandom secureRandom, final @Nullable AlgorithmParameterSpec algorithmParameterSpec) {
        PrivateKey privateKey = new BytesBasedPrivateKeySupplier().get(privateKeyBytes, algorithm, Securitys.getProvider(provider));
        return sign(data, privateKey, algorithm, provider, secureRandom, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return algorithmParameterSpec;
            }
        });
    }

    public static byte[] sign(@NonNull byte[] data, @NonNull byte[] privateKeyBytes, @NonNull String algorithm, @Nullable String provider, @Nullable SecureRandom secureRandom) {
        return sign(data, privateKeyBytes, algorithm, provider, secureRandom, (AlgorithmParameterSpec) null);
    }

    public static byte[] sign(@NonNull byte[] data, @NonNull PrivateKey privateKey, @NonNull String algorithm, @Nullable String provider, @Nullable SecureRandom secureRandom, @Nullable AlgorithmParameterSupplier cipherAlgoParameterSupplier) {
        try {
            Signature signature = createSignature(algorithm, provider, privateKey, secureRandom, cipherAlgoParameterSupplier);
            return sign(signature, data);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
