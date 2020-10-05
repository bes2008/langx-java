package com.jn.langx.util;

import com.jn.langx.util.function.Function2;

/**
 * 这是对 Function2<I1, I2, O>的扩展，用于支持当函数执行过程中出现任何错误，都会将异常转换为RuntimeException。
 *
 * @param <I1>
 * @param <I2>
 * @param <O>
 */
public abstract class ThrowableFunction2<I1, I2, O> implements Function2<I1, I2, O> {
    @Override
    public O apply(I1 i1, I2 i2) {
        try {
            return doFun2(i1, i2);
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public abstract O doFun2(I1 i1, I2 i2) throws Throwable;
}
