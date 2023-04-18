package com.jn.langx.io.stream;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.io.InputStream;

public class UnsyncByteArrayInputStream extends InputStream {
    protected byte[] myBuffer;

    private int myPosition;

    private int myCount;

    private int myMarkedPosition;

    public UnsyncByteArrayInputStream(@NonNull byte[] buf) {
        this(buf, 0, buf.length);
    }

    public UnsyncByteArrayInputStream(@NonNull byte[] buf, int offset, int length) {
        init(buf, offset, length);
    }

    public void init(@NonNull byte[] buf, int offset, int length) {
        Preconditions.checkNotNull(buf);
        this.myBuffer = buf;
        this.myPosition = offset;
        this.myCount = length;
    }

    public int read() {
        return (this.myPosition < this.myCount) ? (this.myBuffer[this.myPosition++] & 0xFF) : -1;
    }

    @Override
    public int read(@NonNull byte[] b, int off, int len) {
        Preconditions.checkNotEmpty(b);
        if (off < 0 || len < 0 || len > b.length - off)
            throw new IndexOutOfBoundsException();
        if (this.myPosition >= this.myCount)
            return -1;
        if (this.myPosition + len > this.myCount)
            len = this.myCount - this.myPosition;
        if (len <= 0)
            return 0;
        System.arraycopy(this.myBuffer, this.myPosition, b, off, len);
        this.myPosition += len;
        return len;
    }
    @Override
    public long skip(long n) {
        if (this.myPosition + n > this.myCount)
            n = (this.myCount - this.myPosition);
        if (n < 0L)
            return 0L;
        this.myPosition = (int)(this.myPosition + n);
        return n;
    }
    @Override
    public int available() {
        return this.myCount - this.myPosition;
    }
    @Override
    public boolean markSupported() {
        return true;
    }
    @Override
    public synchronized void mark(int readLimit) {
        this.myMarkedPosition = this.myPosition;
    }
    @Override
    public synchronized void reset() {
        this.myPosition = this.myMarkedPosition;
    }
}
