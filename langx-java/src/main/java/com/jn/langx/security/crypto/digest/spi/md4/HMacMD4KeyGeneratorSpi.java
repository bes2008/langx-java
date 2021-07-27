package com.jn.langx.security.crypto.digest.spi.md4;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HMacMD4KeyGeneratorSpi extends BaseKeyGeneratorSpi {
    public HMacMD4KeyGeneratorSpi() {
        super("HMACMD4", 128);
    }
}
