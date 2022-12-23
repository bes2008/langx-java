package com.jn.langx.util.timing.clock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class CpuClock implements Clock{
    private static final ThreadMXBean THREAD_MX_BEAN = ManagementFactory.getThreadMXBean();

    @Override
    public long getTick() {
        return THREAD_MX_BEAN.getCurrentThreadCpuTime();
    }

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }
}
