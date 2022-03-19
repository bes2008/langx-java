package com.jn.langx.util.hash;

import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Reflects;

public abstract class AbstractHasher implements Hasher {
    protected long seed;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * 用于流式计算
     */
    public void update(byte[] bytes, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(bytes[i]);
        }
    }

    protected void update(byte b) {
    }

    protected void reset() {
        setSeed(0);
    }

    @Override
    public String getName() {
        String name = Reflects.getSimpleClassName(this);
        if (name.endsWith("Hasher")) {
            name = name.substring(0, name.length() - "Hasher".length());
        }
        name = Strings.lowerCase(name);
        return name;
    }

    @Override
    public final Hasher get(Long seed) {
        if(seed==null){
            seed = 0L;
        }
        return createInstance(seed);
    }

    protected abstract Hasher createInstance(long seed);
}
