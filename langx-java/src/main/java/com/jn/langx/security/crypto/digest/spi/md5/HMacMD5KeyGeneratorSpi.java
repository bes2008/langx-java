package com.jn.langx.security.crypto.digest.spi.md5;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HMacMD5KeyGeneratorSpi extends BaseKeyGeneratorSpi {
    public HMacMD5KeyGeneratorSpi(){
        super("HMACMD5", 128);
    }
}
