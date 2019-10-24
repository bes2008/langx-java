package com.jn.langx.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public abstract class AbstractResource implements Resource {
    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public boolean exists() {
        try {
            return getFile().exists();
        } catch (IOException ex) {
            // Fall back to stream existence: can we open the stream?
            try {
                getInputStream().close();
                return true;
            } catch (Throwable isEx) {
                return false;
            }
        }
    }


    @Override
    public URI getURI() throws IOException {
        return getFile().toURI();
    }

    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
