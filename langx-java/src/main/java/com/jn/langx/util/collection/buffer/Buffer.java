package com.jn.langx.util.collection.buffer;

import com.jn.langx.io.Rewindable;
import com.jn.langx.util.collection.buffer.exception.InvalidMarkException;
import com.jn.langx.util.collection.buffer.exception.ReadOnlyBufferException;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

public abstract class Buffer<BF extends Buffer> implements Rewindable {

    // Invariants: mark <= position <= limit <= capacity
    private long mark = -1;
    // index
    private long position = 0;
    // 实际长度
    private long limit;
    private long capacity;

    // Creates a new buffer with the given mark, position, limit, and capacity,
    // after checking invariants.
    //
    protected Buffer(long mark, long pos, long lim, long cap) {    // package-private
        if (cap < 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = cap;
        limit(lim);
        position(pos);
        if (mark >= 0) {
            if (mark > pos) {
                throw new IllegalArgumentException();
            }
            this.mark = mark;
        }
    }

    /**
     * Returns this buffer's capacity. </p>
     *
     * @return The capacity of this buffer
     */
    public final long capacity() {
        return capacity;
    }

    /**
     * Returns this buffer's position. </p>
     *
     * @return The position of this buffer
     */
    public final long position() {
        return position;
    }

    /**
     * Sets this buffer's position.  If the mark is defined and larger than the
     * new position then it is discarded. </p>
     *
     * @param newPosition The new position value; must be non-negative
     *                    and no larger than the current limit
     * @return This buffer
     * @throws IllegalArgumentException If the preconditions on <tt>newPosition</tt> do not hold
     */
    public final BF position(long newPosition) {
        if ((newPosition > limit) || (newPosition < 0)) {
            throw new IllegalArgumentException();
        }
        position = newPosition;
        if (mark > position) {
            mark = -1;
        }
        return (BF)this;
    }

    /**
     * Returns this buffer's limit. </p>
     *
     * @return The limit of this buffer
     */
    public final long limit() {
        return limit;
    }

    protected final long checkIndex(long i) {
        if ((i < 0) || (i >= limit))
            throw new IndexOutOfBoundsException();
        return i;
    }

    protected final long checkIndex(long i, long nb) {
        if ((i < 0) || (nb > limit - i))
            throw new IndexOutOfBoundsException();
        return i;
    }

    /**
     * 需要扩容时，才能调用该方法。
     * Sets this buffer's limit.  If the position is larger than the new limit
     * then it is set to the new limit.  If the mark is defined and larger than
     * the new limit then it is discarded. </p>
     *
     * @param newLimit The new limit value; must be non-negative
     *                 and no larger than this buffer's capacity
     * @return This buffer
     * @throws IllegalArgumentException If the preconditions on <tt>newLimit</tt> do not hold
     */
    public final BF limit(long newLimit) {
        if ((newLimit > capacity) || (newLimit < 0)) {
            throw new IllegalArgumentException();
        }
        limit = newLimit;
        if (position > limit) {
            position = limit;
        }
        if (mark > limit) {
            mark = -1;
        }
        return (BF)this;
    }

    /**
     * Sets this buffer's mark at its position. </p>
     *
     * @return This buffer
     */
    public final BF mark() {
        mark = position;
        return (BF)this;
    }

    /**
     * Resets this buffer's position to the previously-marked position.
     *
     * <p> Invoking this method neither changes nor discards the mark's
     * value. </p>
     *
     * @return This buffer
     * @throws InvalidMarkException If the mark has not been set
     */
    public final BF reset() {
        long m = mark;
        if (m < 0) {
            throw new InvalidMarkException();
        }
        position = m;
        return (BF)this;
    }

    /**
     * Clears this buffer.  The position is set to zero, the limit is set to
     * the capacity, and the mark is discarded.
     *
     * <p> Invoke this method before using a sequence of channel-read or
     * <i>put</i> operations to fill this buffer.  For example:
     *
     * <blockquote><pre>
     * buf.clear();     // Prepare buffer for reading
     * in.read(buf);    // Read data</pre></blockquote>
     *
     * <p> This method does not actually erase the data in the buffer, but it
     * is named as if it did because it will most often be used in situations
     * in which that might as well be the case. </p>
     *
     * @return This buffer
     */
    public BF clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return (BF)this;
    }

    /**
     * Flips this buffer.  The limit is set to the current position and then
     * the position is set to zero.  If the mark is defined then it is
     * discarded.
     *
     * <p> After a sequence of channel-read or <i>put</i> operations, invoke
     * this method to prepare for a sequence of channel-write or relative
     * <i>get</i> operations.  For example:
     *
     * <blockquote><pre>
     * buf.put(magic);    // Prepend header
     * in.read(buf);      // Read data into rest of buffer
     * buf.flip();        // Flip buffer
     * out.write(buf);    // Write header + data to channel</pre></blockquote>
     *
     * <p> This method is often used in conjunction with method when transferring data from
     * one place to another.  </p>
     *
     * @return This buffer
     */
    public BF flip() {
        limit = position;
        position = 0;
        mark = -1;
        return (BF)this;
    }

    /**
     * Rewinds this buffer.  The position is set to zero and the mark is
     * discarded.
     *
     * <p> Invoke this method before a sequence of channel-write or <i>get</i>
     * operations, assuming that the limit has already been set
     * appropriately.  For example:
     *
     * <blockquote><pre>
     * out.write(buf);    // Write remaining data
     * buf.rewind();      // Rewind buffer
     * buf.get(array);    // Copy data into array</pre></blockquote>
     *
     * @return This buffer
     */
    public BF rewind() {
        position = 0;
        mark = -1;
        return (BF)this;
    }

    /**
     * Returns the number of elements between the current position and the
     * limit. </p>
     *
     * @return The number of elements remaining in this buffer
     */
    public final long remaining() {
        return limit - position;
    }

    /**
     * Tells whether there are any elements between the current position and
     * the limit. </p>
     *
     * @return <tt>true</tt> if, and only if, there is at least one element
     * remaining in this buffer
     */
    public final boolean hasRemaining() {
        return position < limit;
    }

    protected final long nextGetIndex() {
        if (position >= limit)
            throw new BufferUnderflowException();
        return position++;
    }

    protected final long nextGetIndex(long nb) {
        if (limit - position < nb) {
            throw new BufferUnderflowException();
        }
        long p = position;
        position += nb;
        return p;
    }


    /**
     * Checks the current position against the limit, throwing a {@link
     * BufferOverflowException} if it is not smaller than the limit, and then
     * increments the position.
     *
     * @return The current position value, before it is incremented
     */
    protected final long nextPutIndex() {
        if (position >= limit)
            throw new BufferOverflowException();
        return position++;
    }


    /**
     * Tells whether or not this buffer is read-only. </p>
     *
     * @return <tt>true</tt> if, and only if, this buffer is read-only
     */
    public abstract boolean isReadOnly();

    /**
     * Tells whether or not this buffer is backed by an accessible
     * array.
     *
     * <p> If this method returns <tt>true</tt> then the {@link #array() array}
     * and {@link #arrayOffset() arrayOffset} methods may safely be invoked.
     * </p>
     *
     * @return <tt>true</tt> if, and only if, this buffer
     * is backed by an array and is not read-only
     * @since 2.10.6
     */
    public abstract boolean hasArray();

    /**
     * Returns the array that backs this
     * buffer&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> This method is intended to allow array-backed buffers to be
     * passed to native code more efficiently. Concrete subclasses
     * provide more strongly-typed return values for this method.
     *
     * <p> Modifications to this buffer's content will cause the returned
     * array's content to be modified, and vice versa.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before invoking this
     * method in order to ensure that this buffer has an accessible backing
     * array.  </p>
     *
     * @return The array that backs this buffer
     * @throws ReadOnlyBufferException       If this buffer is backed by an array but is read-only
     * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
     * @since 2.10.6
     */
    public abstract Object array();

    /**
     * Returns the offset within this buffer's backing array of the first
     * element of the buffer&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> If this buffer is backed by an array then buffer position <i>p</i>
     * corresponds to array index <i>p</i>&nbsp;+&nbsp;<tt>arrayOffset()</tt>.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before invoking this
     * method in order to ensure that this buffer has an accessible backing
     * array.  </p>
     *
     * @return The offset within this buffer's array
     * of the first element of the buffer
     * @throws ReadOnlyBufferException       If this buffer is backed by an array but is read-only
     * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
     * @since 2.10.6
     */
    public abstract long arrayOffset();

    static void checkBounds(long off, long len, long size) { // package-private
        if ((off | len | (off + len) | (size - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }

    public final long markValue() {                             // package-private
        return mark;
    }

    public final void truncate() {
        mark = -1;
        position = 0;
        limit = 0;
        capacity = 0;
    }

    public final void discardMark() {
        mark = -1;
    }

}
