package com.jn.langx.io.buffer;

import com.jn.langx.Builder;

public class BigByteBufferBuilder implements Builder<BigByteBuffer> {
    private boolean readonly = false;
    private boolean direct = false;
    private int segmentSize = BigByteBuffer.MIN_SEGMENT_SIZE;
    private long capacity = Long.MAX_VALUE;

    public BigByteBufferBuilder readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public BigByteBufferBuilder direct(boolean direct) {
        this.direct = direct;
        return this;
    }

    public BigByteBufferBuilder segmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
        return this;
    }

    public BigByteBufferBuilder capacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    public BigByteBuffer build() {
        BigByteBuffer bigByteBuffer = new BigByteBuffer(capacity, direct, segmentSize);
        bigByteBuffer.setReadonly(readonly);
        return bigByteBuffer;
    }
}
