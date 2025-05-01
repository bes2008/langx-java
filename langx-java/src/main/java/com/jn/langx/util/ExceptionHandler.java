package com.jn.langx.util;

import com.jn.langx.util.function.Handler;

public interface ExceptionHandler<E extends Throwable> extends Handler<E> {
    void handle(E e);
}
