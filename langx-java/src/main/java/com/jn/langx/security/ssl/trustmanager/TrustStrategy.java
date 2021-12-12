package com.jn.langx.security.ssl.trustmanager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A strategy to establish trustworthiness of certificates without consulting the trust manager
 * configured in the actual SSL context. This interface can be used to override the standard
 * JSSE certificate verification process.
 *
 */
public interface TrustStrategy {

    /**
     * Determines whether the certificate chain can be trusted without consulting the trust manager
     * configured in the actual SSL context. This method can be used to override the standard JSSE
     * certificate verification process.
     * <p>
     * Please note that, if this method returns {@code false}, the trust manager configured
     * in the actual SSL context can still clear the certificate as trusted.
     *
     * @param chain the peer certificate chain
     * @param authType the authentication type based on the client certificate
     * @return {@code true} if the certificate can be trusted without verification by
     *   the trust manager, {@code false} otherwise.
     * @throws CertificateException thrown if the certificate is not trusted or invalid.
     */
    boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException;

}
