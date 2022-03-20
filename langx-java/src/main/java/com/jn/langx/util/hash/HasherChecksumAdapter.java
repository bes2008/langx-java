package com.jn.langx.util.hash;

import java.util.zip.Checksum;

public class HasherChecksumAdapter implements Checksum {
    private StreamingHasher hasher;

    public HasherChecksumAdapter(StreamingHasher hasher){
        this.hasher = hasher;
    }

    @Override
    public void update(int b) {
        byte[] bytes = new byte[]{(byte)b};
        update(bytes, 0, 1);
    }

    @Override
    public void update(byte[] b, int off, int len) {
        hasher.update(b, off, len);
    }

    @Override
    public long getValue() {
        return hasher.getHash();
    }

    @Override
    public void reset() {
        hasher.reset();
    }
}
