package com.jn.langx.security.ssl;



import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;

import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Private key details.
 *
 * @since 2.8.8
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
        return Arrs.copy(certChain);
    }

    @Override
    public String toString() {
        return type + ':' + Arrays.toString(certChain);
    }

}
