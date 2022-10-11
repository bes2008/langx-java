package com.jn.langx.io.stream;

public interface IOStreamInterceptor<IOStream> {
    boolean beforeRead(IOStream stream, final byte[] b, final int off, final int len);

    boolean afterRead(IOStream stream, final byte[] b, final int off, final int len);

    boolean beforeWrite(IOStream stream, final byte[] b, final int off, final int len);

    boolean afterWrite(IOStream stream, final byte[] b, final int off, final int len);
}
