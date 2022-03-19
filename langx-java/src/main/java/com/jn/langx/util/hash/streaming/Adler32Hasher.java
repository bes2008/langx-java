package com.jn.langx.util.hash.streaming;

import com.jn.langx.util.hash.AbstractHasher;

import java.util.zip.Adler32;

/**
 * @since 4.4.0
 */
public class Adler32Hasher extends ChecksumHasher {
    public Adler32Hasher() {
        super(new Adler32());
    }

    @Override
    protected AbstractHasher createInstance() {
        return new Adler32Hasher();
    }
}
