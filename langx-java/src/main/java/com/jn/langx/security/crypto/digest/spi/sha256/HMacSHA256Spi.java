package com.jn.langx.security.crypto.digest.spi.sha256;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;


public class HMacSHA256Spi extends HmacCoreSpi {
    public HMacSHA256Spi(){
        super("SHA-256");
    }
}
