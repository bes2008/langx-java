package com.jn.langx.util.hash;

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
