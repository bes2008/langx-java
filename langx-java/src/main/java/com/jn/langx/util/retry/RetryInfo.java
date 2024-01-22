package com.jn.langx.util.retry;

/**
 * 代表了单次尝试情况
 */
public class RetryInfo<R> {

    private int attempts;
    private final int maxAttempts;

    private final long startTime;  // mills

    private final long timeout; // mills

    // 当次try的backoff，-1代表还没有设置
    private long backoff=-1; // mills

    private Throwable error;
    private R result;

    public RetryInfo(int attempts, int maxAttempts, long startTime, long timeout) {
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.startTime = startTime;
        this.timeout= timeout;
    }
    public RetryInfo(int attempt, int maxAttempts, long backoff) {
        this(attempt,maxAttempts,System.currentTimeMillis(),0L);
        this.setBackoff(backoff);
    }

    public void setBackoff(long backoff) {
        this.backoff = backoff;
    }

    public int getRetryCount() {
        return attempts - 1;
    }

    public int getAttempts() {
        return attempts;
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
        return getMaxAttempts() - attempts;
    }

    private boolean isInfiniteRetriesLeft() {
        return maxAttempts <= 0;
    }

    public long getBackoff() {
        return backoff;
    }

    public boolean isLastAttempts() {
        if (isInfiniteRetriesLeft()) {
            return false;
        }
        return getMaxAttempts() - 1 == attempts;
    }

    public boolean isFirstAttempts() {
        return attempts == 1;
    }

    public RetryInfo<R> nextAttempts(){
        this.attempts++;
        this.backoff=-1;
        this.error=null;
        this.result=null;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
        this.result=null;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
        this.error=null;
    }

    public boolean hasError(){
        return this.error!=null;
    }
}