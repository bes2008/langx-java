package com.jn.langx.util.concurrent.promise;

import com.jn.langx.Action;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.executor.ImmediateExecutor;
import com.jn.langx.util.function.Handler;
import com.jn.langx.util.struct.Holder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟 JavaScript中的Promise实现。
 * <p>
 * <pre>
 * Promise 的作用:
 *  1. 绑定一个task,并同步执行该 task，
 *  2. 然后将 task 的执行结果通知给所有的 subscribes
 * Promise 到底承诺了什么？
 *  1. 它承诺的是，无论结果是成功或者是失败，它都会通知所有的订阅者。
 *  2. 它承诺的是，无论订阅者在结果产生前，还是结果产生后进行的订阅，都会收到通知。
 * Promise 的状态：
 *  1. pending: 待定，任务正在运行中，没有运行完成，所以结果是待定的。
 *  2. fulfilled: 任务运行成功，有结果
 *  3. rejected: 任务运行失败，或者 task 运行过程 throw exception
 * Subscriber是什么？
 *  1. Subscriber 也被包装为一个Promise，它是一个Promise的订阅者，也叫 Subscriber
 *  2. 一个Subscriber是通过 Promise.then方法产生的，也就是then方法是用于注册订阅者的。
 *  3. Subscriber是一个异步任务，用于处理它订阅的Promise的运行结果。
 * Promise框架中的任务同步与异步：
 *  1. 通过new Promise(task)创建的Promise对象，JavaScript 中task是同步执行的，而这里实现的默认是异步的，可以通过参数指定为异步的。
 *  2. 它的订阅者(通过then方法创建的Promise)则都是异步执行的。
 * Promise与 Subscriber的关系：
 *  1. 一个Promise可以有多个订阅者。
 *  2. 一个Subscriber 同一时间只订阅一个 Promise。
 *  3. Subscriber 订阅的本质是 Promise的运行结果，当Promise的运行结果是一个新的Promise，则自动转移订阅关系，订阅新的Promise。
 *  4. Promise 与 Promise(Subscriber)产生订阅关系后，会自动形成 Promise Chain
 * Promise 的task 的执行结果的处理：
 *  1. 如果task执行成功，则调用resolve(result)
 *  2. 如果task执行失败，则调用reject(error)
 *  3. 如果task结果是 一个新的task，则 task会被包装成一个新的 Promise
 *  4. 如果task结果是一个新的Promise，则自动转移订阅关系，订阅新的Promise。
 *  5. 如果  Promise (subscriber) `A`在订阅时，没有指定 errorHandler，则error 会自动往`A`的下游的 Promise (subscriber) 传递
 * </pre>
 *
 * <pre>
 *                  ++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *                  +                   Promise                          +
 *                  ++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *                     /                        |                       \
 *           +++++++++++++++++++++++ +++++++++++++++++++++++ +++++++++++++++++++++++
 *           + Promise(Subscriber) + + Promise(Subscriber) + + Promise(Subscriber) +
 *           +++++++++++++++++++++++ +++++++++++++++++++++++ +++++++++++++++++++++++
 *            /         |       \               |               /         |       \
 *         Promise   Promise   Promise       Promise         Promise   Promise   Promise
 *                      |
 *                   Promise
 *                      |
 *                   Promise
 *            /         |       \    
 *         Promise   Promise   Promise
 *
 * </pre>
 *
 * 参考链接：
 * 1. <a href="https://javascript.info/async">JavaScript Promise</a>
 * </p>
 */
public class Promise {
    /**
     * The task is in the initial state. 也代表了还在运行中，没有运行完成
     */
    private static final int STATE_PENDING = 0;
    /**
     * Fulfilled: The task was completed successfully, and the result is available.
     */
    private static final int STATE_FULFILLED = 1;
    /**
     * Rejected: The task failed, and an error is provided.
     */
    private static final int STATE_REJECTED = 2;

    /**
     * The state of the promise.
     * Promise 只会有三个状态：pending, fulfilled, rejected
     */
    private AtomicInteger state = new AtomicInteger(STATE_PENDING);
    /**
     * 任务结果,  可以是任意对象
     * 当 state = PENDING 时, result = null
     * 当 state = FULFILLED 时, result = 任务返回的结果
     * 当 state = REJECTED 时, result = 任务抛出的异常
     */
    private Object result;
    private Executor executor;
    private Task task;
    private LinkedBlockingDeque<Subscriber> subscribers = new LinkedBlockingDeque<Subscriber>();

    private Handler resolve = new Handler() {
        @Override
        public void handle(Object lastActionResult) {
            if (isSettled()) {
                return;
            }
            Promise.this.result = (lastActionResult);
            Promise.this.state.set(Promise.STATE_FULFILLED);
            notifySubscribers();
        }
    };
    private Handler reject = new Handler() {
        @Override
        public void handle(Object lastActionException) {
            if (isSettled()) {
                return;
            }
            Promise.this.result = lastActionException;
            Promise.this.state.set(Promise.STATE_REJECTED);
            notifySubscribers();
        }
    };

    /**
     * 运行结果通知给所有订阅者
     */
    private synchronized void notifySubscribers() {
        List<Subscriber> list = new ArrayList<Subscriber>();
        subscribers.drainTo(list);
        // 异步执行任务
        for (final Subscriber subscriber : list) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    subscriber.handle();
                }
            });
        }
    }

    /**
     * 注册一个订阅者
     *
     * @param subscriber 订阅者
     */
    private void registerSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
        if (isSettled()) {
            notifySubscribers();
        }
    }


    /**
     * Promise是否已敲定（settled）
     */
    private boolean isSettled() {
        return state.get() != STATE_PENDING;
    }

    /**
     * 该方法用于完全模拟JavaScript Promise，task是同步执行的。
     *
     * @param task
     */
    public Promise(final Task task) {
        this(new ImmediateExecutor(), task, false);
    }

    /**
     * 创建一个异步执行的 Promise
     */
    public Promise(Executor executor, final Task task) {
        this(executor, task, true);
    }

    /**
     * 创建可自定义的是否异步执行的 Promise
     */
    public Promise(Executor executor, final Task task, boolean async) {
        this(executor, task, async, false);
    }

    /**
     * @param executor 任务执行器，如果为null，则使用当前线程执行任务，对于异步任务、订阅者时，它不能为null
     * @param task     promise绑定的 task
     * @param async    是否异步执行task
     * @param isSubscriber 是否是订阅者，如果是订阅者，则只会异步执行task
     */
    private Promise(Executor executor, final Task task, boolean async, boolean isSubscriber) {
        Preconditions.checkNotNull(executor, "executor is required");
        Preconditions.checkNotNull(task, "task is required");
        this.task = task;
        this.executor = executor;
        // 如果是订阅者，则只会异步执行task，并且不会立即提交任务到executor中，而是在上游 Promise通知时，才会提交到 executor中
        if (!isSubscriber) {
            if (async) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        executeTask();
                    }
                });
            } else {
                executeTask();
            }
        }
    }

    private void executeTask() {
        try {
            Object result = task.run(resolve, reject);
            if (result instanceof Task) {
                result = new Promise((Task) result);
            }
            if (result instanceof Promise) {
                Promise newInPromise = (Promise) result;
                if (!newInPromise.isSettled() && newInPromise.executor == null) {
                    newInPromise.executor = executor;
                }

                // 将当前Promise的订阅者，转交给新的Promise上。
                List<Subscriber> subscriberList = new ArrayList<Subscriber>();
                this.subscribers.drainTo(subscriberList);
                for (Subscriber subscriber : subscriberList) {
                    newInPromise.registerSubscriber(subscriber);
                }
            } else {
                if (!isSettled()) {
                    resolve.handle(result);
                }
            }
        } catch (Throwable e) {
            if (!isSettled()) {
                reject.handle(e);
            }
        }
    }

    /**
     * 用于订阅当前Promise，并创建一个新的Promise。新的Promise本质是一个订阅者。
     * 以回调的方式，订阅当前Promise的运行结果。
     *
     * @param successCallback 订阅成功结果
     * @param errorCallback   订阅失败结果
     * @return Promise 返回新的Promise，是一个与订阅者强绑定的 Promise。
     */
    public Promise then(@Nullable AsyncCallback successCallback, @Nullable AsyncCallback errorCallback) {
        if (successCallback == null) {
            successCallback = AsyncCallback.NOOP;
        }
        if (errorCallback == null) {
            errorCallback = AsyncCallback.RETHROW;
        }
        final Subscriber subscriber = new Subscriber(successCallback, errorCallback);
        final Promise outPromise = new Promise(executor, subscriber, true, true);
        subscriber.bindOutPromise(outPromise);

        registerSubscriber(subscriber);

        return outPromise;

    }


    public Promise then(AsyncCallback successCallback) {
        return then(successCallback, null);
    }

    public Promise catchError(AsyncCallback errorCallback) {
        return then(null, errorCallback);
    }

    public Promise finallyAction(final Action callback) {
        return then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                callback.doAction();
                return lastResult;
            }
        }, new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                callback.doAction();
                return RETHROW.apply(lastResult);
            }
        });
    }

    /**
     * 每一个 Subscriber 通过 successCallback, errorCallback 订阅了前一个Promise。
     * 同时关联了一个与它伴生的 Promise outPromise。
     * <p>
     * Subscriber 的作用是，当前一个Promise的运行结果（result）发生时，将结果通过 successCallback, errorCallback 来处理，并返回一个新结果。
     * 这个新结果会作为与它伴生的 Promise 绑定的结果。
     * <p>
     * Subscriber 是 一个 Task，是 outPromise 的任务。
     */
    private class Subscriber implements Task {
        /**
         *
         */
        private AsyncCallback successCallback;
        private AsyncCallback errorCallback;

        private Promise outPromise;

        public Subscriber(@NonNull AsyncCallback successCallback, @NonNull AsyncCallback errorCallback) {
            this.successCallback = successCallback;
            this.errorCallback = errorCallback;
        }

        private void bindOutPromise(Promise outPromise) {
            this.outPromise = outPromise;
        }

        public void handle() {
            outPromise.executeTask();
        }

        @Override
        public Object run(Handler resolve, Handler reject) {
            Object newResult = null;
            if (state.get() == STATE_FULFILLED && successCallback != null) {
                newResult = successCallback.apply(result);
            } else if (state.get() == STATE_REJECTED && errorCallback != null) {
                newResult = errorCallback.apply(result);
            }
            return newResult;
        }

    }


    public static Promise of(Executor executor, final Runnable task) {
        return new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                // 想要代表失败，需要抛出一个异常
                task.run();
                return "success";
            }
        });
    }

    public static Promise of(Executor executor, final Callable task) {
        return new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                try {
                    return task.call();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    /**
     * 用于将所有的 promises封装成一个Promise。
     * <pre>
     * 1. 这个Promise用于将所有的 Promises都运行完，再将它们的结果整理成一个 List交给 订阅者。
     *   1.1 返回的结果List中元素的顺序，就是传入的promises的顺序。
     * 2. 如果这些 promise有一个是失败了，则这个Promise也会失败，并且是立即失败。
     * </pre>
     *
     * @param promises 要并行完成的任务集，这些任务在创建时最好是 async，不然就失去了并行运行的效果。
     * @return Promise 封装的新的Promise
     */
    public static Promise all(final Promise... promises) {
        return new Promise(new Task() {
            @Override
            public Object run(Handler resolve, final Handler reject) {
                final List<Object> results = new ArrayList<Object>();
                // 先创建一个空的结果集，用于保存所有结果，避免后续出现 IndexOutOfBoundsException
                for (int i = 0; i < promises.length; i++) {
                    results.add(null);
                }

                for (int i = 0; i < promises.length; i++) {
                    Promise promise = promises[i];
                    final Holder<Integer> indexHolder = new Holder<Integer>(i);
                    promise.then(new AsyncCallback() {
                        @Override
                        public Object apply(Object lastResult) {
                            results.set(indexHolder.get(), lastResult);
                            return null;
                        }
                    }, new AsyncCallback() {
                        @Override
                        public Object apply(Object lastResult) {
                            reject.handle(lastResult);
                            return lastResult;
                        }
                    });
                }
                return results;
            }
        });
    }

    public static Promise all(Executor executor, final Task... tasks) {
        Promise[] promises = new Promise[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Task task = tasks[i];
            Promise promise = new Promise(executor, task);
            promises[i] = promise;
        }

        return all(promises);
    }

    public static Promise all(Executor executor, final Callable... tasks) {
        Promise[] promises = new Promise[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Promise promise = of(executor, tasks[i]);
            promises[i] = promise;
        }

        return all(promises);
    }

    public static Promise all(Executor executor, final Runnable... tasks) {
        Promise[] promises = new Promise[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Promise promise = of(executor, tasks[i]);
            promises[i] = promise;
        }

        return all(promises);
    }
}