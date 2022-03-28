package com.jn.langx.io.stream;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.progress.ProgressSource;

import java.io.OutputStream;

/**
 * @since 4.4.2
 */
public class ProgressTracedOutputStream extends WrappedOutputStream {
    public ProgressTracedOutputStream(OutputStream in, final ProgressSource progressSource) {
        super(in, Collects.<Consumer2<OutputStream, byte[]>>asList(new Consumer2<OutputStream, byte[]>() {
            @Override
            public void accept(OutputStream in, byte[] bytes) {
                if (!progressSource.started()) {
                    progressSource.start();
                }
                progressSource.forward(bytes.length);
            }
        }));
    }
}