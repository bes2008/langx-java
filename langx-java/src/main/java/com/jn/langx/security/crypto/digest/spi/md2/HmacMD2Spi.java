package com.jn.langx.security.crypto.digest.spi.md2;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HmacMD2Spi extends HmacCoreSpi {
    public HmacMD2Spi(){
        super("MD2");
    }
}
