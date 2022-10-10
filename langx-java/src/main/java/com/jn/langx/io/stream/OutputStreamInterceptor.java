package com.jn.langx.io.stream;

import java.io.OutputStream;

public abstract class OutputStreamInterceptor implements IOStreamInterceptor<OutputStream> {
    @Override
    public boolean beforeRead(OutputStream outputStream, final byte[] b, final int off, final int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean afterRead(OutputStream outputStream, final byte[] b, final int off, final int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public abstract boolean beforeWrite(OutputStream outputStream, final byte[] b, final int off, final int len);

    @Override
    public abstract boolean afterWrite(OutputStream outputStream, final byte[] b, final int off, final int len);
}
