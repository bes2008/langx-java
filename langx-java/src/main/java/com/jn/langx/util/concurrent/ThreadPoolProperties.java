package com.jn.langx.util.concurrent;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.os.Platform;

import java.util.concurrent.TimeUnit;

public class ThreadPoolProperties {

    private int coreSize = Platform.cpuCore();
    private int maxSize = 2 * this.coreSize;
    private long keepAliveTime = 3;
    private TimeUnit unit = TimeUnit.SECONDS;
    private int queueSize = 100;

    public void setCoreSize(int coreSize) {
        Preconditions.checkArgument(coreSize > 0);
        this.coreSize = coreSize;
    }

    public void setMaxSize(int maxSize) {
        Preconditions.checkArgument(coreSize > 0);
        this.maxSize = maxSize;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public int getMaxSize() {
        return maxSize;
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
        this.coreSize = coreThreads;
        this.maxSize = maxThreads;
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
