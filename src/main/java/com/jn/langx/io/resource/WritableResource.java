package com.jn.langx.io.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public interface WritableResource extends Resource {

    /**
     * Indicate whether the contents of this resource can be written
     * via {@link #getOutputStream()}.
     * <p>Will be {@code true} for typical resource descriptors;
     * note that actual content writing may still fail when attempted.
     * However, a value of {@code false} is a definitive indication
     * that the resource content cannot be modified.
     * @see #getOutputStream()
     * @see #isReadable()
     */
    boolean isWritable();

    /**
     * Return an {@link OutputStream} for the underlying resource,
     * allowing to (over-)write its content.
     * @throws IOException if the stream could not be opened
     * @see #getInputStream()
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Return a {@link WritableByteChannel}.
     * <p>It is expected that each call creates a <i>fresh</i> channel.
     * <p>The default implementation returns {@link Channels#newChannel(OutputStream)}
     * with the result of {@link #getOutputStream()}.
     * @return the byte channel for the underlying resource (must not be {@code null})
     * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
     * @throws IOException if the content channel could not be opened
     * @see #getOutputStream()
     */
    WritableByteChannel writableChannel() throws IOException ;
}
