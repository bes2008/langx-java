package com.jn.langx.io.stream;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.progress.ProgressSource;

import java.io.OutputStream;

/**
 * @since 4.4.2
 */
public class ProgressTracedOutputStream extends WrappedOutputStream {
    public ProgressTracedOutputStream(OutputStream in, final ProgressSource progressSource) {
        super(in, Collects.<Consumer4<OutputStream, byte[], Integer, Integer>>asList(new Consumer4<OutputStream, byte[], Integer, Integer>() {
            @Override
            public void accept(OutputStream in, byte[] bytes, Integer off, Integer len) {
                if (!progressSource.started()) {
                    progressSource.start();
                }
                progressSource.forward(len);
            }
        }));
    }
}