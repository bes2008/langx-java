package com.jn.langx.util.retry;


import com.jn.langx.util.retry.backoff.ExponentialBackoffWithLimitPolicy;

import java.util.concurrent.TimeUnit;

/**
 * @since 4.1.0
 */
public class RetryCounterFactory {
    private final RetryConfig retryConfig;

    public RetryCounterFactory(int sleepIntervalMillis) {
        this(Integer.MAX_VALUE, sleepIntervalMillis);
    }

    public RetryCounterFactory(int maxAttempts, int sleepIntervalMillis) {
        this(maxAttempts, sleepIntervalMillis, -1);
    }

    public RetryCounterFactory(int maxAttempts, int sleepIntervalMillis, int maxSleepTime) {
        this(new RetryConfig(
                maxAttempts,
                sleepIntervalMillis,
                maxSleepTime,
                TimeUnit.MILLISECONDS,
                new ExponentialBackoffWithLimitPolicy()));
    }

    public RetryCounterFactory(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    public RetryCounter create() {
        return new RetryCounter(retryConfig);
    }
}
