package com.jn.langx.util.retry;

public class RetryInfoImpl  implements RetryInfo {

    private final int attempt;
    private final int maxAttempts;
    private final long backoff; // mills

    public RetryInfoImpl(int attempt, int maxAttempts, long backoff) {
        this.attempt = attempt;
        this.maxAttempts = maxAttempts;
        this.backoff = backoff;
    }

    @Override
    public int getRetryCount() {
        return attempt - 1;
    }

    @Override
    public int getNumberOfAttempts() {
        return attempt;
    }

    @Override
    public int getMaxAttempts() {
        if (isInfiniteRetriesLeft()) {
            return Integer.MAX_VALUE;
        }
        return -1;
    }

    @Override
    public int getAttemptsLeft() {
        if (isInfiniteRetriesLeft()) {
            return Integer.MAX_VALUE;
        }
        return getMaxAttempts() - attempt;
    }

    @Override
    public boolean isInfiniteRetriesLeft() {
        return maxAttempts<=0;
    }

    @Override
    public long getBackoff() {
        return backoff;
    }

    @Override
    public boolean isLastAttempt() {
        if (isInfiniteRetriesLeft()) {
            return false;
        }
        return getMaxAttempts() - 1 == attempt;
    }

    @Override
    public boolean isFirstAttempt() {
        return attempt == 1;
    }
}