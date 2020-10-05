package com.jn.langx.util;

import com.jn.langx.util.function.Function;

/**
 * 这是对 Function<I,O>的扩展，用于支持当函数执行过程中出现任何错误，都会将异常转换为RuntimeException。
 *
 * @param <I>
 * @param <O>
 */
public abstract class ThrowableFunction<I, O> implements Function<I, O> {
    @Override
    public O apply(I i) {
        try {
            return doFun(i);
        } catch (Throwable ex) {
            throw com.jn.langx.util.Throwables.wrapAsRuntimeException(ex);
        }
    }

    public abstract O doFun(I i) throws Throwable;
}
