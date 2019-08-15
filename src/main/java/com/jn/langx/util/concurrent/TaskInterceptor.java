package com.jn.langx.util.concurrent;

/**
 * @author jinuo.fang
 */
public interface TaskInterceptor {
    void doBefore();
    void doAfter();
    void doError(Throwable ex);
}
