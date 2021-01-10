package com.jn.langx.util.collection.buffer;

import com.jn.langx.annotation.Nullable;

import java.util.List;

public abstract class ReadWriteBuffer<E, BF extends ReadWriteBuffer> extends Buffer<BF> {

    public ReadWriteBuffer(long mark, long pos, long lim, long cap) {
        super(mark, pos, lim, cap);
    }

    /**
     * @param e
     * @return
     * @since 3.3.1
     */
    public abstract BF put(@Nullable E e);

    /**
     * @param index
     * @param e
     * @return
     * @since 3.3.1
     */
    public abstract BF put(long index, @Nullable E e);

    /**
     * @return
     * @since 3.3.1
     */
    public abstract E get();

    /**
     * @param index     起始位置
     * @param maxLength 最多获取数量，如果小于0 ，则从指定位置到limit
     * @return
     * @since 3.3.1
     */
    public abstract List<E> get(long index, long maxLength);
}
