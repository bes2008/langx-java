package com.jn.langx.util.concurrent.interceptor;

import com.jn.langx.util.concurrent.TaskInterceptor;
import com.jn.langx.util.logging.Loggers;
@Deprecated
class LoggerInterceptor implements TaskInterceptor {


    @Override
    public void doBefore() {

    }

    @Override
    public void doAfter() {

    }

    @Override
    public void doError(Throwable ex) {
        Loggers.getLogger(getClass()).warn(ex.getMessage(), ex);
    }
}
