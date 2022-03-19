package com.jn.langx.util.hash.streaming;

import com.jn.langx.util.hash.Hasher;

import java.util.zip.CRC32;

public class Crc32Hasher extends ChecksumHasher {
    public Crc32Hasher() {
        super(new CRC32());
    }

    @Override
    protected Hasher createInstance(long seed) {
        return new Crc32Hasher();
    }
}
