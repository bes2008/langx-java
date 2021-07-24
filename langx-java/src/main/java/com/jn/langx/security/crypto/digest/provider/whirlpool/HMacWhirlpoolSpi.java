package com.jn.langx.security.crypto.digest.provider.whirlpool;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacWhirlpoolSpi extends HmacCoreSpi {
    public HMacWhirlpoolSpi(){
        super("Whirlpool");
    }
}
