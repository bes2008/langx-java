package com.jn.langx.util.retry;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.*;

import java.util.concurrent.Callable;

public class Retryer<R> {
    @NonNull
    private RetryConfig config;

    // 一次执行完毕，拿到结果后，根据结果判断是否要retry
    @NonNull
    private Predicate<R> resultRetryPredicate;
    // 一次执行过程中，发生了error，根据error判断是否要retry
    @NonNull
    private Predicate<Throwable> errorRetryPredicate;
    @NonNull
    private Consumer<RetryInfo<R>> attemptsListener;

    @NonNull
    private WaitStrategy waitStrategy;

    public Retryer(RetryConfig config) {
        this(null, config);
    }

    public Retryer(Predicate<Throwable> retryPredicate, RetryConfig config) {
        this(retryPredicate, null, config, null);
    }

    public Retryer(Predicate<Throwable> errorRetryPredicate, Predicate<R> resultRetryPredicate, RetryConfig config, Consumer<RetryInfo<R>> attemptsListener) {
        this(errorRetryPredicate, resultRetryPredicate, config, attemptsListener, null);
    }

    public Retryer(Predicate<Throwable> errorRetryPredicate, Predicate<R> resultRetryPredicate, RetryConfig config, Consumer<RetryInfo<R>> attemptsListener, WaitStrategy waitStrategy) {
        this.errorRetryPredicate = errorRetryPredicate == null ? Functions.<Throwable>truePredicate() : errorRetryPredicate;
        this.resultRetryPredicate = resultRetryPredicate == null ? Functions.<R>falsePredicate() : resultRetryPredicate;
        this.attemptsListener = attemptsListener == null ? Functions.<RetryInfo<R>>noopConsumer() : attemptsListener;
        this.config = config;
        this.waitStrategy = waitStrategy==null ? new ThreadSleepWaitStrategy():waitStrategy;
    }

    public static <R> R execute(Predicate<Throwable> errorRetryPredicate, Predicate<R> resultRetryPredicate, RetryConfig retryConfig, Consumer<RetryInfo<R>> attemptsListener, Callable<R> task) throws Exception {
        return Retryer.execute(errorRetryPredicate, resultRetryPredicate, retryConfig, attemptsListener, task, null);
    }

    public static <R> R execute(Predicate<Throwable> errorRetryPredicate, Predicate<R> resultRetryPredicate, RetryConfig retryConfig, Consumer<RetryInfo<R>> attemptsListener, Callable<R> task, Callable<R> fallback) throws Exception {
        Retryer<R> retryer = new Retryer<R>(errorRetryPredicate, resultRetryPredicate, retryConfig, attemptsListener);
        return retryer.executeWithRetry(null, task, fallback);
    }

    public R execute(Callable<R> task) {
        return this.execute(task, null);
    }
    public R execute(Callable<R> task,Callable<R> fallback) {
        return executeWithRetry(null, task, fallback);
    }

    /**
     * 递归调用 retry
     */
    private R executeWithRetry(RetryInfo<R> retryInfo, final Callable<R> task, Callable<R> fallback) {
        if (retryInfo == null || retryInfo.getAttempts() < 1) {
            retryInfo = new RetryInfo<R>(1, this.config.getMaxAttempts(), System.currentTimeMillis(), this.config.getTimeUnit().toMillis(this.config.getTimeout()));
        }
        try {
            if (retryInfo.isFirstAttempts()) {
                if (this.config.getDelay() > 0) {
                    this.waitStrategy.await( this.config.getTimeUnit().toMillis(this.config.getDelay()));
                }
            }
            R r = task.call();
            retryInfo.setResult(r);
        }catch (Throwable e){
            retryInfo.setError(e);
        }

        boolean retryLimitExhausted = retryLimitExhausted(retryInfo);
        if (!retryLimitExhausted) { // 还有重试的机会
            boolean needRetry = judgeRetryWhenLimitNotExhausted(retryInfo);
            if (!needRetry) {
                return whenNotRetry(retryInfo, fallback);
            } else {
                // 进行等待
                needRetry = await(retryInfo);
                if (!needRetry) {
                    return whenNotRetry(retryInfo, fallback);
                }
            }
        } else {
            // 不能再尝试了
            return whenNotRetry(retryInfo, fallback);
        }

        return executeWithRetry(retryInfo.nextAttempts(), task, fallback);
    }

    private R whenNotRetry(RetryInfo<R> retryInfo, Callable<R> fallback) {
        if (retryInfo.hasError()) {
            // 做降级处理
            if (fallback != null) {
                try {
                    return fallback.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            throw new RuntimeException(retryInfo.getError());
        } else {
            // 不管对这个执行结果是否满意，都返回它
            return retryInfo.getResult();
        }
    }

    private boolean retryLimitExhausted(RetryInfo<R> retryInfo) {
        return isExhaustedAttempts(retryInfo.getAttempts(), this.config.getMaxAttempts())
                || isExhaustedTimeout(retryInfo.getStartTime(), retryInfo.getTimeout());
    }

    private boolean judgeRetryWhenLimitNotExhausted(RetryInfo<R> retryInfo) {
        boolean needRetry = retryInfo.hasError()
                ? this.errorRetryPredicate.test(retryInfo.getError())
                : this.resultRetryPredicate.test(retryInfo.getResult());
        return needRetry;
    }

    /**
     * @return 返回是否需要retry
     */
    private boolean await(RetryInfo<R> retryInfo) {
        // 计算要等待多久
        long backoffMillis = this.config.getBackoffPolicy().getBackoffTime(this.config, retryInfo.getAttempts());
        retryInfo.setBackoff(backoffMillis);
        // 没有可等待的时间了，不需要等待了
        if (backoffMillis <= 0) {
            return false;
        }

        attemptsListener.accept(retryInfo);
        try {
            waitStrategy.await(retryInfo.getBackoff());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            if (retryInfo.hasError()) {
                throw new RuntimeException(retryInfo.getError());
            }
        }
        return true;
    }

    private static boolean isExhaustedAttempts(int attempts, int maxAttempts) {
        if (maxAttempts <= 0) {
            // 无次数限制
            return false;
        }
        return attempts >= maxAttempts;
    }

    private static boolean isExhaustedTimeout(long startTime, long timeout) {
        if (timeout <= 0) {
            // 无时间限制
            return false;
        }
        if (startTime <= 0) {
            throw new RuntimeException("Illegal state, startTime: " + startTime);
        }
        return System.currentTimeMillis() > (startTime + timeout);
    }

}

