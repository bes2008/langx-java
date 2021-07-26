package com.jn.langx.security.crypto.digest.spi.sha1;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacSHA1Spi extends HmacCoreSpi {
    public HMacSHA1Spi(){
        super("SHA1");
    }
}
