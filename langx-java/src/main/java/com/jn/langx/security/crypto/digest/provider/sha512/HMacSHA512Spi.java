package com.jn.langx.security.crypto.digest.provider.sha512;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacSHA512Spi extends HmacCoreSpi {
    public HMacSHA512Spi(){
        super("SHA-512");
    }
}
