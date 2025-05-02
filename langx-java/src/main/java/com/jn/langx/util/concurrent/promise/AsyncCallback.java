package com.jn.langx.util.concurrent.promise;


import com.jn.langx.util.function.Function;

/**
 * 代表了一个要被异步执行的回调函数。
 * 它被包装为Subscriber持有的result handler，它用于处理上游Promise的运行结果，并产生新的结果，交给下游Promise
 *
 * @param <I> 上游数据
 * @param <O> 下游数据
 */
public interface AsyncCallback<I, O> extends Function<I, O> {
    O apply(I lastResult);

    AsyncCallback NOOP = new AsyncCallback() {
        @Override
        public Object apply(Object lastResult) {
            return lastResult;
        }
    };

    AsyncCallback REJECT = new AsyncCallback() {
        @Override
        public Object apply(Object lastResult) {
            return Promises.toRuntimeException(lastResult);
        }
    };
}
