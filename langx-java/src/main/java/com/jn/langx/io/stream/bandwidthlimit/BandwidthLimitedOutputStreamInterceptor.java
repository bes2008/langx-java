package com.jn.langx.io.stream.bandwidthlimit;

import com.jn.langx.io.stream.OutputStreamInterceptor;

import java.io.OutputStream;

public class BandwidthLimitedOutputStreamInterceptor extends OutputStreamInterceptor {
    private BandwidthLimiter bandwidthLimiter;

    public BandwidthLimitedOutputStreamInterceptor(BandwidthLimiter limiter) {
        this.bandwidthLimiter = limiter;
    }

    @Override
    public boolean beforeWrite(OutputStream outputStream, byte[] b, int off, int len) {
        this.bandwidthLimiter.limitNextBytes(len);
        return true;
    }

    @Override
    public boolean afterWrite(OutputStream outputStream, byte[] b, int off, int len) {
        return true;
    }
}