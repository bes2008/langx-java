package com.jn.langx.security.crypto.digest.spi.md2;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HmacMD2KeyGeneratorSpi extends BaseKeyGeneratorSpi {
    public HmacMD2KeyGeneratorSpi(){
        super("HMACMD2", 128);
    }
}
