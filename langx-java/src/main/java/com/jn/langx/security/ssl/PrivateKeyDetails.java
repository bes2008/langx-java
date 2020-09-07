package com.jn.langx.security.ssl;



import com.jn.langx.util.Preconditions;

import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Private key details.
 *
 * @since 4.4
 */
public final class PrivateKeyDetails {

    private final String type;
    private final X509Certificate[] certChain;

    public PrivateKeyDetails(final String type, final X509Certificate[] certChain) {
        super();
        this.type = Preconditions.checkNotNull(type, "Private key type");
        this.certChain = certChain;
    }

    public String getType() {
        return type;
    }

    public X509Certificate[] getCertChain() {
        return certChain;
    }

    @Override
    public String toString() {
        return type + ':' + Arrays.toString(certChain);
    }

}
