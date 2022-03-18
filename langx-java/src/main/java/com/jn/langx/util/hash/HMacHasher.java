package com.jn.langx.util.hash;

import com.jn.langx.security.crypto.mac.HMacs;

import javax.crypto.Mac;

public class HMacHasher extends AbstractBytesHasher {
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
    public long get() {
        byte[] bytes = this.mac.doFinal();
        reset();
        return toLong(bytes);
    }
}
