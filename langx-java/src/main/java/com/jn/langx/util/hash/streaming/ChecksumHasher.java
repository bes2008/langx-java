package com.jn.langx.util.hash.streaming;

import com.jn.langx.util.hash.Hasher;

import java.util.zip.Checksum;

public abstract class ChecksumHasher extends AbstractStreamingHasher {
    private Checksum checksum;

    public ChecksumHasher(Checksum checksum) {
        this.checksum = checksum;
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        this.checksum.update(bytes, off, len);
    }

    @Override
    protected void update(byte b) {
        this.checksum.update(b);
    }

    @Override
    protected void reset() {
        super.reset();
        this.checksum.reset();
    }

    @Override
    public long getHash() {
        long r = this.checksum.getValue();
        reset();
        return r;
    }

    @Override
    protected Hasher createInstance(long seed) {
        throw new UnsupportedOperationException();
    }
}
