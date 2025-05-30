package com.jn.langx.util.retry;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
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

        if (!judgeRetryAndWait(retryInfo)) {
            // 因为有错或者因为没有backoff时，都要执行 fallback
            if(retryInfo.hasError() || retryInfo.getBackoff()<0){
                if(fallback!=null){
                    try {
                        return fallback.call();
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }
                if(retryInfo.hasError()) {
                    throw new RuntimeException(retryInfo.getError());
                }else{
                    throw new RuntimeException(StringTemplates.formatWithPlaceholder("invalid retry backoff: {}", retryInfo.getBackoff()));
                }
            }else {
                return retryInfo.getResult();
            }
        }
        else{
           return executeWithRetry(retryInfo.nextAttempts(),task, fallback);
        }
    }

    /**
     * @return 返回是否需要retry
     */
    private boolean judgeRetryAndWait(RetryInfo<R> retryInfo) {
        boolean needRetry=!isExhausted(retryInfo.getAttempts(), this.config.getMaxAttempts())
                && !isExhaustedTimeout(retryInfo.getStartTime(), retryInfo.getTimeout())
                && retryInfo.hasError() ? this.errorRetryPredicate.test(retryInfo.getError()) : this.resultRetryPredicate.test(retryInfo.getResult());

        if (needRetry) {
            long backoffMillis = this.config.getBackoffPolicy().getBackoffTime(this.config, retryInfo.getAttempts());
            retryInfo.setBackoff(backoffMillis);
            if (backoffMillis < 0) {
                return false;
            }
        }

        attemptsListener.accept(retryInfo);

        if(needRetry){
            try {
                if (retryInfo.getBackoff() > 0) {
                    waitStrategy.await(retryInfo.getBackoff());
                } else {
                    return false;
                }
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                if(retryInfo.hasError()){
                    throw new RuntimeException(retryInfo.getError());
                }
            }
        }
        return needRetry;
    }

    private static boolean isExhausted(int attempts, int maxAttempts) {
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
            throw new RuntimeException("Illegal args, startTime: " + startTime);
        }
        return System.currentTimeMillis() > (startTime + timeout);
    }

}

