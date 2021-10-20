package com.jn.langx.util.concurrent;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.os.Platform;

import java.util.concurrent.TimeUnit;

public class ThreadPoolProperties {

    private int coreThreads = Platform.cpuCore();
    private int maxThreads = 2 * this.coreThreads;
    private long keepAliveTime = 3;
    private TimeUnit unit = TimeUnit.SECONDS;
    private int queueSize = 100;

    public void setCoreThreads(int coreThreads) {
        Preconditions.checkArgument(coreThreads > 0);
        this.coreThreads = coreThreads;
    }

    public void setMaxThreads(int maxThreads) {
        Preconditions.checkArgument(coreThreads > 0);
        this.maxThreads = maxThreads;
    }

    public int getCoreThreads() {
        return coreThreads;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public ThreadPoolProperties() {
    }

    public ThreadPoolProperties(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit, int queueSize) {
        this.coreThreads = coreThreads;
        this.maxThreads = maxThreads;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.queueSize = queueSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}
