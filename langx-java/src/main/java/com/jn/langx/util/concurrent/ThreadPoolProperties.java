package com.jn.langx.util.concurrent;

import com.jn.langx.util.os.Platform;

import java.util.concurrent.TimeUnit;

public class ThreadPoolProperties {

    private int coreSize = Platform.cpuCore();
    private int maxSize = 2 * this.coreSize;
    private long keepAliveTime = 3;
    private TimeUnit unit = TimeUnit.SECONDS;

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
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
