package com.jn.langx.io.stream;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public final class BufferExposingByteArrayOutputStream extends UnsyncByteArrayOutputStream {
    public BufferExposingByteArrayOutputStream() {
    }

    public BufferExposingByteArrayOutputStream(int size) {
        super(size);
    }

    public BufferExposingByteArrayOutputStream(byte[] buffer) {
        super(buffer);
    }

    public BufferExposingByteArrayOutputStream(@NonNull UnsyncByteArrayOutputStream.ByteArrayAllocator allocator, int initialSize) {
        super(allocator, initialSize);
    }

    public byte[] getInternalBuffer() {
        Preconditions.checkNotNull(this.myBuffer);
        return this.myBuffer;
    }

    public int backOff(int size) {
        Preconditions.checkArgument(size >= 0);
        this.myCount -= size;
        assert this.myCount >= 0 : this.myCount;
        return this.myCount;
    }
}