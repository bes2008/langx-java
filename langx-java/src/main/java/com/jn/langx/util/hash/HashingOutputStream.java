package com.jn.langx.util.hash;


import com.jn.langx.io.stream.WrappedOutputStream;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;

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
    public HashingOutputStream(final StreamingHasher hasher,  OutputStream out) {
        super(out, Collects.<Consumer2<OutputStream, byte[]>>asList(new Consumer2<OutputStream, byte[]>() {
            @Override
            public void accept(OutputStream outputStream, byte[] bytes) {
                hasher.update(bytes, 0, bytes.length);
            }
        }));
    }
}
