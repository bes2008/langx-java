package com.jn.langx.io.buffer;


import com.jn.langx.util.collection.buffer.exception.BufferOverflowException;
import com.jn.langx.util.collection.buffer.exception.BufferUnderflowException;

/**
 * @since 5.1.0
 */
public class HeapCharBuffer extends CharBuffer<HeapCharBuffer> {


    public HeapCharBuffer(int cap, int lim) {

        super(-1, 0, lim, cap, new char[cap], 0);


    }

    public HeapCharBuffer(char[] buf, int off, int len) {

        super(-1, off, (off + len), buf.length, buf, 0);
    }

    public HeapCharBuffer(char[] buf,
                          long mark, long pos, long lim, long cap,
                          long off) {

        super(mark, pos, lim, cap, buf, off);
    }

    public HeapCharBuffer slice() {
        return new HeapCharBuffer(hb,
                -1,
                0,
                this.remaining(),
                this.remaining(),
                this.position() + offset);
    }

    public CharBuffer duplicate() {
        return new HeapCharBuffer(hb,
                this.markValue(),
                this.position(),
                this.limit(),
                this.capacity(),
                offset);
    }


    protected long ix(long i) {
        return i + offset;
    }

    public char get() {
        return hb[(int) ix(nextGetIndex())];
    }

    public char get(int i) {
        return hb[(int) ix(checkIndex(i))];
    }


    char getUnchecked(int i) {
        return hb[(int) ix(i)];
    }

    @Override
    public HeapCharBuffer get(char[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        System.arraycopy(hb, (int) ix(position()), dst, offset, length);
        position(position() + length);
        return this;
    }

    public boolean isDirect() {
        return false;
    }


    public boolean isReadOnly() {
        return false;
    }

    public HeapCharBuffer put(char x) {

        hb[(int) ix(nextPutIndex())] = x;
        return this;


    }

    public HeapCharBuffer put(int i, char x) {

        hb[(int) ix(checkIndex(i))] = x;
        return this;


    }
    @Override
    public CharBuffer put(char[] src, int offset, int length) {

        checkBounds(offset, length, src.length);
        if (length > remaining())
            throw new BufferOverflowException();
        System.arraycopy(src, offset, hb, (int) ix(position()), length);
        position(position() + length);
        return this;


    }
    @Override
    public HeapCharBuffer put(CharBuffer src) {

        if (src instanceof HeapCharBuffer) {
            if (src == this)
                throw new IllegalArgumentException();
            HeapCharBuffer sb = (HeapCharBuffer) src;
            long n = sb.remaining();
            if (n > remaining())
                throw new BufferOverflowException();
            System.arraycopy(sb.hb, (int) sb.ix(sb.position()),
                    hb, (int) ix(position()), (int) n);
            sb.position(sb.position() + n);
            position(position() + n);
        } else if (src.isDirect()) {
            long n = src.remaining();
            if (n > remaining())
                throw new BufferOverflowException();
            src.get(hb, (int) ix(position()), (int) n);
            position(position() + n);
        } else {
            super.put(src);
        }
        return this;


    }

    public HeapCharBuffer compact() {

        System.arraycopy(hb, (int) ix(position()), hb, (int) ix(0), (int) remaining());
        position(remaining());
        limit(capacity());
        discardMark();
        return this;
    }


    public String substring(long start, long end) {
        try {
            return new String(hb, (int) (start + offset), (int) (end - start));
        } catch (StringIndexOutOfBoundsException x) {
            throw new IndexOutOfBoundsException();
        }
    }


    // --- Methods to support CharSequence ---

    public HeapCharBuffer subSequence(int start, int end) {
        if ((start < 0)
                || (end > length())
                || (start > end))
            throw new IndexOutOfBoundsException();
        long pos = position();
        return new HeapCharBuffer(hb,
                -1,
                pos + start,
                pos + end,
                capacity(),
                offset);
    }

}
