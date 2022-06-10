package com.jn.langx.util.concurrent.async;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.concurrent.CommonTask;
import com.jn.langx.util.function.Consumer;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DefaultFuture<V> extends AbstractFuture<V> implements Callable<V>, Runnable {
    private CommonTask<V> task;
    private Future<V> future;
    private final List<GenericFutureListener<? extends GenericFuture<? super V>>> listeners = Collects.emptyArrayList();
    private boolean cancelable = true;

    /**
     * 失败时的 原因
     */
    private Throwable cause;
    private boolean success = false;
    private V expectedResult = null;

    public DefaultFuture(Callable task) {
        this.task = new CommonTask<V>(task);
    }

    public DefaultFuture(Runnable task) {
        this.task = new CommonTask<V>(task);
    }

    public void with(Future<V> future) {
        this.future = future;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public GenericFuture<V> addListener(GenericFutureListener<? extends GenericFuture<? super V>> listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public GenericFuture<V> addListeners(GenericFutureListener<? extends GenericFuture<? super V>>... listeners) {
        Collects.addAll(this.listeners, listeners);
        return this;
    }

    @Override
    public GenericFuture<V> removeListener(GenericFutureListener<? extends GenericFuture<? super V>> listener) {
        this.listeners.remove(listener);
        return this;
    }

    @Override
    public GenericFuture<V> removeListeners(GenericFutureListener<? extends GenericFuture<? super V>>... listeners) {
        Collects.removeAll(this.listeners, Collects.asList(listeners));
        return this;
    }

    @Override
    public GenericFuture<V> sync() throws InterruptedException {
        try {
            return await();
        } catch (InterruptedException ite) {
            throw ite;
        } catch (Throwable ex) {
            cause = ex;
        }
        throw new RuntimeException(cause);
    }

    @Override
    public GenericFuture<V> syncUninterruptibly() {
        try {
            return awaitUninterruptibly();
        } catch (Throwable ex) {
            cause = ex;
        }
        throw new RuntimeException(cause);
    }

    /**
     * 一直等，直到 完成，或者中断
     */
    @Override
    public GenericFuture<V> await() throws InterruptedException {
        await(-1);
        return this;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return await(unit.toMillis(timeout));
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        await(timeoutMillis, false);
        return this.isDone();
    }

    @Override
    public GenericFuture<V> awaitUninterruptibly() {
        awaitUninterruptibly(-1);
        return this;
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return awaitUninterruptibly(unit.toMillis(timeout));
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        try {
            await(timeoutMillis, true);
        } catch (InterruptedException e) {
            // ignore it
        }
        return this.isDone();
    }

    private boolean await(long timeoutMillis, boolean uninterruptible) throws InterruptedException {
        if (!isDone()) {
            long start = System.currentTimeMillis();
            long deadline = timeoutMillis < 0 ? Long.MAX_VALUE : (start + timeoutMillis);

            while (!isDone() && System.currentTimeMillis() < deadline) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ite) {
                    if (uninterruptible) {
                        throw ite;
                    } else {
                        // ignore
                    }
                }
            }
        }
        boolean completed = isDone();
        if(completed) {
            final DefaultFuture _this = this;
            Pipeline.<GenericFutureListener>of(this.listeners).forEach(new Consumer<GenericFutureListener>() {
                @Override
                public void accept(GenericFutureListener listener) {
                    listener.operationComplete(_this);
                }
            });
        }
        return completed;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (cancelable) {
            future.cancel(mayInterruptIfRunning);
            return future.isCancelled();
        }
        return false;
    }


    @Override
    public V call() {
        try {
            V r = this.task.call();
            this.success = true;
            return r;
        } catch (Throwable e) {
            this.cause = e;
            return null;
        }
    }

    @Override
    public void run() {
        try {
            this.task.run();
            this.success = true;
        } catch (Throwable e) {
            this.cause = e;
        }
    }

    @Override
    public V getNow() {

        if (isDone()) {
            try {
                return this.future.get(0, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                // ignore it;
            }
        }else{
            if (expectedResult != null) {
                return expectedResult;
            }
        }
        return null;
    }

    @Override
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public boolean isCancellable() {
        return this.cancelable;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    public void setExpectedResult(V expectedResult) {
        this.expectedResult = expectedResult;
    }

    public static <V> GenericFuture<V> submit(ExecutorService executorService, Runnable runnable, boolean cancelable, V result) {
        DefaultFuture<V> future = new DefaultFuture<V>(runnable);
        future.setExpectedResult(result);
        future.setCancelable(cancelable);
        Future<V> f = executorService.submit((Runnable) future, result);
        future.with(f);
        return future;
    }

    public static <V> GenericFuture<V> submit(ExecutorService executorService, Runnable runnable) {
        return submit(executorService, runnable, true);
    }

    public static <V> GenericFuture<V> submit(ExecutorService executorService, Runnable runnable, boolean cancelable) {
        return submit(executorService, runnable, cancelable, null);
    }

    public static <V> GenericFuture<V> submit(ExecutorService executorService, Callable<V> callable) {
        return submit(executorService, callable, true);
    }

    public static <V> GenericFuture<V> submit(ExecutorService executorService, Callable<V> callable, boolean cancelable) {
        DefaultFuture<V> future = new DefaultFuture<V>(callable);
        future.setCancelable(cancelable);
        Future<V> f = executorService.submit((Callable<V>) future);
        future.with(f);
        return future;
    }
}
