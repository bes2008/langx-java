package com.jn.langx.security.ssl;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A trust strategy that accepts self-signed certificates as trusted. Verification of all other
 * certificates is done by the trust manager configured in the SSL context.
 *
 */
public class TrustSelfSignedStrategy implements TrustStrategy {

    public static final TrustSelfSignedStrategy INSTANCE = new TrustSelfSignedStrategy();

    @Override
    public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        return chain.length == 1;
    }

}
