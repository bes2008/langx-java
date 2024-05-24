package com.jn.langx.util.retry;

public class ThreadSleepWaitStrategy implements WaitStrategy{
    @Override
    public void await(long mills) throws InterruptedException{
        Thread.sleep(mills);
    }
}
