package com.jn.langx.util.retry;

import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

public class Retryer {
    private RetryConfig config;
    private Predicate<Throwable> retryPredicate;
    private Consumer2<RetryInfo, Throwable> errorListener;

    public Retryer(RetryConfig config) {
        this(null, config);
    }

    public Retryer(Predicate retryPredicate, RetryConfig config) {
        this(retryPredicate, config, null);
    }

    public Retryer(Predicate<Throwable> retryPredicate, RetryConfig config, Consumer2<RetryInfo, Throwable> errorListener) {
        this.retryPredicate = retryPredicate == null ? Functions.<Throwable>truePredicate() : retryPredicate;
        this.errorListener = errorListener == null ? Functions.<RetryInfo, Throwable>noopConsumer2() : errorListener;
        this.config = config;
    }


    public static <R> R execute(Predicate<Throwable> retryPredicate, RetryConfig retryConfig, Consumer2<RetryInfo, Throwable> errorListener, Executable<R> executable, Object parameters) throws Exception {
        Retryer retryer = new Retryer(retryPredicate, retryConfig, errorListener);
        return retryer.executeWithRetry(executable, 1, parameters);
    }

    /**
     * 递归调用 retry
     */
    private <R> R executeWithRetry(final Executable<R> executable, final int attempt, final Object... parameters) throws Exception {
        try {
            return executable.execute(parameters);
        } catch (Throwable e) {
            if (waitAndRetry(attempt, e)) {
                return executeWithRetry(executable, attempt + 1, parameters);
            } else {
                if (e instanceof Error) {
                    throw new RuntimeException(e);
                }
                throw (Exception) e;
            }
        }
    }

    /**
     * @return 返回是否需要retry
     */
    private boolean waitAndRetry(int attempt, Throwable e) {
        if (!isExhausted(attempt, this.config.getMaxAttempts()) && this.retryPredicate.test(e)) {
            long backoffMillis = this.config.getBackoffPolicy().getBackoffTime(this.config, attempt);

            RetryInfo retryInfo = new RetryInfo(attempt, this.config.getMaxAttempts(), backoffMillis);
            this.errorListener.accept(retryInfo, e);
            try {
                if (backoffMillis > 0) {
                    Thread.sleep(backoffMillis);
                }
            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isExhausted(int attempt, int maxAttempts) {
        if (maxAttempts <= 0) {
            return false;
        }
        return attempt >= maxAttempts;
    }

    public static interface Executable<V> {
        V execute(Object... parameters) throws Exception;
    }
}

