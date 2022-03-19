package com.jn.langx.util.hash;

import com.jn.langx.security.crypto.mac.HMacs;

import javax.crypto.Mac;

public class HMacHasher extends AbstractBytesResultHasher {
    public static final String HASHER_NAME_PREFIX = "hmac-";
    private Mac mac;

    public HMacHasher(String hmac) {
        this.mac = HMacs.createMac(hmac);
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        this.mac.update(bytes, off, len);
    }

    @Override
    protected void reset() {
        super.reset();
        this.mac.reset();
    }

    @Override
    public long getHash() {
        byte[] bytes = this.mac.doFinal();
        reset();
        return toLong(bytes);
    }

    @Override
    protected Hasher createInstance(long seed) {
        throw new UnsupportedOperationException();
    }
}
