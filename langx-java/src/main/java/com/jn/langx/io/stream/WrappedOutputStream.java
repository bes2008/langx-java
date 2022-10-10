package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.function.Function;

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
     * @param out
     * @param consumers
     *
     * @since 4.4.2
     */
    public WrappedOutputStream(OutputStream out, List<Consumer4<OutputStream, byte[], Integer, Integer>> consumers) {
       this(out, IOStreamPipeline.of(Pipeline.of(consumers).map(new Function<Consumer4<OutputStream,byte[], Integer, Integer>, OutputStreamInterceptor>() {
           @Override
           public OutputStreamInterceptor apply(final Consumer4<OutputStream, byte[], Integer, Integer> consumer) {
               return new OutputStreamInterceptor() {
                   @Override
                   public boolean beforeWrite(OutputStream outputStream, byte[] b, int off, int len) {
                       return true;
                   }

                   @Override
                   public boolean afterWrite(OutputStream outputStream, byte[] b, int off, int len) {
                       consumer.accept(outputStream, b, off, len);
                       return true;
                   }
               };
           }
       }).asList()));
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
