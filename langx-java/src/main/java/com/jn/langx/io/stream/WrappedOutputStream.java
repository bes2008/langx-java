package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer4;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @since 4.4.2
 */
public class WrappedOutputStream extends FilterOutputStream {
    private List<Consumer4<OutputStream, byte[], Integer, Integer>> consumers;

    public WrappedOutputStream(OutputStream out, List<Consumer4<OutputStream, byte[], Integer, Integer>> consumers) {
        super(out);
        this.consumers = consumers;
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[]{(byte) b};
        write(bytes, 0, 1);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len > 0) {
            out.write(b, off, len);
            if (Objs.isNotEmpty(this.consumers)) {
                Collects.forEach(this.consumers, new Consumer<Consumer4<OutputStream, byte[], Integer, Integer>>() {
                    @Override
                    public void accept(Consumer4<OutputStream, byte[], Integer, Integer> consumer) {
                        consumer.accept(WrappedOutputStream.this, b, off, len);
                    }
                });
            }
        }
    }
}
