package com.jn.langx.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.io.Charsets;

public class UnsyncByteArrayOutputStream extends OutputStream {
    protected byte[] myBuffer;

    protected int myCount;

    private boolean myIsShared;

    @NonNull
    private final ByteArrayAllocator myAllocator;

    public UnsyncByteArrayOutputStream() {
        this(32);
    }

    public UnsyncByteArrayOutputStream(int size) {
        this(Arrs.createByteArray(size));
    }

    public UnsyncByteArrayOutputStream(byte[] buffer) {
        this.myAllocator = new ByteArrayAllocator(){
            @Override
            public byte[] allocate(int size) {
                return Arrs.createByteArray(size);
            }
        };
        this.myBuffer = buffer;
    }

    public UnsyncByteArrayOutputStream(@NonNull ByteArrayAllocator allocator, int initialSize) {
        this.myAllocator = allocator;
        this.myBuffer = allocator.allocate(initialSize);
    }

    public void write(int b) {
        int newCount = this.myCount + 1;
        if (newCount > this.myBuffer.length || this.myIsShared) {
            grow(newCount);
            this.myIsShared = false;
        }
        this.myBuffer[this.myCount] = (byte)b;
        this.myCount = newCount;
    }

    private void grow(int newCount) {
        int newLength = (newCount > this.myBuffer.length) ? Math.max(this.myBuffer.length << 1, newCount) : this.myBuffer.length;
        byte[] newBuffer = this.myAllocator.allocate(newLength);
        System.arraycopy(this.myBuffer, 0, newBuffer, 0, this.myBuffer.length);
        this.myBuffer = newBuffer;
    }

    @Override
    public void write(byte[] b, int off, int len) {
        Preconditions.checkNotNull(b);
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0)
            throw new IndexOutOfBoundsException();
        if (len == 0)
            return;
        int newCount = this.myCount + len;
        if (newCount > this.myBuffer.length || this.myIsShared) {
            grow(newCount);
            this.myIsShared = false;
        }
        System.arraycopy(b, off, this.myBuffer, this.myCount, len);
        this.myCount = newCount;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.myBuffer, 0, this.myCount);
    }

    public void reset() {
        this.myCount = 0;
    }

    public byte[] toByteArray() {
        if (this.myBuffer.length == this.myCount) {
            this.myIsShared = true;
            return this.myBuffer;
        }
        return Arrays.copyOf(this.myBuffer, this.myCount);
    }

    public int size() {
        return this.myCount;
    }

    public String toString() {
        return new String(this.myBuffer, 0, this.myCount, Charsets.UTF_8);
    }

    @NonNull
    public InputStream toInputStream() {
        return new UnsyncByteArrayInputStream(this.myBuffer, 0, this.myCount);
    }

    public interface ByteArrayAllocator {
        byte[] allocate(int param1Int);
    }
}