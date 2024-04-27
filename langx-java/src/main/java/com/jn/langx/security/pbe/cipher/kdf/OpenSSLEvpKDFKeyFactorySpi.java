package com.jn.langx.security.pbe.cipher.kdf;

public class OpenSSLEvpKDFKeyFactorySpi extends PBKDFKeyFactorySpi {
    public OpenSSLEvpKDFKeyFactorySpi(String algorithm){
        super(algorithm, new EvpKDF());
    }
}
