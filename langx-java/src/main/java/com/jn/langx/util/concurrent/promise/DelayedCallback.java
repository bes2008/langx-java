package com.jn.langx.util.concurrent.promise;


import com.jn.langx.util.function.Function;

public interface DelayedCallback<I, O> extends Function<I, O> {
    O apply(I lastResult);

    DelayedCallback NOOP = new DelayedCallback() {
        @Override
        public Object apply(Object lastResult) {
            return lastResult;
        }
    };

    DelayedCallback RETHROW = new DelayedCallback() {
        @Override
        public Object apply(Object lastResult) {
            if (lastResult instanceof Throwable) {
                if (lastResult instanceof RuntimeException) {
                    throw (RuntimeException) lastResult;
                }
                throw new RuntimeException((Throwable) lastResult);
            }
            return lastResult;
        }
    };
}
