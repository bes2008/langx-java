package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WrappedInputStream extends FilterInputStream {
    private List<Consumer2<InputStream, byte[]>> consumers;

    public WrappedInputStream(InputStream in, List<Consumer2<InputStream, byte[]>> consumers) {
        super(in);
        this.consumers = consumers;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();

        if (Objs.isEmpty(this.consumers) && b != -1) {
            final byte[] bs = new byte[]{(byte) b};
            consume(bs);
        }
        return b;
    }


    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int length = super.read(b, off, len);
        if (Objs.isEmpty(this.consumers) && length > 0) {
            final byte[] bs = new byte[length];
            System.arraycopy(b, off, bs, 0, length);
            consume(bs);
        }
        return length;
    }

    private void consume(final byte[] bytes) {
        Collects.forEach(this.consumers, new Consumer<Consumer2<InputStream, byte[]>>() {
            @Override
            public void accept(Consumer2<InputStream, byte[]> consumer) {
                consumer.accept(WrappedInputStream.this, bytes);
            }
        });
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }
}
