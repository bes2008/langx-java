package com.jn.langx.security.crypto.digest.provider.md4;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HmacMD4Spi extends HmacCoreSpi {
    public HmacMD4Spi(){
        super("MD4");
    }
}
