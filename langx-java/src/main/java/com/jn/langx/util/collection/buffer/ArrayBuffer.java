package com.jn.langx.util.collection.buffer;

public class ArrayBuffer extends Buffer {
    private Object[] array;

    public ArrayBuffer(int maxCapacity) {
        super(-1, 0, 0, maxCapacity);
        this.array = new Object[maxCapacity];
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
        return position();
    }
}
