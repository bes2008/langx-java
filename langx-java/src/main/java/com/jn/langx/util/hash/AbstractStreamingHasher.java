package com.jn.langx.util.hash;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Reflects;

public abstract class AbstractStreamingHasher implements StreamingHasher {
    protected long seed;

    /*********************************************************************************
     * 一次性计算
     *********************************************************************************/

    /**
     * 计算hash
     * @param bytes input bytes
     * @return hash值
     */
    public long hash(byte[] bytes) {
        return hash( bytes, bytes.length, 0);
    }

    public long hash(byte[] bytes, long seed) {
        return hash( bytes, bytes.length, seed);
    }

    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes  input bytes
     * @param length length of the valid bytes to consider
     * @param seed   seed value
     * @return hash value
     */
    public long hash(byte[] bytes, int length, long seed) {
        this.setSeed(seed);
        this.update(bytes, 0, length);
        return this.getHash();
    }

    /****************************************************************************
     *  下面的几个方式，用于 流式计算
     ****************************************************************************/

    /**
     *
     * @param seed 也是初始值
     */
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


    /********************************************************************************
     * 当 hash 计算后，结果如果是 byte[] 时，需要使用下面的几个方法进行转换
     ********************************************************************************/
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



    /********************************************************************************
     *  其他方法
     ********************************************************************************/

    /**
     *
     * @return the hasher name
     */
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
        if (seed == null) {
            seed = 0L;
        }
        return createInstance(seed);
    }

    protected abstract Hasher createInstance(long seed);
}
