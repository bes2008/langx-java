package com.jn.langx.security.salt;

import com.jn.langx.util.Emptys;

public class EmptySaltGenerator implements BytesSaltGenerator{
    @Override
    public byte[] get(Integer bytesLength) {
        return Emptys.EMPTY_BYTES;
    }
}
