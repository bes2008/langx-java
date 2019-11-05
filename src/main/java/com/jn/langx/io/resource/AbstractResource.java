package com.jn.langx.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public abstract class AbstractResource<E> implements Resource<E> {

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

}
