package com.jn.langx.security.crypto.key;

import java.security.SecureRandom;

public class CipherKeyGenerator {
    protected SecureRandom random;
    protected int bytesLength;

    /**
     * initialise the key generator.
     */
    public void init(int bitLength, SecureRandom random) {
        this.random = random;
        this.bytesLength = (bitLength + 7) / 8;
    }

    /**
     * generate a secret key.
     *
     * @return a byte array containing the key value.
     */
    public byte[] generateKey() {
        byte[] key = new byte[bytesLength];
        random.nextBytes(key);
        return key;
    }
}
