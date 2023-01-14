package com.jn.langx.util.retry;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.concurrent.Executable;
import com.jn.langx.util.function.Consumer3;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate2;

public class Retryer<CTX> {
    @NonNull
    private RetryConfig config;
    @NonNull
    private Predicate2<CTX, Throwable> retryPredicate;
    @NonNull
    private Consumer3<RetryInfo, CTX, Throwable> errorListener;


    public Retryer(RetryConfig config) {
        this(null, config);
    }

    public Retryer(Predicate2<CTX, Throwable> retryPredicate, RetryConfig config) {
        this(retryPredicate, config, null);
    }

    public Retryer(Predicate2<CTX, Throwable> retryPredicate, RetryConfig config, Consumer3<RetryInfo, CTX, Throwable> errorListener) {
        this.retryPredicate = retryPredicate == null ? Functions.<CTX, Throwable>truePredicate2() : retryPredicate;
        this.errorListener = errorListener == null ? new Consumer3<RetryInfo, CTX, Throwable>() {
            @Override
            public void accept(RetryInfo retryInfo, CTX ctx, Throwable throwable) {
                // noop
            }
        } : errorListener;
        this.config = config;
    }

    public static <CTX, R> R execute(Predicate2<CTX, Throwable> retryPredicate, RetryConfig retryConfig, Consumer3<RetryInfo, CTX, Throwable> errorListener, Executable<R> executable, Object parameters) throws Exception {
        Retryer<CTX> retryer = new Retryer<CTX>(retryPredicate, retryConfig, errorListener);
        return retryer.executeWithRetry(executable, 1, parameters);
    }

    /**
     * 递归调用 retry
     */
    public <R> R executeWithRetry(final Executable<R> executable, final int attempt, final Object... parameters) throws Exception {
        try {
            R r = executable.execute(parameters);
            return r;
        } catch (Throwable e) {
            if (waitAndJudgeRetry(attempt, e)) {
                return executeWithRetry(executable, attempt + 1, parameters);
            } else {
                if (e instanceof Error) {
                    throw new RuntimeException(e);
                }
                throw (Exception) e;
            }
        }
    }

    private boolean waitAndJudgeRetry(int attempt, Throwable e) {
        return waitAndJudgeRetry(attempt, this.config, retryPredicate, errorListener, null, e);
    }

    /**
     * @return 返回是否需要retry
     */
    public static <CTX> boolean waitAndJudgeRetry(int attempt, RetryConfig retryConfig, @Nullable Predicate2<CTX, Throwable> retryPredicate, @Nullable Consumer3<RetryInfo, CTX, Throwable> errorListener, @Nullable CTX ctx, Throwable error) {

        if (!isExhausted(attempt, retryConfig.getMaxAttempts()) && (retryPredicate != null && retryPredicate.test(ctx, error))) {
            long backoffMillis = retryConfig.getBackoffPolicy().getBackoffTime(retryConfig, attempt);
            if (backoffMillis < 0) {
                throw new RuntimeException(StringTemplates.formatWithPlaceholder("invalid retry backoff: {}", backoffMillis));
            }

            if (errorListener != null) {
                RetryInfo retryInfo = new RetryInfo(attempt, retryConfig.getMaxAttempts(), backoffMillis);
                errorListener.accept(retryInfo, ctx, error);
            }
            try {
                if (backoffMillis > 0) {
                    retryConfig.getTimeUnit().sleep(backoffMillis);
                } else {
                    return false;
                }

            } catch (InterruptedException interruptedException) {
                throw new RuntimeException(error);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isExhausted(int attempt, int maxAttempts) {
        if (maxAttempts <= 0) {
            // 无次数限制
            return false;
        }
        return attempt >= maxAttempts;
    }
}

