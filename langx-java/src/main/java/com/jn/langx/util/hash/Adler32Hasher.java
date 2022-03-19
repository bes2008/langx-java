package com.jn.langx.util.hash;

import java.util.zip.Adler32;

public class Adler32Hasher extends ChecksumHasher{
    public Adler32Hasher() {
        super(new Adler32());
    }

    @Override
    protected Hasher createInstance(long seed) {
        return new Adler32Hasher();
    }
}
