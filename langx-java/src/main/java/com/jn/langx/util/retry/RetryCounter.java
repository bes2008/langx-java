package com.jn.langx.util.retry;

import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.retry.backoff.ExponentialBackoffPolicy;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Operation retry accounting.
 * Used to calculate wait period, {@link #getBackoffTimeAndIncrementAttempts()}}, or for performing
 * wait, {@link #sleepUntilNextRetry()}, in accordance with a {@link RetryConfig}, initial
 * settings, and a Retry Policy.
 * Like <a href=https://github.com/rholder/guava-retrying>guava-retrying</a>.
 *
 * @see RetryCounterFactory
 * @since 4.1.0
 */
public class RetryCounter {


    private RetryConfig retryConfig;
    private int attempts;

    public RetryCounter(int maxAttempts, long sleepInterval, TimeUnit timeUnit) {
        this(new RetryConfig(maxAttempts, sleepInterval, -1, timeUnit, new ExponentialBackoffPolicy()));
    }

    public RetryCounter(RetryConfig retryConfig) {
        this.attempts = 0;
        this.retryConfig = retryConfig;
    }

    /**
     * Sleep for a back off time as supplied by the backoff policy, and increases the attempts
     */
    public void sleepUntilNextRetry() throws InterruptedException {
        int attempts = getAttemptTimes();
        long sleepTime = getBackoffTime();
        Logger LOG = Loggers.getLogger(RetryCounter.class);
        LOG.trace("Sleeping {} ms before retry #{}...", sleepTime, attempts);
        retryConfig.getTimeUnit().sleep(sleepTime);
        useRetry();
    }

    public boolean shouldRetry() {
        return attempts < retryConfig.getMaxAttempts();
    }

    public void useRetry() {
        attempts++;
    }

    public boolean isRetry() {
        return attempts > 0;
    }

    public int   getAttemptTimes() {
        return attempts;
    }

    public long getBackoffTime() {
        return this.retryConfig.getBackoffPolicy().getBackoffTime(this.retryConfig, getAttemptTimes());
    }

    public long getBackoffTimeAndIncrementAttempts() {
        long backoffTime = getBackoffTime();
        useRetry();
        return backoffTime;
    }
}
