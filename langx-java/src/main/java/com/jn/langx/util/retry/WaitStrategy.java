package com.jn.langx.util.retry;

public interface WaitStrategy {
    void await(long mills) throws InterruptedException;
}
