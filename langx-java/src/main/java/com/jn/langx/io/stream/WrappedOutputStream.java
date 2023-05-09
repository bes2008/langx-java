package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Consumer4;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @since 4.4.2
 */
public class WrappedOutputStream extends FilterOutputStream {
    private IOStreamPipeline pipeline;

    /**
     * @since 5.0.2
     */
    public WrappedOutputStream(OutputStream out, IOStreamPipeline pipeline) {
        super(out);
        this.pipeline = pipeline;
    }

    /**
     *
     * @since 4.4.2
     */
    public WrappedOutputStream(OutputStream out, List<Consumer4<OutputStream, byte[], Integer, Integer>> consumers) {
       this(out, IOStreamPipeline.ofOutputStreamConsumers(consumers));
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[]{(byte) b};
        write(bytes, 0, 1);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len > 0) {
            if (Objs.isNotNull(this.pipeline)) {
               this.pipeline.beforeWrite(this, b, off, len);
            }
            out.write(b, off, len);
            if (Objs.isNotNull(this.pipeline)) {
                this.pipeline.afterWrite(this, b, off, len);
            }
        }
    }
}
