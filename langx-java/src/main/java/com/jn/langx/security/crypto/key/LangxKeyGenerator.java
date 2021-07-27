package com.jn.langx.security.crypto.key;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;
import java.security.Provider;

class LangxKeyGenerator extends KeyGenerator {
    public LangxKeyGenerator(KeyGeneratorSpi keyGeneratorSpi, Provider provider, String algorithm) {
        super(keyGeneratorSpi, provider, algorithm);
    }
}
