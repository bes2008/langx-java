package com.jn.langx.security.crypto.salt;

public class FixedBytesSaltGenerator implements BytesSaltGenerator {
    private byte[] salt;

    public FixedBytesSaltGenerator(byte[] salt){
        this.salt=salt;
    }
    @Override
    public byte[] get(Integer bytesLength) {
        return salt;
    }
}
