package com.jn.langx.util.collection.buffer;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.List;

public class ArrayBuffer<E> extends Buffer<E, ArrayBuffer<E>> {
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
    public int arrayOffset() {
        return offset;
    }

    @Override
    public ArrayBuffer<E> put(@Nullable E e) {
        array[idx(nextPutIndex())] = e;
        return this;
    }

    @Override
    public ArrayBuffer<E> put(int index, @Nullable E e) {
        array[idx(checkIndex(index))] = e;
        return this;
    }

    private int idx(int position) {
        return offset + position;
    }

    @Override
    public E get() {
        return array[idx(nextGetIndex())];
    }

    public E get(int index) {
        return array[idx(checkIndex(index))];
    }

    @Override
    public List<E> get(int index, int maxlength) {
        final List<E> list = Collects.emptyArrayList();
        if (maxlength < 0) {
            maxlength = remaining();
        }
        if (maxlength == 0 || remaining() == 0) {
            return list;
        }
        int len = Maths.min(remaining(), maxlength);

        Collects.forEach(Arrs.range(len), new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                list.add(get());
            }
        });
        return list;
    }
}
