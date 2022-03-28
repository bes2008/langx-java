package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer4;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @since 4.4.2
 */
public class WrappedInputStream extends FilterInputStream {
    private List<Consumer4<InputStream, byte[], Integer, Integer>> consumers;

    public WrappedInputStream(InputStream in, List<Consumer4<InputStream, byte[], Integer, Integer>> consumers) {
        super(in);
        this.consumers = consumers;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();

        if (Objs.isNotEmpty(this.consumers) && b != -1) {
            final byte[] bs = new byte[]{(byte) b};
            consume(bs, 0, 1);
        }
        return b;
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int length = super.read(b, off, len);
        if (Objs.isNotEmpty(this.consumers) && length > 0) {
            consume(b, off, len);
        }
        return length;
    }

    private void consume(final byte[] b, final int off, final int len) {
        Collects.forEach(this.consumers, new Consumer<Consumer4<InputStream, byte[], Integer, Integer>>() {
            @Override
            public void accept(Consumer4<InputStream, byte[], Integer, Integer> consumer) {
                consumer.accept(WrappedInputStream.this, b, off, len);
            }
        });
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void mark(int readlimit) {

    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }
}
