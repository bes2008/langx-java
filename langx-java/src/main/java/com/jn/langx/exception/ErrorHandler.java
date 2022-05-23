package com.jn.langx.exception;

import com.jn.langx.util.function.Handler;
import org.slf4j.Logger;

public interface ErrorHandler extends Handler<Throwable> {


    void handle(Throwable throwable);
}
