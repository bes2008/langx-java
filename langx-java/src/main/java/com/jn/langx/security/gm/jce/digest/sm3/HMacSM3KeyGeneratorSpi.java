package com.jn.langx.security.gm.jce.digest.sm3;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HMacSM3KeyGeneratorSpi extends BaseKeyGeneratorSpi {
    public HMacSM3KeyGeneratorSpi() {
        super("HMACSM3", 256);
    }
}
