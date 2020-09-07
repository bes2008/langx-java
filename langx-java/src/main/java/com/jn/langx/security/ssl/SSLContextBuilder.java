package com.jn.langx.security.ssl;


import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;


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
public class SSLContextBuilder {

    static final String TLS = "TLS";

    private String protocol;
    private final Set<KeyManager> keymanagers;
    private final Set<TrustManager> trustmanagers;
    private SecureRandom secureRandom;

    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    public SSLContextBuilder() {
        super();
        this.keymanagers = new LinkedHashSet<KeyManager>();
        this.trustmanagers = new LinkedHashSet<TrustManager>();
    }

    public SSLContextBuilder useProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public SSLContextBuilder setSecureRandom(final SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }

    public SSLContextBuilder loadTrustMaterial( final KeyStore truststore,final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(truststore);
        final TrustManager[] tms = tmfactory.getTrustManagers();
        if (tms != null) {
            if (trustStrategy != null) {
                for (int i = 0; i < tms.length; i++) {
                    final TrustManager tm = tms[i];
                    if (tm instanceof X509TrustManager) {
                        tms[i] = new InternalTrustManagerProxy((X509TrustManager) tm, trustStrategy);
                    }
                }
            }
            Collects.addAll(this.trustmanagers, tms);
        }
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        return loadTrustMaterial(null, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(
            final File file,
            final char[] storePassword,
            final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Preconditions.checkNotNull(file, "Truststore file");
        final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        final FileInputStream instream = new FileInputStream(file);
        try {
            trustStore.load(instream, storePassword);
        } finally {
            instream.close();
        }
        return loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(
            final File file,
            final char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(file, storePassword, null);
    }

    public SSLContextBuilder loadTrustMaterial(
            final File file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(file, null);
    }

    public SSLContextBuilder loadTrustMaterial(
            final URL url,
            final char[] storePassword,
            final TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Preconditions.checkNotNull(url, "Truststore URL");
        final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        final InputStream instream = url.openStream();
        try {
            trustStore.load(instream, storePassword);
        } finally {
            instream.close();
        }
        return loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(final URL url, final char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return loadTrustMaterial(url, storePassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(final KeyStore keystore, final char[] keyPassword, final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keyPassword);
        final KeyManager[] kms = kmfactory.getKeyManagers();
        if (kms != null) {
            if (aliasStrategy != null) {
                for (int i = 0; i < kms.length; i++) {
                    final KeyManager km = kms[i];
                    if (km instanceof X509ExtendedKeyManager) {
                        kms[i] = new InternalKeyManagerProxy((X509ExtendedKeyManager) km, aliasStrategy);
                    }
                }
            }
            Collects.addAll(keymanagers, kms);
        }
        return this;
    }

    public SSLContextBuilder loadKeyMaterial(
            final KeyStore keystore,
            final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return loadKeyMaterial(keystore, keyPassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(
            final File file,
            final char[] storePassword,
            final char[] keyPassword,
            final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Preconditions.checkNotNull(file, "Keystore file");
        final KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
        final FileInputStream instream = new FileInputStream(file);
        try {
            identityStore.load(instream, storePassword);
        } finally {
            instream.close();
        }
        return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(final File file, final char[] storePassword, final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return loadKeyMaterial(file, storePassword, keyPassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(final URL url, final char[] storePassword, final char[] keyPassword, final PrivateKeyAliasChooseStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Preconditions.checkNotNull(url, "Keystore URL");
        final KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
        final InputStream instream = url.openStream();
        try {
            identityStore.load(instream, storePassword);
        } finally {
            instream.close();
        }
        return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(
            final URL url,
            final char[] storePassword,
            final char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return loadKeyMaterial(url, storePassword, keyPassword, null);
    }

    protected void initSSLContext(
            final SSLContext sslcontext,
            final Collection<KeyManager> keyManagers,
            final Collection<TrustManager> trustManagers,
            final SecureRandom secureRandom) throws KeyManagementException {
        sslcontext.init(
                !keyManagers.isEmpty() ? keyManagers.toArray(new KeyManager[keyManagers.size()]) : null,
                !trustManagers.isEmpty() ? trustManagers.toArray(new TrustManager[trustManagers.size()]) : null,
                secureRandom);
    }

    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sslcontext = SSLContext.getInstance(
                this.protocol != null ? this.protocol : TLS);
        initSSLContext(sslcontext, keymanagers, trustmanagers, secureRandom);
        return sslcontext;
    }

}
