package com.jn.langx.util.concurrent;

public interface TaskInterceptor {
    void doBefore();
    void doAfter();
    void doError(Throwable ex);
}
