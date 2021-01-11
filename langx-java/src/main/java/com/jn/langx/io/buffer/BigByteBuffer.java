package com.jn.langx.io.buffer;

import com.jn.langx.util.DataSize;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.buffer.Buffer;
import com.jn.langx.util.function.Consumer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.List;

public class BigByteBuffer extends Buffer<BigByteBuffer> {
    private final List<ByteBuffer> segments = Collects.emptyArrayList();
    public static final int MIN_SEGMENT_SIZE = DataSize.kb(4).toInt();
    private boolean readonly = false;
    private final int segmentSize;
    private final boolean direct;

    public BigByteBuffer(byte[] bytes, long cap, int segmentSize) {
        this(cap, false, segmentSize);
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                put(bytes[i]);
            }
        }
    }

    public BigByteBuffer(long cap) {
        this(cap, false, MIN_SEGMENT_SIZE);
    }

    public BigByteBuffer(long cap, int segmentSize) {
        this(cap, false, segmentSize);
    }

    public BigByteBuffer(long cap, boolean direct, int segmentSize) {
        this(-1L, 0, cap, cap, direct, segmentSize);
    }

    public BigByteBuffer(long mark, long pos, long lim, long cap, boolean direct, int segmentSize) {
        super(mark, pos, lim, cap);
        Preconditions.checkArgument(segmentSize >= MIN_SEGMENT_SIZE);
        this.direct = direct;
        this.segmentSize = segmentSize;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public BigByteBuffer clear() {
        super.clear();
        Collects.forEach(segments, new Consumer<ByteBuffer>() {
            @Override
            public void accept(ByteBuffer buffer) {
                buffer.clear();
            }
        });
        return this;
    }

    @Override
    public BigByteBuffer flip() {
        super.flip();
        Collects.forEach(segments, new Consumer<ByteBuffer>() {
            @Override
            public void accept(ByteBuffer buffer) {
                buffer.flip();
            }
        });
        return this;
    }

    @Override
    public BigByteBuffer rewind() {
        super.rewind();
        Collects.forEach(segments, new Consumer<ByteBuffer>() {
            @Override
            public void accept(ByteBuffer buffer) {
                buffer.rewind();
            }
        });
        return this;
    }

    private ByteBuffer newBuffer() {
        return direct ? ByteBuffer.allocateDirect(segmentSize) : ByteBuffer.allocate(segmentSize);
    }

    private int segmentIndex(long index) {
        return (int) (index / segmentSize);
    }

    private ByteBuffer getSegmentForPut() {
        if (position() >= limit()) {
            throw new BufferOverflowException();
        }
        int segmentIndex = segmentIndex(nextPutIndex());
        ByteBuffer segment;
        if (segmentIndex > segments.size() - 1) {
            segment = newBuffer();
            segments.add(segment);
        } else {
            segment = segments.get(segmentIndex);
        }
        return segment;
    }

    private ByteBuffer getSegmentForGet() {
        if (position() >= limit()) {
            throw new BufferUnderflowException();
        }
        return segments.get(segmentIndex(nextGetIndex()));
    }

    @Override
    public boolean isReadOnly() {
        return readonly;
    }

    @Override
    public final boolean hasArray() {
        return false;
    }

    @Override
    public final Object array() {
        return null;
    }

    @Override
    public final long arrayOffset() {
        return 0L;
    }


    public BigByteBuffer put(byte b) {
        Preconditions.checkState(!readonly, "the byte buffer is readonly");
        getSegmentForPut().put(b);
        return this;
    }

    public BigByteBuffer put(long index, byte b) {
        Preconditions.checkState(!readonly, "the byte buffer is readonly");
        ByteBuffer segment = segments.get(segmentIndex(checkIndex(index)));
        segment.put((int) (index % segmentSize), b);
        return this;
    }

    public byte get() {
        return getSegmentForGet().get();
    }

    public byte get(long index) {
        ByteBuffer segment = segments.get(segmentIndex(checkIndex(index)));
        return segment.get((int) (index % segmentSize));
    }

    public ByteBuffer get(long index, int maxLength) {
        Preconditions.checkArgument(maxLength >= 0);
        if (maxLength > limit() - index) {
            maxLength = (int) (limit() - index);
        }
        ByteBuffer ret = ByteBuffer.wrap(new byte[maxLength]);
        for (int l = maxLength; l > 0; l--) {
            ret.put(get(index++));
        }
        return ret;
    }
}
