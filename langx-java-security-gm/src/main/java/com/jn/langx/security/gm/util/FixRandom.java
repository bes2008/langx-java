package com.jn.langx.security.gm.util;


import java.math.BigInteger;
import java.security.SecureRandom;

public class FixRandom extends SecureRandom
{
    private BigInteger random;

    public FixRandom(final BigInteger random) {
        this.random = null;
        this.random = random;
    }

    @Override
    public synchronized void nextBytes(final byte[] array) {
        final byte[] intToBytes = _utils.intToBytes(this.random);
        if (array.length != intToBytes.length) {
            throw new RuntimeException("invalid random length");
        }
        System.arraycopy(intToBytes, 0, array, 0, array.length);
    }
}
