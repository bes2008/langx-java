package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.Emptys;

public class EmptySaltGenerator implements BytesSaltGenerator{
    @Override
    public byte[] get(Integer bytesLength) {
        return Emptys.EMPTY_BYTES;
    }
}
