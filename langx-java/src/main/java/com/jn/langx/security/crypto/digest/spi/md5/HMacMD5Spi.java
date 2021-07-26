package com.jn.langx.security.crypto.digest.spi.md5;

import com.jn.langx.security.crypto.mac.HmacCoreSpi;

public class HMacMD5Spi extends HmacCoreSpi {
    public HMacMD5Spi() {
        super("MD5");
    }
}
