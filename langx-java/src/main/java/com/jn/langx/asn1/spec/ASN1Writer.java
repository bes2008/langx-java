package com.jn.langx.asn1.spec;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;


/**
 * This class provides an efficient mechanism for writing ASN.1 elements to
 * output streams.
 */
public final class ASN1Writer {
    /**
     * The thread-local buffers that will be used for encoding the elements.
     */
    @NonNull
    private static final ThreadLocal<ByteStringBuffer> BUFFERS = new ThreadLocal<ByteStringBuffer>();


    /**
     * The maximum amount of memory that will be used for a thread-local buffer.
     */
    private static final int MAX_BUFFER_LENGTH = 524288;


    /**
     * Prevent this class from being instantiated.
     */
    private ASN1Writer() {
        // No implementation is required.
    }


    /**
     * Writes an encoded representation of the provided ASN.1 element to the
     * given output stream.
     *
     * @param element      The ASN.1 element to be written.
     * @param outputStream The output stream to which the encoded representation
     *                     of the element should be written.
     * @throws IOException If a problem occurs while writing the element.
     */
    public static void writeElement(@NonNull final ASN1Element element,
                                    @NonNull final OutputStream outputStream)
            throws IOException {

        ByteStringBuffer buffer = BUFFERS.get();
        if (buffer == null) {
            buffer = new ByteStringBuffer();
            BUFFERS.set(buffer);
        }

        element.encodeTo(buffer);

        try {
            buffer.write(outputStream);
        } finally {
            if (buffer.capacity() > MAX_BUFFER_LENGTH) {
                buffer.setCapacity(MAX_BUFFER_LENGTH);
            }
            buffer.clear();
        }
    }


    /**
     * Appends an encoded representation of the provided ASN.1 element to the
     * given byte buffer.  When this method completes, the position will be at the
     * beginning of the written element, and the limit will be at the end.
     *
     * @param element The ASN.1 element to be written.
     * @param buffer  The buffer to which the element should be added.
     * @throws BufferOverflowException If the provided buffer does not have
     *                                 enough space between the position and
     *                                 the limit to hold the encoded element.
     */
    public static void writeElement(@NonNull final ASN1Element element,
                                    @NonNull final ByteBuffer buffer)
            throws BufferOverflowException {

        ByteStringBuffer b = BUFFERS.get();
        if (b == null) {
            b = new ByteStringBuffer();
            BUFFERS.set(b);
        }

        element.encodeTo(b);

        try {
            if (buffer.remaining() < b.length()) {
                throw new BufferOverflowException();
            }

            final int pos = buffer.position();
            buffer.put(b.getBackingArray(), 0, b.length());
            buffer.limit(buffer.position());
            buffer.position(pos);
        } finally {
            if (b.capacity() > MAX_BUFFER_LENGTH) {
                b.setCapacity(MAX_BUFFER_LENGTH);
            }
            b.clear();
        }
    }
}
