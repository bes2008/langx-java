package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 模拟 JavaScript 的 Promise涉及。
 * <p>
 * Promise 用于执行任务，并且将任务的执行结果传递给所有订阅者。
 */
public class Promise {
    /**
     * The task is in the initial state. 也代表了还没有运行完成
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

    private int state = STATE_PENDING;
    /**
     * 任务结果,  可以是任意对象
     * 当 state = PENDING 时, result = null
     * 当 state = FULFILLED 时, result = 任务返回的结果
     * 当 state = REJECTED 时, result = 任务抛出的异常
     */
    private Object result;
    private Executor executor;
    private Task task;
    private List<DelayedCallback> finallySubscribers;
    private LinkedBlockingDeque<ResultSubscriber> subscribers = new LinkedBlockingDeque<ResultSubscriber>();

    private Handler resolve = new Handler() {
        @Override
        public void handle(Object lastActionResult) {
            if (isSettled()) {
                return;
            }
            Promise.this.result = (lastActionResult);
            Promise.this.state = Promise.STATE_FULFILLED;
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
            Promise.this.state = Promise.STATE_REJECTED;
            notifySubscribers();
        }
    };

    private synchronized void notifySubscribers() {
        List<ResultSubscriber> list = new ArrayList<ResultSubscriber>();
        subscribers.drainTo(list);
        // 异步执行任务
        for (final ResultSubscriber resultSubscriber : list) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    resultSubscriber.handle();
                }
            });
        }
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
    private class ResultSubscriber implements Task {
        /**
         *
         */
        private DelayedCallback successCallback;
        private DelayedCallback errorCallback;

        private Promise outPromise;

        public ResultSubscriber(DelayedCallback successCallback, DelayedCallback errorCallback) {
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
            if (state == STATE_FULFILLED && successCallback != null) {
                newResult = successCallback.apply(result);
            } else if (state == STATE_REJECTED && errorCallback != null) {
                newResult = errorCallback.apply(result);
            }
            return newResult;
        }

    }


    /**
     * 已敲定（settled）
     */
    private boolean isSettled() {
        return state != STATE_PENDING;
    }

    public Promise(Executor executor, final Task task) {
        this(executor, task, false);
    }

    private Promise(Executor executor, final Task task, boolean asyncDelayed) {
        Preconditions.checkArgument(task != null);
        this.task = task;
        this.executor = executor;
        if (!asyncDelayed) {
            executeTask();
        }
    }

    private void executeTask() {
        try {
            Object result = task.run(resolve, reject);
            if (state == STATE_PENDING) {
                resolve.handle(result);
            }
        } catch (Throwable e) {
            reject.handle(e);
        }
    }

    /**
     * 以回调的方式，订阅当前Promise的运行结果。
     *
     * @param successCallback 订阅成功结果
     * @param errorCallback   订阅失败结果
     * @return Promise 返回新的Promise，是一个与订阅者强绑定的 Promise。
     */
    public Promise then(DelayedCallback successCallback, DelayedCallback errorCallback) {
        final ResultSubscriber subscriber = new ResultSubscriber(successCallback, errorCallback);
        final Promise outPromise = new Promise(executor, subscriber, true);
        subscriber.bindOutPromise(outPromise);

        if (!isSettled()) {
            subscribers.add(subscriber);
        } else {
            notifySubscribers();
        }

        return outPromise;

    }

    public Promise then(DelayedCallback successCallback) {
        return then(successCallback, null);
    }

    public Promise catchError(DelayedCallback errorCallback) {
        return then(null, errorCallback);
    }

}