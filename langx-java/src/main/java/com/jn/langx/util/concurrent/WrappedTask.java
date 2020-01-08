package com.jn.langx.util.concurrent;

import com.jn.langx.util.concurrent.interceptor.TaskInterceptorChain;

public abstract class WrappedTask<V> extends TaskInterceptorChain {

    protected V runInternal() throws Exception {
        V v;
        try {
            doBefore();
            v = run0();
        } catch (Throwable ex) {
            doError(ex);
            if (ex instanceof Exception) {
                throw (Exception) ex;
            } else {
                throw new RuntimeException(ex);
            }
        } finally {
            doAfter();
        }
        return v;
    }

    protected abstract V run0() throws Exception;
}
