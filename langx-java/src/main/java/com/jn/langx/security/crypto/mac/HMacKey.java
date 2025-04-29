package com.jn.langx.security.crypto.mac;

import com.jn.langx.util.Preconditions;

import java.security.Key;

public class HMacKey implements Key {
    private byte[] key;
    private String algorithm = "";
    private String format = "";

    public HMacKey(byte[] key) {
        Preconditions.checkNotEmpty(key, "The hmacKey must not be null or empty");
        this.key = key;
    }

    public HMacKey(Key key) {
        this.key = key.getEncoded();
        this.algorithm = key.getAlgorithm();
        this.format = key.getFormat();
    }

    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }

    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public byte[] getEncoded() {
        return key;
    }
}
