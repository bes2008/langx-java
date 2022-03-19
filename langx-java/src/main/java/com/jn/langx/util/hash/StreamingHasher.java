package com.jn.langx.util.hash;


/**
 * 流式使用方法：
 * <pre>
 * 1） hasher.setSeed()
 * 2）多次调用 hasher.update(byte[]) 动态更新数据
 * 3） 调用 getHash() 获取计算后的Hash值
 * </pre>
 * <p>
 * 一次性Hash使用方法：
 * <pre>
 * hasher.hash(byte[])
 * </pre>
 *
 * @since 4.4.0
 */
public interface StreamingHasher extends Hasher {

    void setSeed(long seed);

    /**
     * 用于流式计算
     */
    void update(byte[] bytes, int off, int len);

    long getHash();
}
