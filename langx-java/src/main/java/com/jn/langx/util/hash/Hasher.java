package com.jn.langx.util.hash;


/**
 * 使用方法：
 * <pre>
 * 1） hasher.setSeed()
 * 2）多次调用 hasher.update(byte[]) 动态更新数据
 * 3） 调用 get() 获取计算后的Hash值
 * </pre>
 */
public interface Hasher {

    void setSeed(long seed);

    /**
     * 用于流式计算
     */
    void update(byte[] bytes, int off, int len);

    long get();

}
