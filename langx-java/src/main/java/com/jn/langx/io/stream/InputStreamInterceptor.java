package com.jn.langx.io.stream;

import java.io.InputStream;

public abstract class InputStreamInterceptor implements IOStreamInterceptor<InputStream> {
    @Override
    public abstract boolean beforeRead(InputStream inputStream, final byte[] b, final int off, final int len);

    @Override
    public abstract boolean afterRead(InputStream inputStream, final byte[] b, final int off, final int len);

    @Override
    public boolean beforeWrite(InputStream inputStream, final byte[] b, final int off, final int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean afterWrite(InputStream inputStream, final byte[] b, final int off, final int len) {
        throw new UnsupportedOperationException();
    }
}
