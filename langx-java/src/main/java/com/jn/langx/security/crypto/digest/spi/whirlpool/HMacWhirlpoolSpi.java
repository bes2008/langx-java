package com.jn.langx.security.crypto.digest.spi.whirlpool;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacWhirlpoolSpi extends HmacCoreSpi {
    public HMacWhirlpoolSpi() {
        super("WHIRLPOOL");
    }
}
