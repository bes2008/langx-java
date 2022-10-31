package com.jn.langx.io.buffer;


import com.jn.langx.util.collection.buffer.exception.ReadOnlyBufferException;

/**
 * @since 5.1.0
 */
public class CharSequenceBuffer extends CharBuffer<CharSequenceBuffer> {
    private CharSequence str;

    public CharSequenceBuffer(CharSequence s) {
        this(s, 0, s.length());
    }

    public CharSequenceBuffer(CharSequence s, int start, int end) {
        super(-1, start, end, s.length());
        int n = s.length();
        if ((start < 0) || (end < start) || (end > n))
            throw new IndexOutOfBoundsException();
        str = s;
    }

    public CharSequenceBuffer slice() {
        return new CharSequenceBuffer(str,
                -1,
                0,
                this.remaining(),
                this.remaining(),
                offset + this.position());
    }

    private CharSequenceBuffer(CharSequence s,
                               long mark,
                               long pos,
                               long limit,
                               long cap,
                               long offset) {
        super(mark, pos, limit, cap, null, offset);
        str = s;
    }

    public CharSequenceBuffer duplicate() {
        return new CharSequenceBuffer(str, markValue(),
                position(), limit(), capacity(), offset);
    }


    public final char get() {
        return str.charAt((int) (nextGetIndex() + offset));
    }

    public final char get(int index) {
        return str.charAt((int) (checkIndex(index) + offset));
    }

    char getUnchecked(int index) {
        return str.charAt((int) (checkIndex(index) + offset));
    }

    // ## Override bulk get methods for better performance

    public final CharSequenceBuffer put(char c) {
        throw new ReadOnlyBufferException();
    }

    public final CharSequenceBuffer put(int index, char c) {
        throw new ReadOnlyBufferException();
    }

    public final CharSequenceBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    public final boolean isReadOnly() {
        return true;
    }

    public final String substring(long start, long end) {
        return str.toString().substring((int) (start + offset), (int) (end + offset));
    }

    public final CharSequenceBuffer subSequence(int start, int end) {
        try {
            long pos = position();
            return new CharSequenceBuffer(str,
                    -1,
                    pos + checkIndex(start, pos),
                    pos + checkIndex(end, pos),
                    capacity(),
                    offset);
        } catch (IllegalArgumentException x) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean isDirect() {
        return false;
    }
}