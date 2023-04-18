package com.jn.langx.util.hash.streaming;

import com.jn.langx.util.hash.AbstractHasher;
import com.jn.langx.util.hash.StreamingHasher;

/**
 * @since 4.4.0
 */
public abstract class AbstractStreamingHasher extends AbstractHasher implements StreamingHasher {

    protected AbstractStreamingHasher(){

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
     * 用于流式计算
     */
    public void update(byte[] bytes, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(bytes[i]);
        }
    }

    protected void update(byte b) {
    }

    public void reset() {
        setSeed(0);
    }


}
