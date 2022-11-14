package com.jn.langx.util.retry;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.retry.backoff.ExponentialBackoffPolicy;

import java.util.concurrent.TimeUnit;

public class RetryConfig {
    /**
     * 最大尝试次数，正整数。如果 <=0 则代表无限次数
     */
    private int maxAttempts;
    private long sleepInterval;
    private long maxSleepTime;
    private TimeUnit timeUnit;
    private BackoffPolicy backoffPolicy;
    private float jitter;

    private static final BackoffPolicy DEFAULT_BACKOFF_POLICY = new ExponentialBackoffPolicy();

    public RetryConfig() {
        this(1, 1000L, -1L, null, null);
        jitter = 0.0f;
    }

    public RetryConfig(int maxAttempts,
                       long sleepInterval,
                       long maxSleepTime,
                       TimeUnit timeUnit,
                       BackoffPolicy backoffPolicy) {
        this.maxAttempts = maxAttempts;
        this.timeUnit = timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
        this.sleepInterval = sleepInterval;
        this.maxSleepTime = maxSleepTime;
        this.backoffPolicy = backoffPolicy == null ? ExponentialBackoffPolicy.INSTANCE : backoffPolicy;
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
        this.sleepInterval = sleepInterval;
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
        return sleepInterval;
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

    public static RetryConfig noneRetryConfig() {
        RetryConfig config = new RetryConfig();
        config.setSleepInterval(-1L);
        return config;
    }

    public static RetryConfig buildInfiniteAttempts(long sleepInterval, long maxSleepTime, BackoffPolicy backoffPolicy) {
        return new RetryConfig(-1, sleepInterval, maxSleepTime, TimeUnit.MILLISECONDS, backoffPolicy);
    }
}
