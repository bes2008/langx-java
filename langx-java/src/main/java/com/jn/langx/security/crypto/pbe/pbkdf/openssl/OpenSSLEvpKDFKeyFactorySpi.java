package com.jn.langx.security.crypto.pbe.pbkdf.openssl;

import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeyFactorySpi;

/**
 * @since 5.3.9
 */
public class OpenSSLEvpKDFKeyFactorySpi extends PBKDFKeyFactorySpi {
    public OpenSSLEvpKDFKeyFactorySpi(String algorithm) {
        super(algorithm, new OpenSSLEvpKDF());
    }
}
