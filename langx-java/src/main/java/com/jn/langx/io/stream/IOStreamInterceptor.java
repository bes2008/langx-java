package com.jn.langx.io.stream;

public interface IOStreamInterceptor<Stream> {
    boolean beforeRead(Stream stream, final byte[] b, final int off, final int len);

    boolean afterRead(Stream stream, final byte[] b, final int off, final int len);

    boolean beforeWrite(Stream stream, final byte[] b, final int off, final int len);

    boolean afterWrite(Stream stream, final byte[] b, final int off, final int len);
}
