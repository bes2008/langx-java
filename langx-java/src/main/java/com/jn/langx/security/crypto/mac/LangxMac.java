package com.jn.langx.security.crypto.mac;


import javax.crypto.Mac;
import javax.crypto.MacSpi;
import java.security.Provider;

class LangxMac extends Mac {

    public LangxMac(MacSpi spi, Provider provider, String algorithm) {
        super(spi, provider, algorithm);
    }

}
