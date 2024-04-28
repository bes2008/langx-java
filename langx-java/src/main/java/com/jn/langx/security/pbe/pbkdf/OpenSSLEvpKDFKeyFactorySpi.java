package com.jn.langx.security.pbe.pbkdf;

public class OpenSSLEvpKDFKeyFactorySpi extends PBKDFKeyFactorySpi {
    public OpenSSLEvpKDFKeyFactorySpi(String algorithm){
        super(algorithm, new OpenSSLEvpKDF());
    }
}
