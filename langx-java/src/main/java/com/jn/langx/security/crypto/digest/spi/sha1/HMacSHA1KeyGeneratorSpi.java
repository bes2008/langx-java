package com.jn.langx.security.crypto.digest.spi.sha1;

import com.jn.langx.security.crypto.key.spi.BaseKeyGeneratorSpi;

public class HMacSHA1KeyGeneratorSpi extends BaseKeyGeneratorSpi {
    public HMacSHA1KeyGeneratorSpi(){
        super("HMACSHA1",160);
    }

}
