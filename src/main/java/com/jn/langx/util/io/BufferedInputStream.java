package com.jn.langx.util.io;

import com.jn.langx.util.Preconditions;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BufferedInputStream extends FilterInputStream {

    private static int defaultBufferSize = 8192;

    /**
     * The internal buffer array where the data is stored. When necessary,
     * it may be replaced by another array of
     * a different size.
     */
    private byte[] realBuf;
    private ByteBuffer buf;


    /**
     * Check to make sure that underlying input stream has not been
     * nulled out due to close; if not return it;
     */
    private InputStream getInIfOpen() throws IOException {
        InputStream input = in;
        if (input == null) {
            throw new IOException("Stream closed");
        }
        return input;
    }

    public BufferedInputStream(InputStream in) {
        this(in, defaultBufferSize);
    }

    /**
     * Creates a <code>BufferedInputStream</code>
     * with the specified buffer size,
     * and saves its  argument, the input stream
     * <code>in</code>, for later use.  An internal
     * buffer array of length  <code>size</code>
     * is created and stored in <code>buf</code>.
     *
     * @param in   the underlying input stream.
     * @param size the buffer size.
     * @throws IllegalArgumentException if size <= 0.
     */
    public BufferedInputStream(InputStream in, int size) {
        super(in);
        Preconditions.checkTrue(size > 0, "Buffer size <= 0");
        this.realBuf = new byte[size];
    }

    /**
     * Fills the buffer with more data, taking into account
     * shuffling and other tricks for dealing with marks.
     * Assumes that it is being called by a synchronized method.
     * This method also assumes that all data has already been read in,
     * hence pos > count.
     */
    private void fill() throws IOException {
        fill(realBuf.length);
    }

    private void fill(int maxSize) throws IOException {
        Preconditions.checkTrue(maxSize > 0);
        if (buf != null) {
            buf.clear();
        }
        if (maxSize > realBuf.length) {
            realBuf = new byte[maxSize];
        }
        int length = in.read(realBuf, 0, maxSize);
        if (length != -1) {
            buf = ByteBuffer.wrap(realBuf, 0, length);
        } else {
            buf.clear();
            buf.limit(0);
        }
    }


    /**
     * See
     * the general contract of the <code>read</code>
     * method of <code>InputStream</code>.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
     * @see java.io.FilterInputStream#in
     */
    public synchronized int read() throws IOException {
        return readByte();
    }

    private synchronized byte readByte() throws IOException {
        if (buf == null) {
            if (in.available() > 0) {
                fill();
            }
            if (buf == null) {
                return -1;
            }
        }
        if (!buf.hasRemaining()) {
            fill();
            if (!buf.hasRemaining()) {
                return -1;
            }
        }
        return buf.get();
    }

    /**
     * Read characters into a portion of an array, reading from the underlying
     * stream at most once if necessary.
     */
    private int read1(byte[] bytes, int off, int len) throws IOException {
        int i = 0;
        for (; i < len; ) {
            byte b = readByte();
            if (b != -1) {
                bytes[off] = b;
                off++;
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    /**
     * Reads bytes from this byte-input stream into the specified byte array,
     * starting at the given offset.
     * <p>
     * <p> This method implements the general contract of the corresponding
     * <code>{@link InputStream#read(byte[], int, int) read}</code> method of
     * the <code>{@link InputStream}</code> class.  As an additional
     * convenience, it attempts to read as many bytes as possible by repeatedly
     * invoking the <code>read</code> method of the underlying stream.  This
     * iterated <code>read</code> continues until one of the following
     * conditions becomes true: <ul>
     * <p>
     * <li> The specified number of bytes have been read,
     * <p>
     * <li> The <code>read</code> method of the underlying stream returns
     * <code>-1</code>, indicating end-of-file, or
     * <p>
     * <li> The <code>available</code> method of the underlying stream
     * returns zero, indicating that further input requests would block.
     * <p>
     * </ul> If the first <code>read</code> on the underlying stream returns
     * <code>-1</code> to indicate end-of-file then this method returns
     * <code>-1</code>.  Otherwise this method returns the number of bytes
     * actually read.
     * <p>
     * <p> Subclasses of this class are encouraged, but not required, to
     * attempt to read as many bytes as possible in the same fashion.
     *
     * @param b   destination buffer.
     * @param off offset at which to start storing bytes.
     * @param len maximum number of bytes to read.
     * @return the number of bytes read, or <code>-1</code> if the end of
     * the stream has been reached.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
     */
    public synchronized int read(byte b[], int off, int len)
            throws IOException {
        int length = read1(b, off, len);
        if (length == 0) {
            length = -1;
        }
        return length;
    }

    /**
     * See the general contract of the <code>skip</code>
     * method of <code>InputStream</code>.
     *
     * @throws IOException if the stream does not support seek,
     *                     or if this input stream has been closed by
     *                     invoking its {@link #close()} method, or an
     *                     I/O error occurs.
     */
    public synchronized long skip(long n) throws IOException {
        throw new IOException("Unsupported skip");
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking by the next
     * invocation of a method for this input stream. The next invocation might be
     * the same thread or another thread.  A single read or skip of this
     * many bytes will not block, but may read or skip fewer bytes.
     * <p>
     * This method returns the sum of the number of bytes remaining to be read in
     * the buffer (<code>count&nbsp;- pos</code>) and the result of calling the
     * {@link java.io.FilterInputStream#in in}.available().
     *
     * @return an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking.
     * @throws IOException if this input stream has been closed by
     *                     invoking its {@link #close()} method,
     *                     or an I/O error occurs.
     */
    public synchronized int available() throws IOException {
        return getInIfOpen().available() + (buf.limit() - buf.position());
    }

    /**
     * See the general contract of the <code>mark</code>
     * method of <code>InputStream</code>.
     *
     * @param readlimit the maximum limit of bytes that can be read before
     *                  the mark position becomes invalid.
     * @see java.io.BufferedInputStream#reset()
     */
    public synchronized void mark(int readlimit) {
        buf.mark();
    }

    /**
     * See the general contract of the <code>reset</code>
     * method of <code>InputStream</code>.
     * <p>
     * If <code>markpos</code> is <code>-1</code>
     * (no mark has been set or the mark has been
     * invalidated), an <code>IOException</code>
     * is thrown. Otherwise, <code>pos</code> is
     * set equal to <code>markpos</code>.
     *
     * @throws IOException if this stream has not been marked or,
     *                     if the mark has been invalidated, or the stream
     *                     has been closed by invoking its {@link #close()}
     *                     method, or an I/O error occurs.
     * @see java.io.BufferedInputStream#mark(int)
     */
    public synchronized void reset() throws IOException {
        buf.reset();
    }

    /**
     * Tests if this input stream supports the <code>mark</code>
     * and <code>reset</code> methods. The <code>markSupported</code>
     * method of <code>BufferedInputStream</code> returns
     * <code>true</code>.
     *
     * @return a <code>boolean</code> indicating if this stream type supports
     * the <code>mark</code> and <code>reset</code> methods.
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * Once the stream has been closed, further read(), available(), reset(),
     * or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        in.close();
    }

    public ByteBuffer readAsByteBuffer() throws IOException {
        return readAsByteBuffer(realBuf.length);
    }

    public ByteBuffer readAsByteBuffer(int maxSize) throws IOException {
        if (buf == null || !buf.hasRemaining()) {
            fill(maxSize);
            return buf;
        }
        return buf;
    }
}
