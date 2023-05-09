package com.jn.langx.security.crypto.key.store;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

public class KeyStores extends Securitys {

    /****************************************************
     * KEY_STORE TYPE 不区分大小写
     ****************************************************/

    /**
     * JKS的Provider是SUN，在每个版本的JDK中都有。
     */
    public static final KeyStoreType KEYSTORE_TYPE_JKS = new KeyStoreType("JKS", "SUN");
    /**
     * JCEKS的Provider是SUNJCE， jdk 1.4后我们都能够直接使用它。
     * JCEKS在安全级别上要比JKS强，使用的Provider是JCEKS(推荐)，尤其在保护KeyStore中的私钥上（使用TripleDes）。
     */
    public static final KeyStoreType KEYSTORE_TYPE_JCEKS = new KeyStoreType("JCEKS", "SUNJCE");
    /**
     * PKCS#12是公钥加密标准，它规定了可包含所有私钥、公钥和证书。
     * 其以二进制格式存储，也称为 PFX 文件，
     * 在windows中可以直接导入到密钥区，注意，PKCS#12的密钥库保护密码同时也用于保护Key。
     */
    public static final KeyStoreType KEYSTORE_TYPE_PKCS12 = new KeyStoreType("PKCS12", "SUNJCE");
    /**
     * BKS 来自BouncyCastle Provider，它使用的也是TripleDES来保护密钥库中的Key，
     * 它能够防止证书库被不小心修改（Keystore的keyentry改掉1个 bit都会产生错误），
     * BKS能够跟JKS互操作，读者可以用Keytool去TryTry。
     */
    public static final KeyStoreType KEYSTORE_TYPE_BKS = new KeyStoreType("BKS", "BouncyCastle Provider");
    /**
     * UBER比较特别，当密码是通过命令行提供的时候，它只能跟keytool交互。
     * 整个keystore是通过PBE/SHA1/Twofish加密，因此keystore能够防止被误改、察看以及校验。
     * 以前，Sun JDK(提供者为SUN)允许你在不提供密码的情况下直接加载一个Keystore，类似cacerts，UBER不允许这种情况。
     */
    public static final KeyStoreType KEYSTORE_TYPE_UBER = new KeyStoreType("UBER", "BouncyCastle");


    public static List<KeyStoreType> getAllKeyStoreTypes() {
        Provider[] providers = Security.getProviders();
        return Pipeline.of(providers).map(new Function<Provider, List<KeyStoreType>>() {
            @Override
            public List<KeyStoreType> apply(final Provider provider) {
                return Pipeline.of(provider.stringPropertyNames()).filter(new Predicate<String>() {
                    @Override
                    public boolean test(String propertyName) {
                        return Strings.startsWithIgnoreCase(propertyName, "Alg.Alias.KeyStore.") || Strings.startsWithIgnoreCase(propertyName, "KeyStore.");
                    }
                }).map(new Function<String, String>() {
                    @Override
                    public String apply(String propertyName) {
                        String keystoreType = null;
                        if (Strings.startsWith(propertyName, "Alg.Alias.KeyStore.")) {
                            keystoreType = propertyName.substring("Alg.Alias.KeyStore.".length());
                        } else if (Strings.startsWith(propertyName, "KeyStore.")) {
                            keystoreType = propertyName.substring("KeyStore.".length());
                        }
                        return keystoreType;
                    }
                }).clearNulls().map(new Function<String, KeyStoreType>() {
                    @Override
                    public KeyStoreType apply(String keyStoreType) {
                        return new KeyStoreType(keyStoreType, provider.getName());
                    }
                }).asList();
            }
        }).flatMap(new Function<KeyStoreType, KeyStoreType>() {
            @Override
            public KeyStoreType apply(KeyStoreType input) {
                return input;
            }
        }).asList();
    }

    /**
     * Make a best guess about the "type" (see {@link KeyStore#getType()}) of the keystore file located at the given {@code Path}.
     * This method only references the <em>file name</em> of the keystore, it does not look at its contents.
     */
    public static String inferKeyStoreType(File path) {
        String name = path == null ? "" : path.toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".p12") || name.endsWith(".pfx") || name.endsWith(".pkcs12")) {
            return "PKCS12";
        } else {
            return "jks";
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
            KeyStore keyStore;
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
            Logger logger = Loggers.getLogger(PKIs.class);
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
            Logger logger = Loggers.getLogger(PKIs.class);
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
            Logger logger = Loggers.getLogger(PKIs.class);
            logger.warn("can't find a valid certificate, the alias is {}", alias);
        }
        return null;
    }

    public static List<Certificate> findCertificateChain(@NonNull KeyStore keyStore, @NonNull String alias) {
        try {
            if (!keyStore.containsAlias(alias)) {
                return Lists.immutableList();
            }
            Certificate[] certificates = keyStore.getCertificateChain(alias);
            return Collects.newArrayList(certificates);
        } catch (Throwable ex) {
            Logger logger = Loggers.getLogger(PKIs.class);
            logger.warn("can't find a valid certificate, the alias is {}", alias);
        }
        return Lists.immutableList();
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

    /**
     * Return a Set with all trusted X509Certificates contained in
     * this KeyStore.
     */
    public static Set<X509Certificate> getTrustedCerts(KeyStore ks) {
        Set<X509Certificate> set = new HashSet<X509Certificate>();
        try {
            for (Enumeration<String> e = ks.aliases(); e.hasMoreElements(); ) {
                String alias = e.nextElement();
                if (ks.isCertificateEntry(alias)) {
                    Certificate cert = ks.getCertificate(alias);
                    if (cert instanceof X509Certificate) {
                        set.add((X509Certificate)cert);
                    }
                } else if (ks.isKeyEntry(alias)) {
                    Certificate[] certs = ks.getCertificateChain(alias);
                    if ((certs != null) && (certs.length > 0) &&
                            (certs[0] instanceof X509Certificate)) {
                        set.add((X509Certificate)certs[0]);
                    }
                }
            }
        } catch (KeyStoreException e) {
            // ignore
        }
        return set;
    }

}
