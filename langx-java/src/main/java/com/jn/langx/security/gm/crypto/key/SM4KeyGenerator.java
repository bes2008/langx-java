package com.jn.langx.security.gm.crypto.key;

import com.jn.langx.security.crypto.key.SecureRandoms;
import com.jn.langx.security.crypto.key.CipherKeyGenerator;

import java.security.SecureRandom;

public class SM4KeyGenerator {
    private CipherKeyGenerator engine;

    public SM4KeyGenerator() {
        this(SecureRandoms.getSHA1PRNG());
    }

    public SM4KeyGenerator(SecureRandom random) {
        this.engine = new CipherKeyGenerator();
        this.engine.init(128, random);
    }

    public byte[] genSecretKey() {
        return this.engine.generateKey();
    }

}
