package com.jn.langx.io;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.buffer.Buffer;

import java.nio.ByteBuffer;
import java.util.List;

public class BigByteBuffer extends Buffer<BigByteBuffer> {
    private final List<ByteBuffer> buffers = Collects.emptyArrayList();
    private boolean readonly = false;

    public BigByteBuffer(long mark, long pos, long lim, long cap) {
        super(mark, pos, lim, cap);
    }

    @Override
    public boolean isReadOnly() {
        return readonly;
    }

    @Override
    public boolean hasArray() {
        return false;
    }

    @Override
    public Object array() {
        return null;
    }

    @Override
    public long arrayOffset() {
        return 0L;
    }

    public BigByteBuffer put(byte b) {
        return null;
    }

    public BigByteBuffer put(long index, byte b) {
        return null;
    }

    public byte get() {
        return 0;
    }

    public ByteBuffer get(long index, long maxLength) {
        return null;
    }
}
