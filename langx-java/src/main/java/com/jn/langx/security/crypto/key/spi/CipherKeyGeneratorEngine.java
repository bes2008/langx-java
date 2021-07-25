package com.jn.langx.security.crypto.key.spi;

import java.security.SecureRandom;

public class CipherKeyGeneratorEngine {
    protected SecureRandom random;
    protected int strength;

    /**
     * initialise the key generator.
     */
    public void init(int strength, SecureRandom random) {
        this.random = random;
        this.strength = (strength + 7) / 8;
    }

    /**
     * generate a secret key.
     *
     * @return a byte array containing the key value.
     */
    public byte[] generateKey() {
        byte[] key = new byte[strength];
        random.nextBytes(key);
        return key;
    }
}
