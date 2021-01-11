package com.jn.langx.util.collection.buffer;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class ArrayBuffer<E> extends ReadWriteBuffer<E, ArrayBuffer<E>> {
    private E[] array;

    // 起点偏移量
    private int offset = 0;

    public ArrayBuffer(int maxCapacity) {
        super(-1, 0, 0, maxCapacity);
        this.array = (E[]) new Object[maxCapacity];
    }

    public ArrayBuffer(E[] buf) {
        this(-1, 0, buf.length, buf.length, buf, 0);
    }

    public ArrayBuffer(E[] buf, int offset) {
        this(-1, offset, buf.length, buf.length, buf, 0);
    }

    /**
     * 把指定的数组 包装为 ArrayBuffer，从 offset开始，包装长度为 length
     *
     * @param buf
     * @return
     */
    public ArrayBuffer(E[] buf, int offset, int length) {
        this(-1, offset, offset + length, buf.length, buf, 0);
    }

    public ArrayBuffer(int mark, int pos, int lim, int cap, E[] buf, int offset) {
        super(mark, pos, lim, cap);
        this.array = buf;
        this.offset = offset;
    }


    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean hasArray() {
        return true;
    }

    @Override
    public Object array() {
        return array;
    }

    @Override
    public long arrayOffset() {
        return offset;
    }

    @Override
    public ArrayBuffer<E> put(@Nullable E e) {
        array[(int) idx(nextPutIndex())] = e;
        return this;
    }

    @Override
    public ArrayBuffer<E> put(long index, @Nullable E e) {
        array[(int) idx(checkIndex(index))] = e;
        return this;
    }

    private long idx(long position) {
        return offset + position;
    }

    @Override
    public E get() {
        return array[(int) idx(nextGetIndex())];
    }

    public E get(int index) {
        return array[(int) idx(checkIndex(index))];
    }

    @Override
    public List<E> get(long index, long maxLength) {
        Preconditions.checkArgument(maxLength >= 0);
        final List<E> list = Collects.emptyArrayList();
        long len = Maths.minLong(limit() - checkIndex(index), maxLength);
        for (; len >= 0; len--) {
            list.add(get());
        }
        return list;
    }
}
