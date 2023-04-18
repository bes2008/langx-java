package com.jn.langx.security.ssl;


import com.jn.langx.Builder;
import com.jn.langx.security.crypto.key.store.KeyStores;
import com.jn.langx.security.ssl.keymanager.InternalKeyManagerProxy;
import com.jn.langx.security.ssl.keymanager.PrivateKeyAliasChooseStrategy;
import com.jn.langx.security.ssl.trustmanager.InternalTrustManagerProxy;
import com.jn.langx.security.ssl.trustmanager.TrustStrategy;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.io.IOs;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Builder for {@link javax.net.ssl.SSLContext} instances.
 * <p>
 * Please note: the default Oracle JSSE implementation of {@link SSLContext#init(KeyManager[], TrustManager[], SecureRandom)}
 * accepts multiple key and trust managers, however only only first matching type is ever used.
 * See for example:
 * <a href="http://docs.oracle.com/javase/7/docs/api/javax/net/ssl/SSLContext.html#init%28javax.net.ssl.KeyManager[],%20javax.net.ssl.TrustManager[],%20java.security.SecureRandom%29">
 * SSLContext.html#init
 * </a>
 */
public class SSLContextBuilder implements Builder<SSLContext> {

    private String protocol = SSLs.TLS;
    private final Set<KeyManager> keyManagers;
    private final Set<TrustManager> trustManagers;
    private SecureRandom secureRandom;

    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    public SSLContextBuilder() {
        super();
        this.keyManagers = new LinkedHashSet<KeyManager>();
        this.trustManagers = new LinkedHashSet<TrustManager>();
    }

    public SSLContextBuilder setProtocol(String protocol) {
        protocol = Strings.useValueIfEmpty(protocol, SSLs.TLS);
        SSLProtocolVersion protocolVersion = null;
        if ("SSL".equals(protocol)) {
            protocolVersion = SSLProtocolVersion.SSLv30;
        } else {
            protocolVersion = Enums.ofName(SSLProtocolVersion.class, protocol);
        }
        return setProtocol(protocolVersion);
    }

    public SSLContextBuilder setProtocol(final SSLProtocolVersion protocol) {
        this.protocol = protocol == null ? SSLs.TLS : protocol.getName();
        return this;
    }

    public SSLContextBuilder setSecureRandom(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(final KeyStore trustStore, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmFactory.init(trustStore);
        final TrustManager[] tms = tmFactory.getTrustManagers();
        if (tms != null) {
            if (trustStrategy != null) {
                for (int i = 0; i < tms.length; i++) {
                    final TrustManager tm = tms[i];
                    if (tm instanceof X509TrustManager) {
                        tms[i] = new InternalTrustManagerProxy((X509TrustManager) tm, trustStrategy);
                    }
                }
            }
            Collects.addAll(this.trustManagers, tms);
        }
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(null, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(final File file, final char[] storePassword, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException{
        Preconditions.checkNotNullArgument(file, "Truststore file");
        final KeyStore trustStore = KeyStores.getKeyStore(KeyStore.getDefaultType(), null, file, storePassword);
        return loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(final File file, final char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(file, storePassword, null);
    }

    public SSLContextBuilder loadTrustMaterial(final File file) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(file, null);
    }

    public SSLContextBuilder loadTrustMaterial(final URL url, final char[] storePassword, final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, IOException {
        Preconditions.checkNotNullArgument(url, "Truststore URL");
        KeyStore trustStore = null;
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            trustStore = KeyStores.getKeyStore(KeyStore.getDefaultType(), null, inputStream, storePassword);
        } finally {
            IOs.close(inputStream);
        }
        return loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(final URL url, final char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, IOException {
        return loadTrustMaterial(url, storePassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(final KeyStore keystore, final char[] keyPassword, final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmFactory.init(keystore, keyPassword);
        final KeyManager[] kms = kmFactory.getKeyManagers();
        if (kms != null) {
            if (aliasStrategy != null) {
                for (int i = 0; i < kms.length; i++) {
                    final KeyManager km = kms[i];
                    if (km instanceof X509ExtendedKeyManager) {
                        kms[i] = new InternalKeyManagerProxy((X509ExtendedKeyManager) km, aliasStrategy);
                    }
                }
            }
            Collects.addAll(keyManagers, kms);
        }
        return this;
    }

    public SSLContextBuilder loadKeyMaterial(final KeyStore keystore, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return loadKeyMaterial(keystore, keyPassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(final File file, final char[] storePassword, final char[] keyPassword, final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        Preconditions.checkNotNullArgument(file, "Keystore file");
        final KeyStore identityStore = KeyStores.getKeyStore(KeyStore.getDefaultType(), null, file, storePassword);
        return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(final File file, final char[] storePassword, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return loadKeyMaterial(file, storePassword, keyPassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(final URL url, final char[] storePassword, final char[] keyPassword, final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IOException {
        Preconditions.checkNotNullArgument(url, "Keystore URL");
        KeyStore identityStore = null;
        InputStream inputStream = url.openStream();
        try {
            identityStore = KeyStores.getKeyStore(KeyStore.getDefaultType(), null, inputStream, storePassword);
        } finally {
            IOs.close(inputStream);
        }
        return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(final URL url, final char[] storePassword, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IOException {
        return loadKeyMaterial(url, storePassword, keyPassword, null);
    }

    protected void initSSLContext(final SSLContext sslcontext, final Collection<KeyManager> keyManagers, final Collection<TrustManager> trustManagers, final SecureRandom secureRandom) throws KeyManagementException {
        sslcontext.init(!keyManagers.isEmpty() ? keyManagers.toArray(new KeyManager[keyManagers.size()]) : null,
                !trustManagers.isEmpty() ? trustManagers.toArray(new TrustManager[trustManagers.size()]) : null,
                secureRandom);
    }

    public SSLContext build() {
        try {
            final SSLContext sslcontext = SSLContext.getInstance(this.protocol);
            initSSLContext(sslcontext, keyManagers, trustManagers, secureRandom);
            return sslcontext;
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

}
