package com.jn.langx.util.hash;


import com.jn.langx.io.stream.WrappedInputStream;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;

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
        super(in, Collects.<Consumer2<InputStream, byte[]>>asList(new Consumer2<InputStream, byte[]>() {
            @Override
            public void accept(InputStream key, byte[] bytes) {
                if (hasher != null) {
                    hasher.update(bytes, 0, bytes.length);
                }
            }
        }));
    }

}
