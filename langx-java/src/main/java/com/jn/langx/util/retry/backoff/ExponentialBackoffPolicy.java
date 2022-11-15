package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;

/**
 * 指数级
 */
public class ExponentialBackoffPolicy extends BackoffPolicy {
    public static ExponentialBackoffPolicy INSTANCE = new ExponentialBackoffPolicy();

    @Override
    public long getBackoffTimeInternal(RetryConfig config, int attempts) {
        long backoffTime = (long) (config.getTimeUnit().toMillis(config.getSleepInterval()) * Math.pow(2, attempts));
        return backoffTime;
    }
}