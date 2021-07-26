package com.jn.langx.security.crypto.digest.spi.whirlpool;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HMacWhirlpoolKeyGenSpi extends BaseKeyGeneratorSpi {
    public HMacWhirlpoolKeyGenSpi() {
        super("HMACWHIRLPOOL", 512);
    }
}
