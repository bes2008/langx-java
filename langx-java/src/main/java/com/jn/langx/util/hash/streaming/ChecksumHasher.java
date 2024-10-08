package com.jn.langx.util.hash.streaming;

import java.util.zip.Checksum;

/**
 * @since 4.4.0
 */
public abstract class ChecksumHasher extends AbstractStreamingHasher {
    private Checksum checksum;

    protected ChecksumHasher(Checksum checksum) {
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
    public void reset() {
        super.reset();
        if(this.checksum!=null) {
            this.checksum.reset();
        }
    }

    @Override
    public long getHash() {
        long r = this.checksum.getValue();
        reset();
        return r;
    }

}
