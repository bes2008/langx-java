package com.jn.langx.util.retry;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.retry.backoff.ExponentialBackoffPolicy;

import java.util.concurrent.TimeUnit;

public class RetryConfig {
    /**
     * 最大尝试次数，正整数。如果 <=0 则代表次数限制
     */
    private int maxAttempts;

    // 最大时间。如果 <=0 ，则代表无时间限制
    private int timeout;

    // 首次开始之前的延迟
    private int delay;

    private long interval;
    // 单次等待的最大时间，它的值通常要大于sleepInterval
    private long maxSleepTime;
    private TimeUnit timeUnit;
    private BackoffPolicy backoffPolicy;
    private float jitter;

    public RetryConfig() {
        this(1, 1000L, -1L, null, null);
        jitter = 0.0f;
    }

    public RetryConfig(int maxAttempts,
                       long sleepInterval,
                       long maxSleepTime,
                       TimeUnit timeUnit,
                       BackoffPolicy backoffPolicy) {
        this(maxAttempts, -1, 0, sleepInterval, maxSleepTime, timeUnit, backoffPolicy);
    }

    public RetryConfig(int maxAttempts,
                       int timeout,
                       int delay,
                       long interval,
                       long maxSleepTime,
                       TimeUnit timeUnit,
                       BackoffPolicy backoffPolicy) {
        this.maxAttempts = maxAttempts<0 ? -1 : Maths.max(1,maxAttempts);
        this.timeout=Maths.max(0,timeout);
        this.delay= Maths.max(0,delay);
        this.timeUnit = timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
        this.interval = interval;
        this.maxSleepTime = maxSleepTime;
        this.backoffPolicy = backoffPolicy == null ? ExponentialBackoffPolicy.INSTANCE : backoffPolicy;
        if (this.timeout>0){
            Preconditions.checkArgument(this.timeout>this.delay, StringTemplates.formatWithPlaceholder("illegal args, timeout: {}, delay: {}", this.timeout, this.delay));
        }
    }

    public RetryConfig setBackoffPolicy(BackoffPolicy backoffPolicy) {
        this.backoffPolicy = backoffPolicy;
        return this;
    }

    public RetryConfig setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public RetryConfig setMaxSleepTime(long maxSleepTime) {
        this.maxSleepTime = maxSleepTime;
        return this;
    }

    public RetryConfig setSleepInterval(long sleepInterval) {
        this.interval = sleepInterval;
        return this;
    }

    public RetryConfig setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public RetryConfig setJitter(float jitter) {
        Preconditions.checkArgument(jitter >= 0.0f && jitter < 1.0f,
                "Invalid jitter: %s, should be in range [0.0, 1.0)", jitter);
        this.jitter = jitter;
        return this;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getMaxSleepTime() {
        return maxSleepTime;
    }

    public long getSleepInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public float getJitter() {
        return jitter;
    }

    public BackoffPolicy getBackoffPolicy() {
        return backoffPolicy;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public static RetryConfig noneRetryConfig() {
        RetryConfig config = new RetryConfig();
        config.setSleepInterval(-1L);
        return config;
    }

    public static RetryConfig buildInfiniteAttempts(long sleepInterval, long maxSleepTime, BackoffPolicy backoffPolicy) {
        return new RetryConfig(-1, sleepInterval, maxSleepTime, TimeUnit.MILLISECONDS, backoffPolicy);
    }
}
