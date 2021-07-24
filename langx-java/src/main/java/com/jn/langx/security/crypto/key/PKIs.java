package com.jn.langx.security.crypto.key;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Key
 */
public class PKIs extends Securitys {
    private static final Logger logger = LoggerFactory.getLogger(PKIs.class);

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

    public static PrivateKey createPrivateKey(@NotEmpty String algorithm, @Nullable String provider, @NotEmpty byte[] pkcs8PrivateKey, boolean base64ed){
        Preconditions.checkNotEmpty(pkcs8PrivateKey, "the private key is null or empty");
        PKCS8EncodedKeySpec pkcs8PrivKey = new PKCS8EncodedKeySpec(base64ed ? Base64.decodeBase64(pkcs8PrivateKey):pkcs8PrivateKey);
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

    public static KeyPair createKeyPair(@NotEmpty String algorithm, @Nullable String provider, @NonNull int keyLength, @Nullable SecureRandom secureRandom) {
        try {
            Preconditions.checkTrue(keyLength > 0);
            KeyPairGenerator keyPairGenerator = getKeyPairGenerator(algorithm, provider);
            if("SM2".equals(algorithm)){
                keyLength = 256;
            }
            if (secureRandom == null) {
                keyPairGenerator.initialize(keyLength);
            } else {
                keyPairGenerator.initialize(keyLength, secureRandom);
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
        try {
            Preconditions.checkNotNull(algorithm);
            return Strings.isEmpty(provider) ? KeyGenerator.getInstance(algorithm) : KeyGenerator.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, byte[] bytes) {
        return new SecretKeySpec(bytes, algorithm);
    }

    public static SecretKey createSecretKey(@NotEmpty String algorithm, @Nullable String provider, @Nullable Integer keyLength, @Nullable SecureRandom secureRandom) {
        Preconditions.checkTrue(keyLength != null || secureRandom != null);
        try {

            KeyGenerator secretKeyGenerator = getSecretKeyGenerator(algorithm, provider);
            if (keyLength == null) {
                secretKeyGenerator.init(secureRandom);
            } else if (secureRandom == null) {
                secretKeyGenerator.init(keyLength);
            } else {
                secretKeyGenerator.init(keyLength, secureRandom);
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

    public static KeyStore getEmptyKeyStore(@NonNull String type, @Nullable String provider) {
        try {
            return Strings.isEmpty(provider) ? KeyStore.getInstance(type) : KeyStore.getInstance(type, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyStore getKeyStore(@NonNull String type, @Nullable String provider, InputStream inputStream, char[] password) {
        try {
            KeyStore keyStore = getEmptyKeyStore(type, provider);
            keyStore.load(inputStream, password);
            return keyStore;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyStore getKeyStore(@NonNull String type, @Nullable String provider, File file, char[] password) {
        try {
            FileInputStream inputStream = null;
            KeyStore keyStore = null;
            try {
                inputStream = new FileInputStream(file);
                keyStore = getKeyStore(type, provider, inputStream, password);
            } finally {
                IOs.close(inputStream);
            }
            return keyStore;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }


    public static void persist(KeyStore keyStore, File file, @NonNull String password) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            persist(keyStore, outputStream, password);
        } finally {
            IOs.close(outputStream);
        }
    }

    public static void persist(@NonNull KeyStore keyStore, @NonNull OutputStream outputStream, @NonNull String password) {
        try {
            persist(keyStore, outputStream, password.toCharArray());
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static void persist(@NonNull KeyStore keyStore, @NonNull OutputStream outputStream, @NonNull char[] password) {
        try {
            keyStore.store(outputStream, password);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPair findKeyPair(@NonNull KeyStore keyStore, String alias, String password) {
        try {
            return findKeyPair(keyStore, alias, password.toCharArray());
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static KeyPair findKeyPair(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull char[] password) {
        try {
            if (!keyStore.containsAlias(alias) && keyStore.isKeyEntry(alias)) {
                return null;
            }
            Key key = keyStore.getKey(alias, password);
            if (key instanceof PrivateKey) {
                PrivateKey privateKey = (PrivateKey) key;
                Certificate certificate = keyStore.getCertificate(alias);
                PublicKey publicKey = certificate.getPublicKey();
                return new KeyPair(publicKey, privateKey);
            }
        } catch (Throwable ex) {
            logger.warn("can't find a valid key pair, the alias is {}", alias);
        }
        return null;
    }

    public static SecretKey findSecretKey(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull String password) {
        return findSecretKey(keyStore, alias, password.toCharArray());
    }

    public static SecretKey findSecretKey(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull char[] password) {
        try {
            if (!keyStore.containsAlias(alias) && keyStore.isKeyEntry(alias)) {
                return null;
            }
            Key key = keyStore.getKey(alias, password);
            if (key instanceof SecretKey) {
                return (SecretKey) key;
            }
        } catch (Throwable ex) {
            logger.warn("can't find a valid key pair, the alias is {}", alias);
        }
        return null;
    }

    public static Certificate findCertificate(@NonNull KeyStore keyStore, @NonNull String alias) {
        try {
            if (!keyStore.containsAlias(alias)) {
                return null;
            }
            return keyStore.getCertificate(alias);
        } catch (Throwable ex) {
            logger.warn("can't find a valid certificate, the alias is {}", alias);
        }
        return null;
    }

    public static List<Certificate> findCertificateChain(@NonNull KeyStore keyStore, @NonNull String alias) {
        try {
            if (!keyStore.containsAlias(alias)) {
                return null;
            }
            Certificate[] certificates = keyStore.getCertificateChain(alias);
            return Collects.newArrayList(certificates);
        } catch (Throwable ex) {
            logger.warn("can't find a valid certificate, the alias is {}", alias);
        }
        return null;
    }

    public static PublicKey findPublicKey(@NonNull KeyStore keyStore, @NonNull String alias) {
        Certificate certificate = findCertificate(keyStore, alias);
        PublicKey publicKey = null;
        if (certificate != null) {
            publicKey = certificate.getPublicKey();
        }
        return publicKey;
    }

    public static void setSecretKey(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull SecretKey secretKey, @NonNull char[] password) {
        try {
            keyStore.setKeyEntry(alias, secretKey, password, null);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static void setPrivateKey(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull PrivateKey privateKey, @NonNull char[] password, @NonNull List<Certificate> certificateChain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, Collects.toArray(certificateChain, Certificate[].class));
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static void setCertificate(@NonNull KeyStore keyStore, @NonNull String alias, @NonNull Certificate certificate) {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
