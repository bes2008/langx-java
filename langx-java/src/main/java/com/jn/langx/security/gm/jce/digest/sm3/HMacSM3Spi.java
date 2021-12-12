package com.jn.langx.security.gm.jce.digest.sm3;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;


public class HMacSM3Spi extends HmacCoreSpi {
    public HMacSM3Spi() {
        super("SM3", 64);
    }
}
