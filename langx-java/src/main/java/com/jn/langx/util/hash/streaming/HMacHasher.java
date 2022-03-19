package com.jn.langx.util.hash.streaming;

import com.jn.langx.exception.IllegalParameterException;
import com.jn.langx.security.crypto.mac.HMacs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.reflect.type.Types;

import javax.crypto.Mac;
import java.util.List;

/**
 * @since 4.4.0
 */
public class HMacHasher extends AbstractStreamingHasher {
    public static final String HASHER_NAME_PREFIX = "hmac-";
    private Mac mac;

    public HMacHasher(){}

    public void setMac(Mac mac) {
        this.mac = mac;
    }

    public HMacHasher(String algorithm, byte[] secretKey) {
        Mac mac = HMacs.createMac(algorithm, secretKey);
        setMac(mac);
    }

    @Override
    public void update(byte[] bytes, int off, int len) {
        this.mac.update(bytes, off, len);
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
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
    protected AbstractHasher createInstance(Object initParams) {
        if (initParams == null || !Types.isArray(initParams.getClass())) {
            throw new IllegalParameterException("initParams");
        }
        List<Object> params = Pipeline.of(initParams).asList();
        Preconditions.checkArgument(params.size() >= 2);
        String algorithm = (String) params.get(0);
        byte[] secretKey = (byte[]) params.get(1);
        return new HMacHasher(algorithm, secretKey);
    }
}
