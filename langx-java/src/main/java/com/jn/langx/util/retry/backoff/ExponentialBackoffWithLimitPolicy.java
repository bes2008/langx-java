package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.RetryConfig;

public class ExponentialBackoffWithLimitPolicy extends ExponentialBackoffPolicy {
    @Override
    public long getBackoffTime(RetryConfig config, int attempts) {
        long backoffTime = super.getBackoffTime(config, attempts);
        return config.getMaxSleepTime() > 0 ? Math.min(backoffTime, config.getMaxSleepTime()) : backoffTime;
    }
}
