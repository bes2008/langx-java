package com.jn.langx.io.stream.bandwidthlimit;

import com.jn.langx.io.stream.IOStreamPipeline;
import com.jn.langx.io.stream.WrappedOutputStream;

import java.io.OutputStream;

public class BandwidthLimitedOutputStream extends WrappedOutputStream {
    public BandwidthLimitedOutputStream(OutputStream out, BandwidthLimiter limiter) {
        super(out, IOStreamPipeline.of(new BandwidthLimitedOutputStreamInterceptor(limiter)));
    }
}
