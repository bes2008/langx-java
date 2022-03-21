package com.jn.langx.util.hash.streaming.crc;

import com.jn.langx.util.hash.streaming.AbstractStreamingHasher;

public class CrcHasher extends AbstractStreamingHasher {
    private CrcCalculator calculator;
    private long h;

    public CrcHasher(String name) {
        CrcAlgoMetadata metadata = CRCs.ALGO_METADATA_REGISTRY.get(name);
        this.calculator = new CrcCalculator(metadata);
        reset();
    }

    @Override
    public void setSeed(long seed) {
        this.seed = this.calculator.getInit();
        this.h = this.seed;
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        this.h = this.calculator.update(this.h, bytes, off, len);
    }

    @Override
    public long getHash() {
        long r = this.calculator.getHashResult(this.h);
        reset();
        return r;
    }
}
