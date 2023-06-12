package com.jn.langx.util.random;

import com.jn.langx.util.function.Supplier;

public interface BytesRandom extends Supplier<Integer, byte[]> {
    /**
     * 生成一个随机的bytes数组填充到 dest中
     */
    void get(byte[] dest);

    /**
     * 基于指定的 size 生成一个 byte[] ，生成的byte[] 的length 不一定等于 size
     */
    @Override
    byte[] get(Integer size);
}