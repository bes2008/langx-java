package com.jn.langx.util.hash;


import com.jn.langx.Factory;
import com.jn.langx.Named;

/**
 * 使用方法：
 * <pre>
 * 1） hasher.setSeed()
 * 2）多次调用 hasher.update(byte[]) 动态更新数据
 * 3） 调用 getHash() 获取计算后的Hash值
 * </pre>
 */
public interface Hasher extends Factory<Long,Hasher>, Named {

    void setSeed(long seed);

    /**
     * 用于流式计算
     */
    void update(byte[] bytes, int off, int len);

    long getHash();

    @Override
    Hasher get(Long seed);

    @Override
    String getName();
}
