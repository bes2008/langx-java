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
        return retryer.executeWithRetry(executable, null, parameters);
    }

    /**
     * 递归调用 retry
     */
    public <R> R executeWithRetry(final Executable<R> executable, RetryInfo retryInfo, final Object... parameters) throws Exception {
        if (retryInfo==null || retryInfo.getAttempts()<1){
            retryInfo = new RetryInfo(1, this.config.getMaxAttempts(), System.currentTimeMillis(), this.config.getTimeUnit().toMillis(this.config.getTimeout()));
        }
        try {
            if(retryInfo.isFirstAttempts()){
                if(this.config.getDelay()>0){
                    this.config.getTimeUnit().sleep(this.config.getDelay());
                }
            }

            R r = executable.execute(parameters);
            return r;
        } catch (Throwable e) {
            if (waitAndJudgeRetry(retryInfo, e)) {
                return executeWithRetry(executable, retryInfo.nextAttempts(), parameters);
            } else {
                if (e instanceof Error) {
                    throw new RuntimeException(e);
                }
                throw (Exception) e;
            }
        }
    }

    private boolean waitAndJudgeRetry(RetryInfo retryInfo, Throwable e) {
        return waitAndJudgeRetry(retryInfo, this.config, retryPredicate, errorListener, null, e);
    }

    /**
     * @return 返回是否需要retry
     */
    private static <CTX> boolean waitAndJudgeRetry(RetryInfo retryInfo, RetryConfig retryConfig, @Nullable Predicate2<CTX, Throwable> retryPredicate, @Nullable Consumer3<RetryInfo, CTX, Throwable> errorListener, @Nullable CTX ctx, Throwable error) {

        if (!isExhausted(retryInfo.getAttempts(), retryConfig.getMaxAttempts())
                && !isExhaustedTimeout(retryInfo.getStartTime(), retryInfo.getTimeout())
                && (retryPredicate != null && retryPredicate.test(ctx, error))) {
            long backoffMillis = retryConfig.getBackoffPolicy().getBackoffTime(retryConfig, retryInfo.getAttempts());
            if (backoffMillis < 0) {
                throw new RuntimeException(StringTemplates.formatWithPlaceholder("invalid retry backoff: {}", backoffMillis));
            }

            if (errorListener != null) {
                retryInfo.setBackoff(backoffMillis);
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

    private static boolean isExhausted(int attempts, int maxAttempts) {
        if (maxAttempts <= 0) {
            // 无次数限制
            return false;
        }
        return attempts >= maxAttempts;
    }

    private static boolean isExhaustedTimeout(long startTime, long timeout) {
        if(timeout<=0){
            // 无时间限制
            return false;
        }
        if(startTime<=0){
            throw new RuntimeException("Illegal args, startTime: "+startTime);
        }
        return System.currentTimeMillis() > (startTime + timeout);
    }
}

