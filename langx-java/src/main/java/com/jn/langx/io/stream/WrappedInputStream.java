package com.jn.langx.io.stream;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Unsigneds;
import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.io.IOs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @since 4.4.2
 */
public class WrappedInputStream extends FilterInputStream {
    private IOStreamPipeline pipeline;

    /**
     * @since 5.0.2
     */
    public WrappedInputStream(InputStream in, IOStreamPipeline pipeline) {
        super(in);
        this.pipeline = pipeline;
    }

    /**
     * @since 4.4.2
     */
    public WrappedInputStream(InputStream in, List<Consumer4<InputStream, byte[], Integer, Integer>> consumers) {
        this(in, IOStreamPipeline.ofInputStreamConsumers(consumers));
    }


    @Override
    public int read() throws IOException {
        if (Objs.isNotNull(this.pipeline)) {
            this.pipeline.beforeRead(this, Emptys.EMPTY_BYTES, 0, 1);
        }
        int b = super.read();

        if (Objs.isNotNull(this.pipeline) && b != -1) {
            final byte[] bs = new byte[]{Unsigneds.toSignedByte(b)};
            this.pipeline.afterRead(this, bs, 0, 1);
        }
        return b;
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int l = Maths.min(len, IOs.DEFAULT_BUFFER_SIZE);
        if (Objs.isNotNull(this.pipeline)) {
            this.pipeline.beforeRead(this, b, off, l);
        }
        int length = super.read(b, off, l);

        if (Objs.isNotNull(this.pipeline) && length > 0) {
            this.pipeline.afterRead(this, b, off, length);
        }
        return length;
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
