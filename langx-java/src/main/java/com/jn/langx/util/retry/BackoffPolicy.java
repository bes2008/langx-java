package com.jn.langx.util.retry;

import com.jn.langx.util.random.ThreadLocalRandom;

public class BackoffPolicy {
    public long getBackoffTime(RetryConfig config, int attempts) {
        return addJitter(config.getSleepInterval(), config.getJitter());
    }

    protected static long addJitter(long interval, float jitter) {
        long jitterInterval = (long) (interval * ThreadLocalRandom.current().nextFloat() * jitter);
        return interval + jitterInterval;
    }
}
