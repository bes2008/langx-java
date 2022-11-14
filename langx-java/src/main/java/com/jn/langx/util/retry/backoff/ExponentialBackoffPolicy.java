package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;

public class ExponentialBackoffPolicy extends BackoffPolicy {
    @Override
    public long getBackoffTime(RetryConfig config, int attempts) {
        long backoffTime = (long) (config.getTimeUnit().toMillis(config.getSleepInterval()) * Math.pow(2, attempts));
        backoffTime = addJitter(backoffTime, config.getJitter());
        long maxSleepTime = config.getMaxSleepTime() > 0 ? config.getTimeUnit().toMillis(config.getMaxSleepTime()) : -1L;
        return maxSleepTime > 0 ? Math.min(backoffTime, config.getMaxSleepTime()) : backoffTime;
    }
}