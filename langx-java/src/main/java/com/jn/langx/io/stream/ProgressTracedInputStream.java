package com.jn.langx.io.stream;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer4;
import com.jn.langx.util.progress.ProgressSource;

import java.io.InputStream;

/**
 * @since 4.4.2
 */
public class ProgressTracedInputStream extends WrappedInputStream {
    public ProgressTracedInputStream(InputStream in, final ProgressSource progressSource) {
        super(in, Collects.<Consumer4<InputStream, byte[],Integer, Integer>>asList(new Consumer4<InputStream, byte[],Integer, Integer>() {
            Boolean lengthGot = null;

            @Override
            public void accept(InputStream in, byte[] bytes, Integer off, Integer len) {
                if (!progressSource.started()) {
                    progressSource.start();
                }
                progressSource.forward(len);
                if (progressSource.getExpected() < 0 && lengthGot == null) {
                    lengthGot = true;
                    try {
                        long expected = progressSource.getProgress() + in.available();
                        progressSource.update(-1L, expected);
                    } catch (Exception ex) {
                        // ignore
                    }
                }
            }
        }));
    }
}
