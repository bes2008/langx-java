package com.jn.langx.io.stream;

import com.jn.langx.util.Maths;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.unicode.BOM;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeInputStream extends InputStream {
    private static final int BOM_MAX_SIZE = 4;

    protected volatile PushbackInputStream pushbackInputStream;
    private InputStream in;

    private BOM bom;

    public UnicodeInputStream(InputStream inputStream) throws IOException {
        this.pushbackInputStream = new PushbackInputStream(inputStream, BOM_MAX_SIZE);
        init();
    }

    public BOM getBom() {
        return bom;
    }

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are unread back to the stream, only
     * BOM bytes are skipped.
     *
     * @throws IOException if InputStream cannot be created
     */
    private void init() throws IOException {
        if (in != null) {
            return;
        }

        byte[] bomBytes = new byte[BOM_MAX_SIZE];
        int n, unread;
        n = this.pushbackInputStream.read(bomBytes, 0, bomBytes.length);
        if (n < bomBytes.length) {
            byte[] bs = new byte[n];
            System.arraycopy(bomBytes, 0, bs, 0, n);
            bomBytes = bs;
        }

        BOM bom = BOM.findBom(bomBytes);
        if (bom != null) {
            unread = n - bom.getBytes().length;
            this.bom = bom;
        } else {
            unread = n;
        }

        if (unread > 0) {
            this.pushbackInputStream.unread(bomBytes, (n - unread), unread);
        }
        this.in = this.pushbackInputStream;
    }


    /**
     * Reads the next byte of data from this input stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, the value
     * <code>-1</code> is returned. This method blocks until input data
     * is available, the end of the stream is detected, or an exception
     * is thrown.
     * <p>
     * This method
     * simply performs <code>in.read()</code> and returns the result.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if an I/O error occurs.
     * @see #in
     */
    public int read() throws IOException {
        return in.read();
    }

    /**
     * Reads up to <code>byte.length</code> bytes of data from this
     * input stream into an array of bytes. This method blocks until some
     * input is available.
     * <p>
     * This method simply performs the call
     * <code>read(b, 0, b.length)</code> and returns
     * the  result. It is important that it does
     * <i>not</i> do <code>in.read(b)</code> instead;
     * certain subclasses of  <code>FilterInputStream</code>
     * depend on the implementation strategy actually
     * used.
     *
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or
     * <code>-1</code> if there is no more data because the end of
     * the stream has been reached.
     * @throws IOException if an I/O error occurs.
     * @see java.io.FilterInputStream#read(byte[], int, int)
     */
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads up to <code>len</code> bytes of data from this input stream
     * into an array of bytes. If <code>len</code> is not zero, the method
     * blocks until some input is available; otherwise, no
     * bytes are read and <code>0</code> is returned.
     * <p>
     * This method simply performs <code>in.read(b, off, len)</code>
     * and returns the result.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in the destination array <code>b</code>
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or
     * <code>-1</code> if there is no more data because the end of
     * the stream has been reached.
     * @throws NullPointerException      If <code>b</code> is <code>null</code>.
     * @throws IndexOutOfBoundsException If <code>off</code> is negative,
     *                                   <code>len</code> is negative, or <code>len</code> is greater than
     *                                   <code>b.length - off</code>
     * @throws IOException               if an I/O error occurs.
     * @see #in
     */
    public int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, Maths.min(len, IOs.DEFAULT_BUFFER_SIZE));
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from the
     * input stream. The <code>skip</code> method may, for a variety of
     * reasons, end up skipping over some smaller number of bytes,
     * possibly <code>0</code>. The actual number of bytes skipped is
     * returned.
     * <p>
     * This method simply performs <code>in.skip(n)</code>.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if the stream does not support seek,
     *                     or if some other I/O error occurs.
     */
    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or
     * skipped over) from this input stream without blocking by the next
     * caller of a method for this input stream. The next caller might be
     * the same thread or another thread.  A single read or skip of this
     * many bytes will not block, but may read or skip fewer bytes.
     * <p>
     * This method returns the result of {@link #in in}.available().
     *
     * @return an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking.
     * @throws IOException if an I/O error occurs.
     */
    public int available() throws IOException {
        return in.available();
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * This
     * method simply performs <code>in.close()</code>.
     *
     * @throws IOException if an I/O error occurs.
     * @see #in
     */
    public void close() throws IOException {
        in.close();
    }

    /**
     * Marks the current position in this input stream. A subsequent
     * call to the <code>reset</code> method repositions this stream at
     * the last marked position so that subsequent reads re-read the same bytes.
     * <p>
     * The <code>readlimit</code> argument tells this input stream to
     * allow that many bytes to be read before the mark position gets
     * invalidated.
     * <p>
     * This method simply performs <code>in.mark(readlimit)</code>.
     *
     * @param readlimit the maximum limit of bytes that can be read before
     *                  the mark position becomes invalid.
     * @see #in
     * @see java.io.FilterInputStream#reset()
     */
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    /**
     * Repositions this stream to the position at the time the
     * <code>mark</code> method was last called on this input stream.
     * <p>
     * This method
     * simply performs <code>in.reset()</code>.
     * <p>
     * Stream marks are intended to be used in
     * situations where you need to read ahead a little to see what's in
     * the stream. Often this is most easily done by invoking some
     * general parser. If the stream is of the type handled by the
     * parse, it just chugs along happily. If the stream is not of
     * that type, the parser should toss an exception when it fails.
     * If this happens within readlimit bytes, it allows the outer
     * code to reset the stream and try another parser.
     *
     * @throws IOException if the stream has not been marked or if the
     *                     mark has been invalidated.
     * @see #in
     * @see java.io.FilterInputStream#mark(int)
     */
    public synchronized void reset() throws IOException {
        in.reset();
    }

    /**
     * Tests if this input stream supports the <code>mark</code>
     * and <code>reset</code> methods.
     * This method
     * simply performs <code>in.markSupported()</code>.
     *
     * @return <code>true</code> if this stream type supports the
     * <code>mark</code> and <code>reset</code> method;
     * <code>false</code> otherwise.
     * @see #in
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     */
    public boolean markSupported() {
        return in.markSupported();
    }
}
