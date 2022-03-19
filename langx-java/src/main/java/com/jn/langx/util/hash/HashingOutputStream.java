package com.jn.langx.util.hash;


import com.jn.langx.util.Preconditions;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * An {@link OutputStream} that maintains a hash of the data written to it.
 */
public final class HashingOutputStream extends FilterOutputStream {
    private final StreamingHasher hasher;

    /**
     * Creates an output stream that hashes using the given {@link StreamingHasher}, and forwards all
     * data written to it to the underlying {@link OutputStream}.
     *
     * <p>The {@link OutputStream} should not be written to before or after the hand-off.
     */
    public HashingOutputStream(StreamingHasher hasher, OutputStream out) {
        this(hasher,null,out);
    }
    public HashingOutputStream(StreamingHasher hasher, Integer seed, OutputStream out) {
        super(Preconditions.checkNotNull(out));
        this.hasher =Preconditions. checkNotNull(hasher);
        if (seed == null) {
            seed = 0;
        }
        this.hasher.setSeed(seed);
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[]{(byte) b};
        write(bytes, 0, 1);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        out.write(bytes, off, len);
        hasher.update(bytes, off, len);
    }

    /**
     * Returns the {@literal hash code} based on the data written to this stream. The result is
     * unspecified if this method is called more than once on the same instance.
     */
    public long getHash() {
        return hasher.getHash();
    }

    // Overriding close() because FilterOutputStream's close() method pre-JDK8 has bad behavior:
    // it silently ignores any exception thrown by flush(). Instead, just close the delegate stream.
    // It should flush itself if necessary.
    @Override
    public void close() throws IOException {
        out.close();
    }
}
