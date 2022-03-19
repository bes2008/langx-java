package com.jn.langx.util.hash;

import com.jn.langx.util.Preconditions;

public abstract class AbstractBytesResultHasher extends AbstractHasher{

    protected long toLong(byte[] bytes){
        try {
            return asLong(bytes);
        } catch (IllegalStateException ex) {
            return asInt(bytes);
        }
    }


    protected int asInt(byte[] bytes) {
        Preconditions.checkState(
                bytes.length >= 4,
                "AbstractBytesHasher#asInt() requires >= 4 bytes (it only has {} bytes).",
                bytes.length);
        return (bytes[0] & 0xFF)
                | ((bytes[1] & 0xFF) << 8)
                | ((bytes[2] & 0xFF) << 16)
                | ((bytes[3] & 0xFF) << 24);
    }

    protected long asLong(byte[] bytes) {
        Preconditions.checkState(
                bytes.length >= 8,
                "AbstractBytesHasher#asLong() requires >= 8 bytes (it only has {} bytes).",
                bytes.length);
        return padToLong(bytes);
    }

    protected long padToLong(byte[] bytes) {
        long retVal = (bytes[0] & 0xFF);
        for (int i = 1; i < Math.min(bytes.length, 8); i++) {
            retVal |= (bytes[i] & 0xFFL) << (i * 8);
        }
        return retVal;
    }
}
