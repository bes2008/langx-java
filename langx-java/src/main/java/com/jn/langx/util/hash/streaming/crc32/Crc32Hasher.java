package com.jn.langx.util.hash.streaming.crc32;

import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.hash.streaming.ChecksumHasher;

import java.util.zip.CRC32;

/**
 * @since 4.4.0
 */
public class Crc32Hasher extends ChecksumHasher {
    public Crc32Hasher() {
        super(new CRC32());
    }

    @Override
    protected AbstractHasher createInstance(Object initParam) {
        return new Crc32Hasher();
    }
}
