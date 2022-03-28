package com.jn.langx.io.stream;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @since 4.4.2
 */
public class WrappedOutputStream extends FilterOutputStream {
    private List<Consumer2<OutputStream, byte[]>> consumers;

    public WrappedOutputStream(OutputStream out, List<Consumer2<OutputStream, byte[]>> consumers) {
        super(out);
        this.consumers = consumers;
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[]{(byte) b};
        write(bytes, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len > 0) {
            out.write(b, off, len);
            if(Objs.isNotEmpty(this.consumers)) {
                final byte[] bytes = new byte[len];
                System.arraycopy(b, off, bytes, 0, len);
                Collects.forEach(this.consumers, new Consumer<Consumer2<OutputStream, byte[]>>() {
                    @Override
                    public void accept(Consumer2<OutputStream, byte[]> consumer) {
                        consumer.accept(WrappedOutputStream.this, bytes);
                    }
                });
            }
        }
    }
}
