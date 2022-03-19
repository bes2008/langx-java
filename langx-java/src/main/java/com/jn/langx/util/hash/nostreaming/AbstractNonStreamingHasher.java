package com.jn.langx.util.hash.nostreaming;

import com.jn.langx.util.hash.AbstractHasher;

public abstract class AbstractNonStreamingHasher extends AbstractHasher {
    @Override
    public long hash(byte[] bytes, int length, long seed) {
        setSeed(seed);
        return doFinal(bytes, 0, length);
    }

    protected abstract long doFinal(byte[] bytes, int off, int len);
}
