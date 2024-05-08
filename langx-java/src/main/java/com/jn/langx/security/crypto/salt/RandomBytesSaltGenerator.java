package com.jn.langx.security.crypto.salt;

import com.jn.langx.security.Securitys;

public class RandomBytesSaltGenerator implements BytesSaltGenerator{
    @Override
    public byte[] get(Integer bytesLength) {
        return Securitys.randomBytes(bytesLength * 8);
    }
}
