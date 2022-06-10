package com.jn.langx.util.concurrent.async;

import com.jn.langx.util.collection.Collects;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DefaultFuture<V> extends AbstractFuture<V> {

    private Future<V> task;
    private final List<GenericFutureListener<? extends GenericFuture<? super V>>> listeners = Collects.emptyArrayList();
    private boolean cancelable = true;

    /**
     * 失败时的 原因
     */
    private Throwable cause;
    private boolean success = false;
    private V result = null;

    public DefaultFuture(Future<V> task) {
        this.task = task;
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

    private GenericFuture<V> await(long timeoutMillis, boolean uninterruptible) throws InterruptedException {
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
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        task.cancel(mayInterruptIfRunning);
        return task.isCancelled();
    }

    @Override
    public V getNow() {
        return this.result;
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
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

}
