package com.jn.langx.util.hash;


import com.jn.langx.util.Preconditions;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


public final class HashingInputStream extends FilterInputStream {
    private final StreamingHasher hasher;

    /**
     * Creates an input stream that hashes using the given {@link StreamingHasher} and delegates all data
     * read from it to the underlying {@link InputStream}.
     *
     * <p>The {@link InputStream} should not be read from before or after the hand-off.
     */
    public HashingInputStream(StreamingHasher hasher, InputStream in) {
        super(Preconditions.checkNotNull(in));
        this.hasher = Preconditions.checkNotNull(hasher);
    }

    /**
     * Reads the next byte of data from the underlying input stream and updates the hasher with the
     * byte read.
     */
    @Override
    public int read() throws IOException {
        int b = in.read();
        if (b != -1) {
            byte[] bytes = new byte[]{(byte) b};
            hasher.update(bytes,0,1);
        }
        return b;
    }

    /**
     * Reads the specified bytes of data from the underlying input stream and updates the hasher with
     * the bytes read.
     */
    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        int numOfBytesRead = in.read(bytes, off, len);
        if (numOfBytesRead != -1) {
            hasher.update(bytes, off, numOfBytesRead);
        }
        return numOfBytesRead;
    }

    /**
     * mark() is not supported for HashingInputStream
     *
     * @return {@code false} always
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * mark() is not supported for HashingInputStream
     */
    @Override
    public void mark(int readlimit) {}

    /**
     * reset() is not supported for HashingInputStream.
     *
     * @throws IOException this operation is not supported
     */
    @Override
    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }

    /**
     * Returns the {@literal hash code} based on the data read from this stream. The result is unspecified
     * if this method is called more than once on the same instance.
     */
    public long getHash() {
        return hasher.getHash();
    }
}
