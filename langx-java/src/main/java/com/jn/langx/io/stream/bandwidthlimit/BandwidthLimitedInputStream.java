package com.jn.langx.io.stream.bandwidthlimit;

import com.jn.langx.io.stream.IOStreamPipeline;
import com.jn.langx.io.stream.WrappedInputStream;

import java.io.InputStream;

public class BandwidthLimitedInputStream extends WrappedInputStream {
    public BandwidthLimitedInputStream(InputStream in, BandwidthLimiter limiter) {
        super(in, IOStreamPipeline.of(new BandwidthLimitedInputStreamInterceptor(limiter)));
    }
}
