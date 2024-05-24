package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.function.Supplier;

public interface BytesSaltGenerator extends Supplier<Integer, byte[]> {
    @Override
    byte[] get(Integer bytesLength);
}
