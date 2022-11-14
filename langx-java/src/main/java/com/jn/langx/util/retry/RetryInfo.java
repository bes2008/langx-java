package com.jn.langx.util.retry;

public class RetryInfo {

    private final int attempt;
    private final int maxAttempts;
    private final long backoff; // mills

    public RetryInfo(int attempt, int maxAttempts, long backoff) {
        this.attempt = attempt;
        this.maxAttempts = maxAttempts;
        this.backoff = backoff;
    }

    public int getRetryCount() {
        return attempt - 1;
    }

    public int getAttempts() {
        return attempt;
    }

    public int getMaxAttempts() {
        if (isInfiniteRetriesLeft()) {
            return Integer.MAX_VALUE;
        }
        return -1;
    }

    public int getAttemptsLeft() {
        if (isInfiniteRetriesLeft()) {
            return Integer.MAX_VALUE;
        }
        return getMaxAttempts() - attempt;
    }

    private boolean isInfiniteRetriesLeft() {
        return maxAttempts <= 0;
    }

    public long getBackoff() {
        return backoff;
    }

    public boolean isLastAttempt() {
        if (isInfiniteRetriesLeft()) {
            return false;
        }
        return getMaxAttempts() - 1 == attempt;
    }

    public boolean isFirstAttempt() {
        return attempt == 1;
    }
}