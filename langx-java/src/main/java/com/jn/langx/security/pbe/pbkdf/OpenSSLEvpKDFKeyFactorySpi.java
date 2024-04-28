package com.jn.langx.security.pbe.pbkdf;

import java.security.SecureRandom;

public class OpenSSLEvpKDFKeyFactorySpi extends PBKDFKeyFactorySpi {
    public OpenSSLEvpKDFKeyFactorySpi(String algorithm, SecureRandom secureRandom){
        super(algorithm, secureRandom, new OpenSSLEvpKDF());
    }
}
