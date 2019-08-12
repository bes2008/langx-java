package com.jn.langx.util.concurrent.interceptor;

import com.jn.langx.util.concurrent.TaskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerInterceptor implements TaskInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public void doBefore() {

    }

    @Override
    public void doAfter() {

    }

    @Override
    public void doError(Throwable ex) {
        logger.warn(ex.getMessage(), ex);
    }
}
