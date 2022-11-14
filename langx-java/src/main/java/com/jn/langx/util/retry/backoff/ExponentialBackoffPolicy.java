package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;

public class ExponentialBackoffPolicy extends BackoffPolicy {
    @Override
    public long getBackoffTimeInternal(RetryConfig config, int attempts) {
        long backoffTime = (long) (config.getTimeUnit().toMillis(config.getSleepInterval()) * Math.pow(2, attempts));
        return backoffTime;
    }
}