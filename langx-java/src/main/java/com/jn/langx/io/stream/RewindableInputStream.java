package com.jn.langx.io.stream;

import com.jn.langx.io.Rewindable;
import com.jn.langx.io.buffer.BigByteBuffer;
import com.jn.langx.io.buffer.BigByteBufferBuilder;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RewindableInputStream extends FilterInputStream implements Rewindable<Void> {
    private BigByteBufferBuilder bufferBuilder;
    private BigByteBuffer buffer;
    private volatile boolean closed = false;

    public RewindableInputStream(InputStream in) {
        super(in);
        bufferBuilder = new BigByteBufferBuilder();
    }

    public RewindableInputStream(InputStream in, boolean direct, int segmentSize) {
        super(in);
        bufferBuilder = new BigByteBufferBuilder().segmentSize(segmentSize).direct(direct);
    }

    public RewindableInputStream(InputStream in, boolean direct, long capacity, int segmentSize) {
        super(in);
        bufferBuilder = new BigByteBufferBuilder().capacity(capacity).segmentSize(segmentSize).direct(direct);
    }

    @Override
    public int read() throws IOException {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            doRead();
        }
        if (this.buffer.hasRemaining()) {
            return this.buffer.get();
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            doRead();
        }

        int count = 0;
        while (off < b.length && count < len) {
            if (this.buffer.hasRemaining()) {
                b[off] = this.buffer.get();
            } else {
                return -1;
            }
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            doRead();
        }
        Preconditions.checkArgument(n >= 0);
        n = Maths.minLong(this.buffer.remaining(), n);
        if (n > 0) {
            this.buffer.position(this.buffer.position() + n);
        }
        return n;
    }

    @Override
    public int available() throws IOException {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            doRead();
        }
        return (int) this.buffer.remaining();
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            this.closed = true;
            this.buffer = null;
            super.close();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            try {
                doRead();
            } catch (IOException ex) {
                // ignore it
            }
        }
        this.buffer.mark();
    }

    @Override
    public synchronized void reset() throws IOException {
        if (closed) {
            throw new IllegalStateException("the stream is closed");
        }
        if (this.buffer == null) {
            doRead();
        }
        this.buffer.reset();
    }

    private void doRead() throws IOException {
        BigByteBuffer bf = bufferBuilder.build();

        while (true) {
            int bt = in.read();
            if (bt != -1) {
                bf.put((byte) bt);
            } else {
                break;
            }
        }

        this.buffer = bf;
        this.buffer.flip();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public Void rewind() {
        this.buffer.rewind();
        return null;
    }


}
