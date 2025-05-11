package com.jn.langx.util.concurrent.promise;

import com.jn.langx.exception.ErrorHandler;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.concurrent.executor.ImmediateExecutor;
import com.jn.langx.util.function.Handler;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.counter.AtomicIntegerCounter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Promises {

    public static Promise of(Executor executor, final Object task) {
        if (task instanceof Promise) {
            return (Promise) task;
        }
        if (task instanceof Task) {
            return new Promise(executor, (Task) task);
        }
        if (task instanceof Runnable) {
            return new Promise(executor, new Task() {
                @Override
                public Object run(Handler resolve, ErrorHandler reject) {
                    // 想要代表失败，需要抛出一个异常
                    ((Runnable) task).run();
                    return null;
                }
            });
        }
        if (task instanceof Callable) {
            return new Promise(executor, new Task() {
                @Override
                public Object run(Handler resolve, ErrorHandler reject) {
                    try {
                        return ((Callable) task).call();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        if (task instanceof Throwable) {
            return new Promise(executor, new Task() {
                @Override
                public Object run(Handler resolve, ErrorHandler reject) {
                    Throwable reason = (Throwable) task;
                    reject.handle(reason);
                    throw null;
                }
            });
        }


        if (executor == null) {
            executor = ImmediateExecutor.INSTANCE;
        }
        // task 是 一个值，就判定为一个结果
        return new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, ErrorHandler reject) {
                Object result = task;
                resolve.handle(result);
                return result;
            }
        });
    }


    /**
     * 用于将所有的 dependency promises封装成一个Promise, 只有所有的 dependency promises都成功，才是一个成功的 Promise。
     * <pre>
     * 1. 这个Promise用于将所有的 dependency Promises都运行完，再将它们的结果整理成一个 List交给 订阅者。
     *   1.1 返回的结果List中元素的顺序，就是传入的promises的顺序。
     * 2. 如果这些 dependency promises 有一个是失败了，则这个Promise也会失败，并且是立即失败。那么其它的 promise即便是成功了，它们的结果也是被ignore了。
     * </pre>
     *
     * @param dependencyPromises 要并行完成的任务集，这些任务在创建时最好是 async，不然就失去了并行运行的效果。
     * @return Promise 以 List&lt;Object> 为结果的Promise。
     */
    public static <E> Promise<List<E>> all(Executor executor, final Promise... dependencyPromises) {
        return new Promise<List<E>>(executor, new Task() {
            @Override
            public Object run(Handler resolve, final ErrorHandler reject) {

                if (dependencyPromises.length == 0) {
                    return Collects.emptyArrayList();
                }
                final Object[] results = new Object[dependencyPromises.length];
                final CountDownLatch latch = new CountDownLatch(dependencyPromises.length);

                for (int i = 0; i < dependencyPromises.length; i++) {
                    Promise dependencyPromise = dependencyPromises[i];
                    final Holder<Integer> indexHolder = new Holder<Integer>(i);
                    dependencyPromise.then(new AsyncCallback<E, E>() {
                        @Override
                        public E apply(E lastResult) {
                            results[indexHolder.get()] = lastResult;
                            latch.countDown();
                            return lastResult;
                        }
                    }, new AsyncCallback<Throwable, E>() {
                        @Override
                        public E apply(Throwable lastResult) {
                            long count = latch.getCount();
                            while (count > 0) {
                                latch.countDown();
                                count--;
                            }
                            reject.handle(lastResult);
                            return null;
                        }
                    });
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    reject.handle(e);
                }
                return Collects.asList(results);
            }
        });
    }

    public static <E> Promise<List<E>> all(Executor executor, final Object... dependencyTasks) {
        return all(executor, Collects.asList(dependencyTasks));
    }

    public static <E> Promise<List<E>> all(Executor executor, final Iterable dependencyTasks) {
        List tasks = Lists.newArrayList(dependencyTasks);
        Promise[] promises = new Promise[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Promise promise = of(executor, tasks.get(i));
            promises[i] = promise;
        }

        return all(executor, promises);
    }

    /**
     * 用于对外的状态
     */
    public static class StatedResult<R> {
        private Promise.State state;
        private R result;

        StatedResult(Promise.State state, R result) {
            this.state = state;
            this.result = result;
        }

        public Promise.State getState() {
            return state;
        }

        public R getResult() {
            return result;
        }

        @Override
        public String toString() {
            return StringTemplates.formatWithPlaceholder("state: {}, {}: {}", state, state == Promise.State.REJECTED ? "reason" : "result", result);
        }
    }

    /**
     * 不管依赖的 promise 是成功还是失败，都会在它们完成时，将结果收集起来。
     *
     * @param dependencyPromises 依赖的任务
     * @return 以List&lt;StatedResult>为完成结果的 Promise
     */
    public static <E> Promise<List<StatedResult<E>>> allSettled(Executor executor, final Promise... dependencyPromises) {
        return new Promise<List<StatedResult<E>>>(executor, new Task() {
            @Override
            public Object run(Handler resolve, ErrorHandler reject) {
                if (dependencyPromises.length == 0) {
                    return Lists.newArrayList();
                }
                final StatedResult[] results = new StatedResult[dependencyPromises.length];
                final CountDownLatch latch = new CountDownLatch(dependencyPromises.length);
                for (int i = 0; i < dependencyPromises.length; i++) {
                    Promise dependencyPromise = dependencyPromises[i];
                    final Holder<Integer> indexHolder = new Holder<Integer>(i);
                    dependencyPromise.then(new AsyncCallback() {
                        @Override
                        public Object apply(Object lastResult) {
                            results[indexHolder.get()] = new StatedResult(Promise.State.FULFILLED, lastResult);
                            latch.countDown();
                            return null;
                        }
                    }, new AsyncCallback() {
                        @Override
                        public Object apply(Object lastResult) {
                            results[indexHolder.get()] = new StatedResult(Promise.State.REJECTED, lastResult);
                            latch.countDown();
                            return lastResult;
                        }
                    });
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    reject.handle(e);
                }
                return Lists.newArrayList(results);
            }
        });
    }

    public static <E> Promise<List<StatedResult<E>>> allSettled(Executor executor, final Object... dependencyTasks) {
        return allSettled(executor, Collects.asList(dependencyTasks));
    }

    public static <E> Promise<List<StatedResult<E>>> allSettled(Executor executor, final Iterable dependencyTasks) {
        List tasks = Lists.newArrayList(dependencyTasks);
        Promise[] promises = new Promise[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Promise promise = of(executor, tasks.get(i));
            promises[i] = promise;
        }

        return allSettled(executor, promises);
    }

    /**
     * 适用于多个task 竞赛的场景，只要有一个settled就行（不论它是成功还是失败）。
     */
    public static <R> Promise<R> race(Executor executor, final Promise... dependencyPromises) {
        return anySettled(executor, dependencyPromises);
    }

    /**
     * 适用于多个task 竞赛的场景，只要有一个settled就行（不论它是成功还是失败）。如果成功，则返回它的结果，如果失败，则返回它的原因。
     */
    public static <R> Promise<R> anySettled(Executor executor, final Promise... dependencyPromises) {
        return new Promise<R>(executor, new Task() {
            @Override
            public Object run(Handler resolve, ErrorHandler reject) {
                if (dependencyPromises.length == 0) {
                    return null;
                }

                final AtomicReference<R> result = new AtomicReference<R>();
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicBoolean isDone = new AtomicBoolean(false);
                for (int i = 0; i < dependencyPromises.length; i++) {
                    Promise dependencyPromise = dependencyPromises[i];
                    dependencyPromise.then(new AsyncCallback<R, R>() {
                        @Override
                        public R apply(R lastResult) {
                            if (!isDone.get()) {
                                result.set(lastResult);
                                latch.countDown();
                            }
                            return lastResult;
                        }
                    }, new AsyncCallback<R, R>() {
                        @Override
                        public R apply(R lastResult) {
                            if (!isDone.get()) {
                                result.set(lastResult);
                                latch.countDown();
                            }
                            return lastResult;
                        }
                    });
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    reject.handle(e);
                }
                return result.get();
            }
        });
    }

    public static <R> Promise<R> anySettled(Executor executor, final Object... dependencyTasks) {
        return anySettled(executor, Collects.asList(dependencyTasks));
    }

    public static <R> Promise<R> anySettled(Executor executor, final Iterable dependencyTasks) {
        List tasks = Lists.newArrayList(dependencyTasks);
        Promise[] promises = new Promise[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Promise promise = of(executor, tasks.get(i));
            promises[i] = promise;
        }

        return anySettled(executor, promises);
    }

    /**
     * 适用场景：只要有一个 dependency 是成功的就返回它的结果。如果都没有成功，则返回AggregateException。
     *
     * @param executor           执行器
     * @param dependencyPromises 依赖任务
     * @return 以第一个成功的结果为完成结果的 Promise
     */
    public static <R> Promise<R> any(Executor executor, final Promise... dependencyPromises) {
        return new Promise<R>(executor, new Task() {
            @Override
            public Object run(Handler resolve, ErrorHandler reject) {
                if (dependencyPromises.length == 0) {
                    return null;
                }
                final AtomicReference<R> result = new AtomicReference<R>();
                final AtomicBoolean anySuccess = new AtomicBoolean(false);
                final AtomicIntegerCounter allSettledCount = new AtomicIntegerCounter(0);
                final CountDownLatch anySuccessLatch = new CountDownLatch(1);


                final AggregateException aggregateException = new AggregateException();

                for (int i = 0; i < dependencyPromises.length; i++) {
                    Promise<R> dependencyPromise = dependencyPromises[i];
                    dependencyPromise.then(new AsyncCallback<R, R>() {
                        @Override
                        public R apply(R lastResult) {
                            if (!anySuccess.get()) {
                                result.set(lastResult);
                                anySuccess.set(true);
                                anySuccessLatch.countDown();
                            }
                            allSettledCount.increment();
                            return null;
                        }
                    }, new AsyncCallback<Throwable, R>() {
                        @Override
                        public R apply(Throwable ex) {
                            aggregateException.add(ex);
                            allSettledCount.increment();
                            throw Throwables.wrapAsRuntimeException(ex);
                        }
                    });
                }

                try {
                    // 最多等到所有的任务都 settled
                    while (allSettledCount.get() != dependencyPromises.length) {
                        anySuccessLatch.await(10, TimeUnit.MILLISECONDS);
                    }
                } catch (InterruptedException e) {
                    reject.handle(e);
                }
                if (anySuccess.get()) {
                    return result.get();
                } else {
                    return aggregateException;
                }
            }
        });
    }

    public static <R> Promise<R> any(Executor executor, final Object... dependencyTasks) {
        return any(executor, Collects.asList(dependencyTasks));
    }

    public static <R> Promise<R> any(Executor executor, final Iterable dependencyTasks) {
        List tasks = Lists.newArrayList(dependencyTasks);
        Promise[] promises = new Promise[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Promise promise = of(executor, tasks.get(i));
            promises[i] = promise;
        }

        return any(executor, promises);
    }

    public static Promise run(Runnable task) {
        return of(new ImmediateExecutor(), task);
    }

    public static <R> Promise<R> run(Callable<R> task) {
        return of(new ImmediateExecutor(), task);
    }

    public static <R> Promise<R> run(Task<R> task) {
        return of(new ImmediateExecutor(), task);
    }

    public static <R> Promise<R> run(final Supplier0<R> supplier) {
        return run(new Callable<R>() {
            @Override
            public R call() {
                return supplier.get();
            }
        });
    }

    public static Promise runAsync(Executor executor, Runnable task) {
        return of(executor, task);
    }

    public static <R> Promise<R> runAsync(Executor executor, Callable<R> task) {
        return of(executor, task);
    }

    public static <R> Promise<R> runAsync(Executor executor, Task<R> task) {
        return of(executor, task);
    }

    public static <R> Promise<R> runAsync(Executor executor, final Supplier0<R> supplier) {
        return runAsync(executor, new Callable<R>() {
            @Override
            public R call() {
                return supplier.get();
            }
        });
    }

    /**
     * 创建一个 resolved promise
     */
    public static <E> Promise<E> resolved(final E result) {
        return of(new ImmediateExecutor(), result);
    }

    /**
     * 创建一个 rejected promise
     */
    public static Promise rejected(final Throwable reason) {
        return new Promise(new Task<Object>() {
            @Override
            public Object run(Handler<Object> resolve, ErrorHandler reject) {
                reject.handle(reason);
                return null;
            }
        });
    }

    /**
     * 用于模拟 JavaScript中的async/await 中的 await 指令。等待该promise运行完成，获取结果。
     *
     * 当Promise执行成功，可以返回运行结果，当Promise执行失败，通常会抛出异常。
     *
     * @param promise promise对象
     * @param <R>     返回值类型
     * @return promise 运行的返回值
     */
    public static <R> R await(Promise promise) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Holder<StatedResult<R>> resultHolder = new Holder<StatedResult<R>>();
        promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                resultHolder.set(new StatedResult(Promise.State.FULFILLED, lastResult));
                latch.countDown();
                return lastResult;
            }
        }, new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                resultHolder.set(new StatedResult(Promise.State.REJECTED, lastResult));
                latch.countDown();
                return lastResult;
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (resultHolder.get().getState() == Promise.State.FULFILLED) {
            return resultHolder.get().getResult();
        } else {
            throw Throwables.wrapAsRuntimeException((Throwable) resultHolder.get().getResult());
        }
    }

    public static class AggregateException extends RuntimeException {
        public AggregateException() {
            super();
        }

        private List<Throwable> causes = Lists.newArrayList();

        public void add(Throwable cause) {
            causes.add(cause);
        }

        public Throwable getCause(int index) {
            return causes.get(index);
        }

        public int getCauseCount() {
            return causes.size();
        }

        public List<Throwable> getCauses() {
            return Collects.immutableArrayList(causes);
        }

    }

    static <R> AsyncCallback<? extends Throwable, R> newRejectCallback() {
        return new AsyncCallback<Throwable, R>() {
            @Override
            public R apply(Throwable reason) {
                throw Throwables.wrapAsRuntimeException(reason);
            }
        };
    }

    static AsyncCallback newNoopResolveCallback() {
        return new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return lastResult;
            }
        };
    }
}
