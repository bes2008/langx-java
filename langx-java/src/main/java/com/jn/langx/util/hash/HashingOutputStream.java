package com.jn.langx.util.hash;


import com.jn.langx.io.stream.IOStreamPipeline;
import com.jn.langx.io.stream.OutputStreamInterceptor;
import com.jn.langx.io.stream.WrappedOutputStream;

import java.io.OutputStream;


/**
 * An {@link OutputStream} that maintains a hash of the data written to it.
 *
 * @since 4.4.2
 */
public final class HashingOutputStream extends WrappedOutputStream {

    /**
     * Creates an output stream that hashes using the given {@link StreamingHasher}, and forwards all
     * data written to it to the underlying {@link OutputStream}.
     *
     * <p>The {@link OutputStream} should not be written to before or after the hand-off.
     */
    public HashingOutputStream(final StreamingHasher hasher, OutputStream out) {
        super(out, IOStreamPipeline.of(new OutputStreamInterceptor() {
                    @Override
                    public boolean beforeWrite(OutputStream outputStream, byte[] b, int off, int len) {
                        return true;
                    }

                    @Override
                    public boolean afterWrite(OutputStream outputStream, byte[] b, int off, int len) {
                        hasher.update(b, 0, len);
                        return true;
                    }
                }));
    }
}
