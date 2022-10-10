package com.jn.langx.util.hash;


import com.jn.langx.io.stream.IOStreamPipeline;
import com.jn.langx.io.stream.InputStreamInterceptor;
import com.jn.langx.io.stream.WrappedInputStream;

import java.io.InputStream;

/**
 * @since 4.4.2
 */
public final class HashingInputStream extends WrappedInputStream {

    /**
     * Creates an input stream that hashes using the given {@link StreamingHasher} and delegates all data
     * read from it to the underlying {@link InputStream}.
     *
     * <p>The {@link InputStream} should not be read from before or after the hand-off.
     */
    public HashingInputStream(final StreamingHasher hasher, InputStream in) {
        super(in, IOStreamPipeline.of(new InputStreamInterceptor() {
            @Override
            public boolean beforeRead(InputStream inputStream, byte[] b, int off, int len) {
                return true;
            }

            @Override
            public boolean afterRead(InputStream inputStream, byte[] b, int off, int len) {
                hasher.update(b, off, len);
                return true;
            }
        }));
    }

}
