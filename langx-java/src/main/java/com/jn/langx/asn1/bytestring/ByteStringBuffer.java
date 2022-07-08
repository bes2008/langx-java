package com.jn.langx.asn1.bytestring;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.spec.ASN1OctetString;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.Utf8s;

/**
 * This class provides a growable byte array to which data can be appended.
 * Methods in this class are not synchronized.
 */
public final class ByteStringBuffer
        implements Serializable, Appendable {
    /**
     * The default initial capacity for this buffer.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 20;


    /**
     * The pre-allocated array that will be used for a boolean value of "false".
     */
    @NonNull
    private static final byte[] FALSE_VALUE_BYTES = Utf8s.getBytes("false");


    /**
     * The pre-allocated array that will be used for a boolean value of "true".
     */
    @NonNull
    private static final byte[] TRUE_VALUE_BYTES = Utf8s.getBytes("true");


    /**
     * A thread-local byte array that will be used for holding numeric values
     * to append to the buffer.
     */
    @NonNull
    private static final ThreadLocal<byte[]> TEMP_NUMBER_BUFFER =
            new ThreadLocal<byte[]>();


    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = 2899392249591230998L;


    // The backing array for this buffer.
    @NonNull
    private byte[] array;

    // The length of the backing array.
    private int capacity;

    // The position at which to append the next data.
    private int endPos;


    /**
     * Creates a new empty byte string buffer with a default initial capacity.
     */
    public ByteStringBuffer() {
        this(DEFAULT_INITIAL_CAPACITY);
    }


    /**
     * Creates a new byte string buffer with the specified capacity.
     *
     * @param initialCapacity The initial capacity to use for the buffer.  It
     *                        must be greater than or equal to zero.
     */
    public ByteStringBuffer(final int initialCapacity) {
        array = new byte[initialCapacity];
        capacity = initialCapacity;
        endPos = 0;
    }


    /**
     * Appends the provided boolean value to this buffer.
     *
     * @param b The boolean value to be appended to this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer append(final boolean b) {
        if (b) {
            return append(TRUE_VALUE_BYTES, 0, 4);
        } else {
            return append(FALSE_VALUE_BYTES, 0, 5);
        }
    }


    /**
     * Appends the provided byte to this buffer.
     *
     * @param b The byte to be appended to this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer append(final byte b) {
        ensureCapacity(endPos + 1);
        array[endPos++] = b;
        return this;
    }


    /**
     * Appends the contents of the provided byte array to this buffer.
     *
     * @param b The array whose contents should be appended to this buffer.  It
     *          must not be {@code null}.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided array is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final byte[] b)
            throws NullPointerException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        return append(b, 0, b.length);
    }


    /**
     * Appends the specified portion of the provided byte array to this buffer.
     *
     * @param b   The array whose contents should be appended to this buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of bytes to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the offset or length are negative,
     *                                   if the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final byte[] b, final int off,
                                   final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        if ((off < 0) || (len < 0) || (off + len > b.length)) {
            final String message;
            if (off < 0) {
                String err = "The provided offset {} is negative.";
                message = StringTemplates.formatWithPlaceholder(err, off);
            } else if (len < 0) {
                String err = "The provided length {} is negative.";
                message = StringTemplates.formatWithPlaceholder(err, len);
            } else {
                String err = "The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).";
                message = StringTemplates.formatWithPlaceholder(err, off, len,
                        b.length);
            }

            throw new IndexOutOfBoundsException(message);
        }

        if (len > 0) {
            ensureCapacity(endPos + len);
            System.arraycopy(b, off, array, endPos, len);
            endPos += len;
        }

        return this;
    }


    /**
     * Appends the provided byte string to this buffer.
     *
     * @param b The byte string to be appended to this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided byte string is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final ByteString b)
            throws NullPointerException {
        if (b == null) {
            throw new NullPointerException("The provided byte string is null.");
        }

        b.appendValueTo(this);
        return this;
    }


    /**
     * Appends the provided byte string buffer to this buffer.
     *
     * @param buffer The buffer whose contents should be appended to this
     *               buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided buffer is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final ByteStringBuffer buffer)
            throws NullPointerException {
        if (buffer == null) {
            throw new NullPointerException("The provided buffer is null.");
        }

        return append(buffer.array, 0, buffer.endPos);
    }


    /**
     * Appends the provided character to this buffer.
     *
     * @param c The character to be appended to this buffer.
     * @return A reference to this buffer.
     */
    @Override()
    @NonNull()
    public ByteStringBuffer append(final char c) {
        final byte b = (byte) (c & 0x7F);
        if (b == c) {
            ensureCapacity(endPos + 1);
            array[endPos++] = b;
        } else {
            append(String.valueOf(c));
        }

        return this;
    }


    /**
     * Appends the contents of the provided character array to this buffer.
     *
     * @param c The array whose contents should be appended to this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided array is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final char[] c)
            throws NullPointerException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        return append(c, 0, c.length);
    }


    /**
     * Appends the specified portion of the provided character array to this
     * buffer.
     *
     * @param c   The array whose contents should be appended to this buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of characters to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the offset or length are negative,
     *                                   if the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer append(@NonNull final char[] c, final int off,
                                   final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        if ((off < 0) || (len < 0) || (off + len > c.length)) {
            final String message;
            if (off < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", off);
            } else if (len < 0) {
                String err = "The provided length {} is negative.";
                message = StringTemplates.formatWithPlaceholder(err, len);
            } else {
                String err = "The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).";
                message = StringTemplates.formatWithPlaceholder(err, off, len,
                        c.length);
            }

            throw new IndexOutOfBoundsException(message);
        }

        if (len > 0) {
            ensureCapacity(endPos + len);

            int pos = off;
            for (int i = 0; i < len; i++, pos++) {
                final byte b = (byte) (c[pos] & 0x7F);
                if (b == c[pos]) {
                    array[endPos++] = b;
                } else {
                    final String remainingString =
                            String.valueOf(c, pos, (off + len - pos));
                    final byte[] remainingBytes = Utf8s.getBytes(remainingString);
                    return append(remainingBytes);
                }
            }
        }

        return this;
    }


    /**
     * Appends the provided character sequence to this buffer.
     *
     * @param s The character sequence to append to this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided character sequence is
     *                              {@code null}.
     */
    @Override()
    @NonNull()
    public ByteStringBuffer append(@NonNull final CharSequence s)
            throws NullPointerException {
        final String str = s.toString();
        return append(str, 0, str.length());
    }


    /**
     * Appends the provided character sequence to this buffer.
     *
     * @param s     The character sequence to append to this buffer.
     * @param start The position in the sequence of the first character in the
     *              sequence to be appended to this buffer.
     * @param end   The position in the sequence immediately after the position
     *              of the last character to be appended.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided character sequence is
     *                                   {@code null}.
     * @throws IndexOutOfBoundsException If the provided start or end positions
     *                                   are outside the bounds of the given
     *                                   character sequence.
     */
    @Override()
    @NonNull()
    public ByteStringBuffer append(@NonNull final CharSequence s, final int start, final int end)
            throws NullPointerException, IndexOutOfBoundsException {
        if (s == null) {
            throw new NullPointerException("The provided character sequence is null.");
        }

        final String string = s.toString();
        final int stringLength = string.length();
        if (start < 0) {
            String err = "The provided start position {} is negative.";
            err = StringTemplates.formatWithPlaceholder(err, start);
            throw new IndexOutOfBoundsException(err);
        } else if (start > end) {
            String err = "The provided start position {} is greater than the provided end position {}.";
            err = StringTemplates.formatWithPlaceholder(err, start, end);
            throw new IndexOutOfBoundsException(err);
        } else if (start > stringLength) {
            String err = "The provided start position {} is beyond the length {} of the provided value.";
            err = StringTemplates.formatWithPlaceholder(err, start, stringLength);
            throw new IndexOutOfBoundsException(err);
        } else if (end > stringLength) {
            String err = "The provided end position {} is beyond the length {} of the provided value.";
            err = StringTemplates.formatWithPlaceholder(err, end, stringLength);
            throw new IndexOutOfBoundsException(err);
        } else if (start < end) {
            ensureCapacity(endPos + (end - start));
            for (int pos = start; pos < end; pos++) {
                final char c = string.charAt(pos);
                if (c <= 0x7F) {
                    array[endPos++] = (byte) (c & 0x7F);
                } else {
                    final String remainingString = string.substring(pos, end);
                    final byte[] remainingBytes = Utf8s.getBytes(remainingString);
                    return append(remainingBytes);
                }
            }
        }

        return this;
    }


    /**
     * Appends the provided integer value to this buffer.
     *
     * @param i The integer value to be appended to this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer append(final int i) {
        final int length = getBytes(i);
        return append(TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Appends the provided long value to this buffer.
     *
     * @param l The long value to be appended to this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer append(final long l) {
        final int length = getBytes(l);
        return append(TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Appends the provided code point to this buffer.
     *
     * @param codePoint The code point to append to this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer appendCodePoint(final int codePoint) {
        final int charByte = codePoint & 0x7F;
        if (charByte == codePoint) {
            return append((byte) charByte);
        } else if (isBmpCodePoint(codePoint)) {
            return append((char) codePoint);
        } else {
            return append(Character.toChars(codePoint));
        }
    }


    /**
     * Inserts the provided boolean value to this buffer.
     *
     * @param pos The position at which the value is to be inserted.
     * @param b   The boolean value to be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, final boolean b)
            throws IndexOutOfBoundsException {
        if (b) {
            return insert(pos, TRUE_VALUE_BYTES, 0, 4);
        } else {
            return insert(pos, FALSE_VALUE_BYTES, 0, 5);
        }
    }


    /**
     * Inserts the provided byte at the specified position in this buffer.
     *
     * @param pos The position at which the byte is to be inserted.
     * @param b   The byte to be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, final byte b)
            throws IndexOutOfBoundsException {
        if ((pos < 0) || (pos > endPos)) {
            final String message;
            if (pos < 0) {
                String err = "The provided position {} is negative.";
                message = StringTemplates.formatWithPlaceholder(err, pos);
            } else {
                String err = "The provided position {} is greater than the length of the buffer ({}).";
                message = StringTemplates.formatWithPlaceholder(err, pos, endPos);
            }

            throw new IndexOutOfBoundsException(message);
        } else if (pos == endPos) {
            return append(b);
        }

        ensureCapacity(endPos + 1);
        System.arraycopy(array, pos, array, pos + 1, (endPos - pos));
        array[pos] = b;
        endPos++;
        return this;
    }


    /**
     * Inserts the contents of the provided byte array at the specified position
     * in this buffer.
     *
     * @param pos The position at which the data is to be inserted.
     * @param b   The array whose contents should be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final byte[] b)
            throws NullPointerException, IndexOutOfBoundsException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        return insert(pos, b, 0, b.length);
    }


    /**
     * Inserts a portion of the data in the provided array at the specified
     * position in this buffer.
     * <p>
     * Appends the specified portion of the provided byte array to this buffer.
     *
     * @param pos The position at which the data is to be inserted.
     * @param b   The array whose contents should be inserted into this buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of bytes to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length, if
     *                                   the offset or length are negative, if
     *                                   the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final byte[] b,
                                   final int off, final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        if ((pos < 0) || (pos > endPos) || (off < 0) || (len < 0) ||
                (off + len > b.length)) {
            final String message;
            if (pos < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is negative.", pos);
            } else if (pos > endPos) {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is greater than the length of the buffer ({}).", pos, endPos);
            } else if (off < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", off);
            } else if (len < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided length {} is negative.", len);
            } else {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).", off, len,
                        b.length);
            }

            throw new IndexOutOfBoundsException(message);
        } else if (len == 0) {
            return this;
        } else if (pos == endPos) {
            return append(b, off, len);
        }

        ensureCapacity(endPos + len);
        System.arraycopy(array, pos, array, pos + len, (endPos - pos));
        System.arraycopy(b, off, array, pos, len);
        endPos += len;
        return this;
    }


    /**
     * Inserts the provided byte string into this buffer at the specified
     * position.
     *
     * @param pos The position at which the data is to be inserted.
     * @param b   The byte string to insert into this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided byte string is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final ByteString b)
            throws NullPointerException, IndexOutOfBoundsException {
        if (b == null) {
            throw new NullPointerException("The provided byte string is null.");
        }

        return insert(pos, b.getValue());
    }


    /**
     * Inserts the provided byte string buffer into this buffer at the specified
     * position.
     *
     * @param pos    The position at which the data is to be inserted.
     * @param buffer The buffer whose contents should be inserted into this
     *               buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided buffer is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos,
                                   @NonNull final ByteStringBuffer buffer)
            throws NullPointerException, IndexOutOfBoundsException {
        if (buffer == null) {
            throw new NullPointerException("The provided buffer is null.");
        }

        return insert(pos, buffer.array, 0, buffer.endPos);
    }


    /**
     * Inserts the provided character into this buffer at the provided position.
     *
     * @param pos The position at which the character is to be inserted.
     * @param c   The character to be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, final char c)
            throws IndexOutOfBoundsException {
        if ((pos < 0) || (pos > endPos)) {
            final String message;
            if (pos < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is negative.", pos);
            } else {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is greater than the length of the buffer ({}).", pos, endPos);
            }

            throw new IndexOutOfBoundsException(message);
        } else if (pos == endPos) {
            return append(c);
        }

        final byte b = (byte) (c & 0x7F);
        if (b == c) {
            ensureCapacity(endPos + 1);
            System.arraycopy(array, pos, array, pos + 1, (endPos - pos));
            array[pos] = b;
            endPos++;
        } else {
            insert(pos, String.valueOf(c));
        }

        return this;
    }


    /**
     * Inserts the contents of the provided character array into this buffer at
     * the specified position.
     *
     * @param pos The position at which the data is to be inserted.
     * @param c   The array whose contents should be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final char[] c)
            throws NullPointerException, IndexOutOfBoundsException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        return insert(pos, new String(c, 0, c.length));
    }


    /**
     * Inserts the specified portion of the provided character array to this
     * buffer at the specified position.
     *
     * @param pos The position at which the data is to be inserted.
     * @param c   The array whose contents should be inserted into this buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of characters to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length, if
     *                                   the offset or length are negative, if
     *                                   the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final char[] c,
                                   final int off, final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        return insert(pos, new String(c, off, len));
    }


    /**
     * Inserts the provided character sequence to this buffer at the specified
     * position.
     *
     * @param pos The position at which the data is to be inserted.
     * @param s   The character sequence to insert into this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided character sequence is
     *                                   {@code null}.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, @NonNull final CharSequence s)
            throws NullPointerException, IndexOutOfBoundsException {
        if (s == null) {
            throw new NullPointerException("The provided character sequence is null.");
        }

        if ((pos < 0) || (pos > endPos)) {
            final String message;
            if (pos < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is negative.", pos);
            } else {
                message = StringTemplates.formatWithPlaceholder("The provided position {} is greater than the length of the buffer ({}).", pos, endPos);
            }

            throw new IndexOutOfBoundsException(message);
        } else if (pos == endPos) {
            return append(s);
        } else {
            return insert(pos, Utf8s.getBytes(s.toString()));
        }
    }


    /**
     * Inserts the provided integer value to this buffer.
     *
     * @param pos The position at which the value is to be inserted.
     * @param i   The integer value to be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, final int i)
            throws IndexOutOfBoundsException {
        final int length = getBytes(i);
        return insert(pos, TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Inserts the provided long value to this buffer.
     *
     * @param pos The position at which the value is to be inserted.
     * @param l   The long value to be inserted into this buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insert(final int pos, final long l)
            throws IndexOutOfBoundsException {
        final int length = getBytes(l);
        return insert(pos, TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Inserts the provided code point into this buffer.
     *
     * @param pos       The position at which the code point is to be inserted.
     * @param codePoint The code point to be inserted.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified position is negative
     *                                   or greater than the current length.
     */
    @NonNull()
    public ByteStringBuffer insertCodePoint(final int pos, final int codePoint)
            throws IndexOutOfBoundsException {
        final int charByte = codePoint & 0x7F;
        if (charByte == codePoint) {
            return insert(pos, (byte) charByte);
        } else if (isBmpCodePoint(codePoint)) {
            return insert(pos, (char) codePoint);
        } else {
            return insert(pos, Character.toChars(codePoint));
        }
    }


    /**
     * Deletes the specified number of bytes from the beginning of the buffer.
     *
     * @param len The number of bytes to delete.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the specified length is negative,
     *                                   or if it is greater than the number of
     *                                   bytes currently contained in this
     *                                   buffer.
     */
    @NonNull()
    public ByteStringBuffer delete(final int len)
            throws IndexOutOfBoundsException {
        return delete(0, len);
    }


    /**
     * Deletes the indicated number of bytes from the specified location in the
     * buffer.
     *
     * @param off The position in the buffer at which the content to delete
     *            begins.
     * @param len The number of bytes to remove from the buffer.
     * @return A reference to this buffer.
     * @throws IndexOutOfBoundsException If the offset or length is negative, or
     *                                   if the combination of the offset and
     *                                   length is greater than the end of the
     *                                   content in the buffer.
     */
    @NonNull()
    public ByteStringBuffer delete(final int off, final int len)
            throws IndexOutOfBoundsException {
        if (off < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", off));
        } else if (len < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided length {} is negative.", len));
        } else if ((off + len) > endPos) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).", off, len, endPos));
        } else if (len == 0) {
            return this;
        } else if (off == 0) {
            if (len == endPos) {
                endPos = 0;
                return this;
            } else {
                final int newEndPos = endPos - len;
                System.arraycopy(array, len, array, 0, newEndPos);
                endPos = newEndPos;
                return this;
            }
        } else {
            if ((off + len) == endPos) {
                endPos = off;
                return this;
            } else {
                final int bytesToCopy = endPos - (off + len);
                System.arraycopy(array, (off + len), array, off, bytesToCopy);
                endPos -= len;
                return this;
            }
        }
    }


    /**
     * Sets the contents of this buffer to include only the provided boolean
     * value.
     *
     * @param b The boolean value to use as the content for this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer set(final boolean b) {
        if (b) {
            return set(TRUE_VALUE_BYTES, 0, 4);
        } else {
            return set(FALSE_VALUE_BYTES, 0, 5);
        }
    }


    /**
     * Sets the contents of this buffer to include only the provided byte.
     *
     * @param b The byte to use as the content for this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer set(final byte b) {
        endPos = 0;
        return append(b);
    }


    /**
     * Sets the contents of this buffer to the contents of the provided byte
     * array.
     *
     * @param b The byte array containing the content to use for this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided array is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final byte[] b)
            throws NullPointerException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        endPos = 0;
        return append(b, 0, b.length);
    }


    /**
     * Sets the contents of this buffer to the specified portion of the provided
     * byte array.
     *
     * @param b   The byte array containing the content to use for this buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of bytes to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the offset or length are negative,
     *                                   if the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final byte[] b, final int off,
                                final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (b == null) {
            throw new NullPointerException("The provided array is null.");
        }

        if ((off < 0) || (len < 0) || (off + len > b.length)) {
            final String message;
            if (off < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", off);
            } else if (len < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided length {} is negative.", len);
            } else {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).", off, len,
                        b.length);
            }

            throw new IndexOutOfBoundsException(message);
        }

        endPos = 0;
        return append(b, off, len);
    }


    /**
     * Sets the contents of this buffer to the contents of the provided byte
     * string.
     *
     * @param b The byte string that should be used as the content for this
     *          buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided byte string is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final ByteString b)
            throws NullPointerException {
        if (b == null) {
            throw new NullPointerException("The provided byte string is null.");
        }

        endPos = 0;
        b.appendValueTo(this);
        return this;
    }


    /**
     * Sets the contents of this buffer to the contents of the provided byte
     * string buffer.
     *
     * @param buffer The buffer whose contents should be used as the content for
     *               this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided buffer is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final ByteStringBuffer buffer)
            throws NullPointerException {
        if (buffer == null) {
            throw new NullPointerException("The provided buffer is null.");
        }

        endPos = 0;
        return append(buffer.array, 0, buffer.endPos);
    }


    /**
     * Sets the contents of this buffer to include only the provided character.
     *
     * @param c The character use as the content for this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer set(final char c) {
        endPos = 0;
        return append(c);
    }


    /**
     * Sets the contents of this buffer to the contents of the provided character
     * array.
     *
     * @param c The character array containing the content to use for this
     *          buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided array is {@code null}.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final char[] c)
            throws NullPointerException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        endPos = 0;
        return append(c, 0, c.length);
    }


    /**
     * Sets the contents of this buffer to the specified portion of the provided
     * character array.
     *
     * @param c   The character array containing the content to use for this
     *            buffer.
     * @param off The offset within the array at which to begin copying data.
     * @param len The number of characters to copy.
     * @return A reference to this buffer.
     * @throws NullPointerException      If the provided array is {@code null}.
     * @throws IndexOutOfBoundsException If the offset or length are negative,
     *                                   if the offset plus the length is beyond
     *                                   the end of the provided array.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final char[] c, final int off,
                                final int len)
            throws NullPointerException, IndexOutOfBoundsException {
        if (c == null) {
            throw new NullPointerException("The provided array is null.");
        }

        if ((off < 0) || (len < 0) || (off + len > c.length)) {
            final String message;
            if (off < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", off);
            } else if (len < 0) {
                message = StringTemplates.formatWithPlaceholder("The provided length {} is negative.", len);
            } else {
                message = StringTemplates.formatWithPlaceholder("The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).", off, len,
                        c.length);
            }

            throw new IndexOutOfBoundsException(message);
        }

        endPos = 0;
        return append(c, off, len);
    }


    /**
     * Sets the contents of this buffer to the specified portion of the provided
     * character sequence.
     *
     * @param s The character sequence to use as the content for this buffer.
     * @return A reference to this buffer.
     * @throws NullPointerException If the provided character sequence is
     *                              {@code null}.
     */
    @NonNull()
    public ByteStringBuffer set(@NonNull final CharSequence s)
            throws NullPointerException {
        if (s == null) {
            throw new NullPointerException("The provided character sequence is null.");
        }

        endPos = 0;
        return append(s);
    }


    /**
     * Sets the contents of this buffer to include only the provided integer
     * value.
     *
     * @param i The integer value to use as the content for this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer set(final int i) {
        final int length = getBytes(i);
        return set(TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Sets the contents of this buffer to include only the provided long value.
     *
     * @param l The long value to use as the content for this buffer.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer set(final long l) {
        final int length = getBytes(l);
        return set(TEMP_NUMBER_BUFFER.get(), 0, length);
    }


    /**
     * Clears the contents of this buffer.
     *
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer clear() {
        endPos = 0;
        return this;
    }


    /**
     * Clears the contents of this buffer.
     *
     * @param zero Indicates whether to overwrite the content of the backing
     *             array with all zeros in order to wipe out any sensitive data
     *             it may contain.
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer clear(final boolean zero) {
        endPos = 0;

        if (zero) {
            Arrays.fill(array, (byte) 0x00);
        }

        return this;
    }


    /**
     * Retrieves the current backing array for this buffer.  The data will begin
     * at position 0 and will contain {@link ByteStringBuffer#length} bytes.
     *
     * @return The current backing array for this buffer.
     */
    @NonNull()
    public byte[] getBackingArray() {
        return array;
    }


    /**
     * Indicates whether this buffer is currently empty.
     *
     * @return {@code true} if this buffer is currently empty, or {@code false}
     * if not.
     */
    public boolean isEmpty() {
        return (endPos == 0);
    }


    /**
     * Retrieves the number of bytes contained in this buffer.
     *
     * @return The number of bytes contained in this buffer.
     */
    public int length() {
        return endPos;
    }


    /**
     * Sets the length of this buffer to the specified value.  If the new length
     * is greater than the current length, the value will be padded with zeroes.
     *
     * @param length The new length to use for the buffer.  It must be greater
     *               than or equal to zero.
     * @throws IndexOutOfBoundsException If the provided length is negative.
     */
    public void setLength(final int length)
            throws IndexOutOfBoundsException {
        if (length < 0) {
            String err = "The provided length {} is negative.";
            err = StringTemplates.formatWithPlaceholder(err, length);
            throw new IndexOutOfBoundsException(err);
        }

        if (length > endPos) {
            ensureCapacity(length);
            Arrays.fill(array, endPos, length, (byte) 0x00);
            endPos = length;
        } else {
            endPos = length;
        }
    }


    /**
     * Returns the current capacity for this buffer.
     *
     * @return The current capacity for this buffer.
     */
    public int capacity() {
        return capacity;
    }


    /**
     * Ensures that the total capacity of this buffer is at least equal to the
     * specified size.
     *
     * @param minimumCapacity The minimum capacity for this buffer.
     */
    public void ensureCapacity(final int minimumCapacity) {
        if (capacity < minimumCapacity) {
            final int newCapacity = Math.max(minimumCapacity, (2 * capacity) + 2);
            final byte[] newArray = new byte[newCapacity];
            System.arraycopy(array, 0, newArray, 0, capacity);
            array = newArray;
            capacity = newCapacity;
        }
    }


    /**
     * Sets the capacity equal to the specified value.  If the provided capacity
     * is less than the current length, then the length will be reduced to the
     * new capacity.
     *
     * @param capacity The new capacity for this buffer.  It must be greater
     *                 than or equal to zero.
     * @throws IndexOutOfBoundsException If the provided capacity is negative.
     */
    public void setCapacity(final int capacity)
            throws IndexOutOfBoundsException {
        if (capacity < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided capacity {} is negative.", capacity));
        }

        if (this.capacity == capacity) {
            return;
        } else if (this.capacity < capacity) {
            final byte[] newArray = new byte[capacity];
            System.arraycopy(array, 0, newArray, 0, this.capacity);
            array = newArray;
            this.capacity = capacity;
        } else {
            final byte[] newArray = new byte[capacity];
            System.arraycopy(array, 0, newArray, 0, capacity);
            array = newArray;
            endPos = Math.min(endPos, capacity);
            this.capacity = capacity;
        }
    }


    /**
     * Trims the backing array to the minimal size required for this buffer.
     *
     * @return A reference to this buffer.
     */
    @NonNull()
    public ByteStringBuffer trimToSize() {
        if (endPos != capacity) {
            final byte[] newArray = new byte[endPos];
            System.arraycopy(array, 0, newArray, 0, endPos);
            array = newArray;
            capacity = endPos;
        }

        return this;
    }


    /**
     * Retrieves the byte at the specified offset in the buffer.
     *
     * @param offset The offset of the byte to read.  It must be between greater
     *               than or equal to zero and less than {@link #length}.
     * @return The byte at the specified offset in the buffer.
     * @throws IndexOutOfBoundsException If the provided offset is negative or
     *                                   greater than or equal to the length of
     *                                   the buffer.
     */
    public byte byteAt(final int offset)
            throws IndexOutOfBoundsException {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", offset));
        } else if (offset >= endPos) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} is too large for the buffer with length {}.", offset, endPos));
        } else {
            return array[offset];
        }
    }


    /**
     * Retrieves the specified subset of bytes from the buffer.
     *
     * @param offset The offset of the first byte to retrieve.  It must be
     *               greater than or equal to zero and less than
     *               {@link #length}.
     * @param length The number of bytes to retrieve.  It must be greater than
     *               or equal to zero, and the sum of {@code offset} and
     *               {@code length} must be less than or equal to
     *               {@link #length}.
     * @return A byte array containing the specified subset of bytes from the
     * buffer.
     * @throws IndexOutOfBoundsException If either the offset or the length is
     *                                   negative, or if the offsset plus the
     *                                   length is greater than or equal to the
     *                                   length of the buffer.
     */
    @NonNull()
    public byte[] bytesAt(final int offset, final int length)
            throws IndexOutOfBoundsException {
        if (offset < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} is negative.", offset));
        }

        if (length < 0) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided length {} is negative.", length));
        }

        if ((offset + length) > endPos) {
            throw new IndexOutOfBoundsException(
                    StringTemplates.formatWithPlaceholder("The provided offset {} plus the provided length {} is greater than the size of the provided array ({}).", offset, length,
                            endPos));
        }

        final byte[] returnArray = new byte[length];
        System.arraycopy(array, offset, returnArray, 0, length);
        return returnArray;
    }


    /**
     * Indicates whether this buffer starts with the specified set of bytes.
     *
     * @param bytes The bytes for which to make the determination.
     * @return {@code true} if this buffer starts with the specified set of
     * bytes, or {@code false} if not.
     */
    public boolean startsWith(@NonNull final byte[] bytes) {
        if (bytes.length > endPos) {
            return false;
        }

        for (int i = 0; i < bytes.length; i++) {
            if (array[i] != bytes[i]) {
                return false;
            }
        }

        return true;
    }


    /**
     * Indicates whether this buffer ends with the specified set of bytes.
     *
     * @param bytes The bytes for which to make the determination.
     * @return {@code true} if this buffer ends with the specified set of bytes,
     * or {@code false} if not.
     */
    public boolean endsWith(@NonNull final byte[] bytes) {
        if (bytes.length > endPos) {
            return false;
        }

        for (int i = 0; i < bytes.length; i++) {
            if (array[endPos - bytes.length + i] != bytes[i]) {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns a new byte array with the content from this buffer.
     *
     * @return A byte array containing the content from this buffer.
     */
    @NonNull()
    public byte[] toByteArray() {
        final byte[] newArray = new byte[endPos];
        System.arraycopy(array, 0, newArray, 0, endPos);
        return newArray;
    }


    /**
     * Returns a new byte string with the content from this buffer.
     *
     * @return A byte string with the content from this buffer.
     */
    @NonNull()
    public ByteString toByteString() {
        return new ASN1OctetString(toByteArray());
    }


    /**
     * Creates an input stream that may be used to read content from this buffer.
     * This buffer should not be altered while the input stream is being used.
     *
     * @return An input stream that may be used to read content from this buffer.
     */
    @NonNull()
    public InputStream asInputStream() {
        return new ByteArrayInputStream(array, 0, endPos);
    }


    /**
     * Reads the contents of the specified file into this buffer, appending it to
     * the end of the buffer.
     *
     * @param file The file to be read.
     * @throws IOException If an unexpected problem occurs.
     */
    public void readFrom(@NonNull final File file)
            throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            readFrom(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }


    /**
     * Reads data from the provided input stream into this buffer, appending it to
     * the end of the buffer.  The entire content of the input stream will be
     * read, but the input stream will not be closed.
     *
     * @param inputStream The input stream from which data is to be read.
     * @throws IOException If an unexpected problem occurs.
     */
    public void readFrom(@NonNull final InputStream inputStream)
            throws IOException {
        final int initialEndPos = endPos;

        try {
            while (true) {
                int remainingCapacity = capacity - endPos;
                if (remainingCapacity <= 100) {
                    ensureCapacity(Math.max(100, (2 * capacity)));
                    remainingCapacity = capacity - endPos;
                }

                final int bytesRead =
                        inputStream.read(array, endPos, remainingCapacity);
                if (bytesRead < 0) {
                    return;
                }

                endPos += bytesRead;
            }
        } catch (final IOException e) {
            endPos = initialEndPos;
            throw e;
        }
    }


    /**
     * Writes the contents of this byte string buffer to the provided output
     * stream.
     *
     * @param outputStream The output stream to which the data should be
     *                     written.
     * @throws IOException If a problem occurs while writing to the provided
     *                     output stream.
     */
    public void write(@NonNull final OutputStream outputStream)
            throws IOException {
        outputStream.write(array, 0, endPos);
    }


    /**
     * Adds the bytes comprising the string representation of the provided long
     * value to the temporary number buffer.
     *
     * @param l The long value to be appended.
     * @return The number of bytes in the string representation of the value.
     */
    private static int getBytes(final long l) {
        // NOTE:  This method is probably not as efficient as it could be, but it is
        // more important to avoid the need for memory allocation.
        byte[] b = TEMP_NUMBER_BUFFER.get();
        if (b == null) {
            b = new byte[20];
            TEMP_NUMBER_BUFFER.set(b);
        }

        if (l == Long.MIN_VALUE) {
            b[0] = '-';
            b[1] = '9';
            b[2] = '2';
            b[3] = '2';
            b[4] = '3';
            b[5] = '3';
            b[6] = '7';
            b[7] = '2';
            b[8] = '0';
            b[9] = '3';
            b[10] = '6';
            b[11] = '8';
            b[12] = '5';
            b[13] = '4';
            b[14] = '7';
            b[15] = '7';
            b[16] = '5';
            b[17] = '8';
            b[18] = '0';
            b[19] = '8';
            return 20;
        } else if (l == 0L) {
            b[0] = '0';
            return 1;
        }

        int pos = 0;
        long v = l;
        if (l < 0) {
            b[0] = '-';
            pos = 1;
            v = Math.abs(l);
        }

        long divisor;
        if (v <= 9L) {
            divisor = 1L;
        } else if (v <= 99L) {
            divisor = 10L;
        } else if (v <= 999L) {
            divisor = 100L;
        } else if (v <= 9999L) {
            divisor = 1000L;
        } else if (v <= 99999L) {
            divisor = 10000L;
        } else if (v <= 999999L) {
            divisor = 100000L;
        } else if (v <= 9999999L) {
            divisor = 1000000L;
        } else if (v <= 99999999L) {
            divisor = 10000000L;
        } else if (v <= 999999999L) {
            divisor = 100000000L;
        } else if (v <= 9999999999L) {
            divisor = 1000000000L;
        } else if (v <= 99999999999L) {
            divisor = 10000000000L;
        } else if (v <= 999999999999L) {
            divisor = 100000000000L;
        } else if (v <= 9999999999999L) {
            divisor = 1000000000000L;
        } else if (v <= 99999999999999L) {
            divisor = 10000000000000L;
        } else if (v <= 999999999999999L) {
            divisor = 100000000000000L;
        } else if (v <= 9999999999999999L) {
            divisor = 1000000000000000L;
        } else if (v <= 99999999999999999L) {
            divisor = 10000000000000000L;
        } else if (v <= 999999999999999999L) {
            divisor = 100000000000000000L;
        } else {
            divisor = 1000000000000000000L;
        }

        while (true) {
            final long digit = v / divisor;
            switch ((int) digit) {
                case 0:
                    b[pos++] = '0';
                    break;
                case 1:
                    b[pos++] = '1';
                    break;
                case 2:
                    b[pos++] = '2';
                    break;
                case 3:
                    b[pos++] = '3';
                    break;
                case 4:
                    b[pos++] = '4';
                    break;
                case 5:
                    b[pos++] = '5';
                    break;
                case 6:
                    b[pos++] = '6';
                    break;
                case 7:
                    b[pos++] = '7';
                    break;
                case 8:
                    b[pos++] = '8';
                    break;
                case 9:
                    b[pos++] = '9';
                    break;
            }

            if (divisor == 1L) {
                break;
            } else {
                v -= (divisor * digit);
                if (v == 0) {
                    while (divisor > 1L) {
                        b[pos++] = '0';
                        divisor /= 10L;
                    }

                    break;
                }

                divisor /= 10L;
            }
        }

        return pos;
    }


    /**
     * Retrieves a hash code for this byte array.
     *
     * @return A hash code for this byte array.
     */
    @Override()
    public int hashCode() {
        int hashCode = 0;

        for (int i = 0; i < endPos; i++) {
            hashCode += array[i];
        }

        return hashCode;
    }


    /**
     * Indicates whether the provided object is a byte string buffer with contents
     * that are identical to that of this buffer.
     *
     * @param o The object for which to make the determination.
     * @return {@code true} if the provided object is considered equal to this
     * buffer, or {@code false} if not.
     */
    @Override()
    public boolean equals(@Nullable final Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ByteStringBuffer)) {
            return false;
        }

        final ByteStringBuffer b = (ByteStringBuffer) o;
        if (endPos != b.endPos) {
            return false;
        }

        for (int i = 0; i < endPos; i++) {
            if (array[i] != b.array[i]) {
                return false;
            }
        }

        return true;
    }


    /**
     * Creates a duplicate of this byte string buffer.  It will have identical
     * content but with a different backing array.  Changes to this byte string
     * buffer will not impact the duplicate, and vice-versa.
     *
     * @return A duplicate of this byte string buffer.
     */
    @NonNull()
    public ByteStringBuffer duplicate() {
        final ByteStringBuffer newBuffer = new ByteStringBuffer(endPos);
        return newBuffer.append(this);
    }


    /**
     * Retrieves a string representation of the contents for this buffer.
     *
     * @return A string representation of the contents for this buffer.
     */
    @Override()
    @NonNull()
    public String toString() {
        return Utf8s.toString(array, 0, endPos);
    }


    /**
     * Determines whether the specified character (Unicode code point)
     * is in the <a href="#BMP">Basic Multilingual Plane (BMP)</a>.
     * Such code points can be represented using a single {@code char}.
     *
     * @param codePoint the character (Unicode code point) to be tested
     * @return {@code true} if the specified code point is between
     * {@link Character#MIN_VALUE} and {@link Character#MAX_VALUE} inclusive;
     * {@code false} otherwise.
     */
    private static boolean isBmpCodePoint(int codePoint) {
        return codePoint >>> 16 == 0;
    }
}
