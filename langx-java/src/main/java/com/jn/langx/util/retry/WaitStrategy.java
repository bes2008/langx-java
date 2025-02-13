package com.jn.langx.util.retry;

/**
 * WaitStrategy接口定义了一个等待策略，用于在多线程环境下提供一种机制
 * 让线程等待特定条件的满足，常用于并发控制和线程同步场景
 */
public interface WaitStrategy {
    /**
     * 等待指定的时间
     *
     * @param mills 需要等待的时间，单位为毫秒
     * @throws InterruptedException 如果在等待过程中线程被中断，会抛出此异常
     */
    void await(long mills) throws InterruptedException;
}
