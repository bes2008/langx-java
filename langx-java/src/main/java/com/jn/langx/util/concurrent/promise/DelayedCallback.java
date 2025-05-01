package com.jn.langx.util.concurrent.promise;


import com.jn.langx.util.function.Function;

public interface DelayedCallback<I, O> extends Function<I, O> {
    O apply(I lastResult);
}
