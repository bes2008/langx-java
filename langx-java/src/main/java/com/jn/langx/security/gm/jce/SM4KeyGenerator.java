package com.jn.langx.security.gm.jce;

import com.jn.langx.security.crypto.key.SecureRandoms;
import com.jn.langx.security.crypto.key.spi.CipherKeyGeneratorEngine;

import java.security.SecureRandom;

public class SM4KeyGenerator {
    private CipherKeyGeneratorEngine engine;

    public SM4KeyGenerator() {
        this(SecureRandoms.getSHA1PRNG());
    }

    public SM4KeyGenerator(SecureRandom random) {
        this.engine = new CipherKeyGeneratorEngine();
        this.engine.init(128, random);
    }

    public byte[] genSecretKey() {
        return this.engine.generateKey();
    }

}
