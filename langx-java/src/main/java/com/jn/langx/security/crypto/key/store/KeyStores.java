package com.jn.langx.security.crypto.key.store;

import com.jn.langx.security.Securitys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.security.Provider;
import java.security.Security;
import java.util.List;

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
}
