package com.jn.langx.security.crypto.key;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.CryptoException;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmSuite;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.key.store.KeyStores;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Key
 */
@SuppressWarnings({"unchecked"})
public class PKIs extends KeyStores {

    public static PublicKey createPublicKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty String base64PublicKey) {
        Preconditions.checkNotEmpty(base64PublicKey, "the public key is null or empty");
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey));
        return createPublicKey(algorithm, provider, pubX509);
    }

    public static PublicKey createPublicKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty byte[] base64PublicKey) {
        Preconditions.checkNotEmpty(base64PublicKey, "the public key is null or empty");
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey));
        return createPublicKey(algorithm, provider, pubX509);
    }

    public static PublicKey createPublicKey(@NotEmpty String algorithm, @Nullable String provider, @NonNull KeySpec keySpec) {
        Preconditions.checkNotNull(keySpec, "the public key is null");
        try {
            KeyFactory keyFactory = getKeyFactory(algorithm, provider);
            return keyFactory.generatePublic(keySpec);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static PrivateKey createPrivateKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty String base64Pkcs8PrivateKey) {
        return createPrivateKey(algorithm, provider, Strings.getBytesUtf8(base64Pkcs8PrivateKey));
    }

    public static PrivateKey createPrivateKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty byte[] base64Pkcs8PrivateKey) {
        return createPrivateKey(algorithm, provider, base64Pkcs8PrivateKey, true);
    }

    public static PrivateKey createPrivateKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty byte[] pkcs8PrivateKey, boolean base64ed) {
        Preconditions.checkNotEmpty(pkcs8PrivateKey, "the private key is null or empty");
        PKCS8EncodedKeySpec pkcs8PrivKey = new PKCS8EncodedKeySpec(base64ed ? Base64.decodeBase64(pkcs8PrivateKey) : pkcs8PrivateKey);
        return createPrivateKey(algorithm, provider, pkcs8PrivKey);
    }

    public static PrivateKey createPrivateKey(@NotEmpty String algorithm, @Nullable String provider, @NonNull KeySpec keySpec) {
        Preconditions.checkNotNull(keySpec, "the private key is null");
        try {
            KeyFactory keyFactory = getKeyFactory(algorithm, provider);
            return keyFactory.generatePrivate(keySpec);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyFactory getKeyFactory(@NotEmpty String algorithm, @Nullable String provider) {
        Preconditions.checkNotNull(algorithm);
        try {
            return Strings.isEmpty(provider) ? KeyFactory.getInstance(algorithm) : KeyFactory.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull String base64PrivateKey, @NonNull String base64PublicKey) {
        return createKeyPair(algorithm, provider, base64PrivateKey.getBytes(Charsets.UTF_8), base64PublicKey.getBytes(Charsets.UTF_8));
    }

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull byte[] base64PrivateKey, @NonNull byte[] base64PublicKey) {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey));
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey));
        return createKeyPair(algorithm, provider, priPKCS8, pubX509);
    }

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull KeySpec privateKeySpec, @NonNull KeySpec publicKeySpec) {
        try {
            KeyFactory keyFactory = getKeyFactory(algorithm, provider);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            return new KeyPair(publicKey, privateKey);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPairGenerator getKeyPairGenerator(@NotEmpty String algorithm, @Nullable String provider) {
        try {
            Preconditions.checkNotNull(algorithm);
            return Strings.isEmpty(provider) ? KeyPairGenerator.getInstance(algorithm) : KeyPairGenerator.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull int keySize, @Nullable SecureRandom secureRandom) {
        try {
            KeyPairGenerator keyPairGenerator = getKeyPairGenerator(algorithm, provider);
            if (keySize <= 0) {
                CipherAlgorithmSuite suite = Ciphers.getAlgorithmSuiteRegistry().get(algorithm);
                if (suite != null) {
                    keySize = suite.getKeySize();
                }
            }
            Preconditions.checkTrue(keySize > 0);
            if (secureRandom == null) {
                keyPairGenerator.initialize(keySize);
            } else {
                keyPairGenerator.initialize(keySize, secureRandom);
            }
            return keyPairGenerator.generateKeyPair();
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull AlgorithmParameterSpec parameterSpec, @Nullable SecureRandom secureRandom) {
        try {
            Preconditions.checkNotNull(parameterSpec);
            KeyPairGenerator keyPairGenerator = getKeyPairGenerator(algorithm, provider);

            if (secureRandom == null) {
                keyPairGenerator.initialize(parameterSpec);
            } else {
                keyPairGenerator.initialize(parameterSpec, secureRandom);
            }
            return keyPairGenerator.generateKeyPair();
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyGenerator getKeyGenerator(@NonNull String algorithm, @Nullable String provider) {
        Preconditions.checkNotEmpty(algorithm);
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = Strings.isBlank(provider) ? KeyGenerator.getInstance(algorithm) : KeyGenerator.getInstance(algorithm, provider);
            return keyGenerator;
        } catch (Throwable ex) {
            if (ex instanceof NoSuchAlgorithmException) {
                if (Strings.startsWith(algorithm, "hmac", true)) {
                    String keyGeneratorSpiClassName = Securitys.getLangxSecurityProvider().findAlgorithm("KeyGenerator", algorithm);
                    if (ClassLoaders.hasClass(keyGeneratorSpiClassName, PKIs.class.getClassLoader())) {
                        try {
                            Class keyGeneratorSpiClass = ClassLoaders.loadClass(keyGeneratorSpiClassName, PKIs.class.getClassLoader());
                            KeyGeneratorSpi keyGeneratorSpi = Reflects.<KeyGeneratorSpi>newInstance(keyGeneratorSpiClass);
                            if (keyGeneratorSpi != null) {
                                LangxKeyGenerator generator = new LangxKeyGenerator(keyGeneratorSpi, Securitys.getLangxSecurityProvider(), algorithm);
                                return generator;
                            }
                        } catch (Throwable ex2) {
                        }
                    }
                }
            }
            throw new CryptoException(StringTemplates.formatWithPlaceholder("{}: {}", ex.getClass(), ex.getMessage()), ex);
        }

    }

    public static SecretKey createSecretKey(String algorithm) {
        KeyGenerator keyGenerator = getKeyGenerator(algorithm, null);
        if (keyGenerator != null) {
            return keyGenerator.generateKey();
        }
        return null;
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, @Nullable String provider, @NonNull KeySpec keySpec) {
        try {
            return getSecretKeyFactory(algorithm, provider).generateSecret(keySpec);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecretKeyFactory getSecretKeyFactory(@NotEmpty String algorithm, @Nullable String provider) {
        try {
            Preconditions.checkNotNull(algorithm);
            return Strings.isEmpty(provider) ? SecretKeyFactory.getInstance(algorithm) : SecretKeyFactory.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyGenerator getSecretKeyGenerator(@NotEmpty String algorithm, @Nullable String provider) {
        return getKeyGenerator(algorithm, provider);
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, byte[] bytes) {
        return new SecretKeySpec(bytes, algorithm);
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, @Nullable String provider, @Nullable Integer keySize, @Nullable SecureRandom secureRandom) {
        if (secureRandom == null) {
            secureRandom = Securitys.getSecureRandom();
        }
        if (keySize == null || keySize <= 0) {
            CipherAlgorithmSuite suite = Ciphers.getAlgorithmSuiteRegistry().get(algorithm);
            if (suite != null) {
                keySize = suite.getKeySize();
            }
        }
        try {
            KeyGenerator secretKeyGenerator = getSecretKeyGenerator(algorithm, provider);
            if (keySize == null || keySize <= 0) {
                secretKeyGenerator.init(secureRandom);
            } else {
                secretKeyGenerator.init(keySize, secureRandom);
            }
            return secretKeyGenerator.generateKey();
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, @Nullable String provider, @Nullable AlgorithmParameterSpec parameterSpec, @Nullable SecureRandom secureRandom) {
        Preconditions.checkTrue(parameterSpec != null || secureRandom != null);
        try {
            KeyGenerator secretKeyGenerator = getSecretKeyGenerator(algorithm, provider);
            if (parameterSpec == null) {
                secretKeyGenerator.init(secureRandom);
            } else if (secureRandom == null) {
                secretKeyGenerator.init(parameterSpec);
            } else {
                secretKeyGenerator.init(parameterSpec, secureRandom);
            }
            return secretKeyGenerator.generateKey();
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

}
