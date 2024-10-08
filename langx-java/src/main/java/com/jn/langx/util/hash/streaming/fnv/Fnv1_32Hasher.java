package com.jn.langx.util.hash.streaming.fnv;

import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.hash.streaming.AbstractStreamingHasher;

/**
 * @since 4.4.0
 */
public class Fnv1_32Hasher extends AbstractStreamingHasher {
    private static final int INITIAL_VALUE = 0x811C9DC5;
    private static final int MULTIPLIER = 16777619;

    private int hash = INITIAL_VALUE;

    @Override
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
    protected AbstractHasher createInstance(Object initParam) {
        return new Fnv1_32Hasher();
    }
}
