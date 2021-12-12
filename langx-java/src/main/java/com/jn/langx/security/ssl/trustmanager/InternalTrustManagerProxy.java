package com.jn.langx.security.ssl.trustmanager;

import com.jn.langx.security.ssl.trustmanager.TrustStrategy;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public final class InternalTrustManagerProxy implements X509TrustManager{
    private final X509TrustManager trustManager;
    private final TrustStrategy trustStrategy;

    InternalTrustManagerProxy(final X509TrustManager trustManager, final TrustStrategy trustStrategy) {
        super();
        this.trustManager = trustManager;
        this.trustStrategy = trustStrategy;
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        this.trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        if (!this.trustStrategy.isTrusted(chain, authType)) {
            this.trustManager.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }

}
