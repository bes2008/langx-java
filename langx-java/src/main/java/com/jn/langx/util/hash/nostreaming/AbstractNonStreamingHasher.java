package com.jn.langx.util.hash.nostreaming;

import com.jn.langx.util.hash.AbstractHasher;

/**
 * @since 4.4.0
 */
public abstract class AbstractNonStreamingHasher extends AbstractHasher {
    @Override
    public long hash(byte[] bytes, int length, long seed) {
        setSeed(seed);
        return doFinal(bytes, 0, length);
    }

    /**
     * 对指定bytes 范围进行 hash运算
     *
     * @param bytes bytes数组
     * @param off   起始位置
     * @param len   要进行hash的byte长度
     *
     */
    protected abstract long doFinal(byte[] bytes, int off, int len);
}
