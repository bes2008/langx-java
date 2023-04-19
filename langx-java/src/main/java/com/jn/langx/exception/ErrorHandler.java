package com.jn.langx.exception;

import com.jn.langx.util.function.Handler;

public interface ErrorHandler extends Handler<Throwable> {


    void handle(Throwable throwable);
}
