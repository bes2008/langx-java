package com.jn.langx.util.hash.impl;

import com.jn.langx.util.hash.AbstractStreamingHasher;
import com.jn.langx.util.hash.Hasher;

public class Fnv1_32Hasher extends AbstractStreamingHasher {
    private final static int INITIAL_VALUE = 0x811C9DC5;
    private final static int MULTIPLIER = 16777619;

    private int hash = INITIAL_VALUE;

    public void update(byte b) {
        hash *= MULTIPLIER;
        hash ^= 0xff & b;
    }

    @Override
    public long getHash() {
        long h = hash & 0xffffffffL;
        reset();
        return h;
    }

    @Override
    public void reset() {
        hash = INITIAL_VALUE;
    }

    @Override
    protected Hasher createInstance(long seed) {
        return new Fnv1_32Hasher();
    }
}
