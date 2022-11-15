package com.jn.langx.util.retry;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.random.ThreadLocalRandom;

public abstract class BackoffPolicy {

    public final long getBackoffTime(RetryConfig config, int attempts) {
        long backoffTime = getBackoffTimeInternal(config, attempts);
        Preconditions.checkTrue(backoffTime >= 0L, "invalid backoff");
        backoffTime = config.getTimeUnit().toMillis(backoffTime);
        backoffTime = addJitter(backoffTime, config.getJitter());
        long maxSleepTime = config.getMaxSleepTime() > 0 ? config.getTimeUnit().toMillis(config.getMaxSleepTime()) : -1L;
        backoffTime = maxSleepTime > 0 ? Math.min(backoffTime, config.getMaxSleepTime()) : backoffTime;
        return backoffTime;
    }

    protected long getBackoffTimeInternal(RetryConfig config, int attempts) {
        long backoffTime = config.getSleepInterval();
        return backoffTime;
    }

    private final long addJitter(long interval, float jitter) {
        long jitterInterval = (long) (interval * ThreadLocalRandom.current().nextFloat() * jitter);
        return interval + jitterInterval;
    }
}
