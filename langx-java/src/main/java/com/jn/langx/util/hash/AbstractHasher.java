package com.jn.langx.util.hash;

public abstract class AbstractHasher implements Hasher {
    protected long seed;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * 用于流式计算
     */
    public void update(byte[] bytes, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(bytes[i]);
        }
    }

    protected void update(byte b) {
    }

    protected void reset() {
        setSeed(0);
    }
}
