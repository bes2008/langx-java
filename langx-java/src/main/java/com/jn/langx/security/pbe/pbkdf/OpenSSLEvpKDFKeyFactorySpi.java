package com.jn.langx.security.pbe.pbkdf;

/**
 * @since 5.3.9
 */
public class OpenSSLEvpKDFKeyFactorySpi extends PBKDFKeyFactorySpi {
    public OpenSSLEvpKDFKeyFactorySpi(String algorithm){
        super(algorithm, new OpenSSLEvpKDF());
    }
}
