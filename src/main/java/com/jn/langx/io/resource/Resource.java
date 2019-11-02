package com.jn.langx.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public interface Resource<E> {

    E getRealResource();
    /**
     * Return an {@link InputStream} for the content of an underlying resource.
     * <p>It is expected that each call creates a <i>fresh</i> stream.
     * <p>This requirement is particularly important when you consider an API such
     * as JavaMail, which needs to be able to read the stream multiple times when
     * creating mail attachments. For such a use case, it is <i>required</i>
     * that each {@code getInputStream()} call returns a fresh stream.
     *
     * @return the input stream for the underlying resource (must not be {@code null})
     * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
     * @throws IOException                   if the content stream could not be opened
     */
    InputStream getInputStream() throws IOException;


    /**
     * Determine whether this resource actually exists in physical form.
     * <p>This method performs a definitive existence check, whereas the
     * existence of a {@code Resource} handle only guarantees a valid
     * descriptor handle.
     */
    boolean exists();

    /**
     * Indicate whether non-empty contents of this resource can be read via
     * {@link #getInputStream()}.
     * <p>Will be {@code true} for typical resource descriptors that exist
     * since it strictly implies {@link #exists()} semantics as of 5.1.
     * Note that actual content reading may still fail when attempted.
     * However, a value of {@code false} is a definitive indication
     * that the resource content cannot be read.
     *
     * @see #getInputStream()
     * @see #exists()
     */
    boolean isReadable();

    /**
     * Return a {@link ReadableByteChannel}.
     * <p>It is expected that each call creates a <i>fresh</i> channel.
     * <p>The default implementation returns {@link Channels#newChannel(InputStream)}
     * with the result of {@link #getInputStream()}.
     *
     * @return the byte channel for the underlying resource (must not be {@code null})
     * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
     * @throws IOException                   if the content channel could not be opened
     * @see #getInputStream()
     */
    ReadableByteChannel readableChannel() throws IOException;

    /**
     * Determine the content length for this resource.
     *
     * @throws IOException if the resource cannot be resolved
     *                     (in the file system or as some other known physical resource type)
     */
    long contentLength() throws IOException;

    /**
     * Return a description for this resource,
     * to be used for error output when working with the resource.
     * <p>Implementations are also encouraged to return this value
     * from their {@code toString} method.
     *
     * @see Object#toString()
     */
    String getDescription();
}
