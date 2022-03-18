package com.jn.langx.util.hash;

import java.util.zip.Checksum;

public class ChecksumHasher extends Hasher {
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
    public long get() {
        long r = this.checksum.getValue();
        reset();
        return r;
    }
}
