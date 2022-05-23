package com.jn.langx.exception;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

public class ErrorHandlers {
    private ErrorHandlers() {
    }

    public static final ErrorHandler IGNORE_ERROR = getIgnoreErrorHandler(Loggers.getLogger(ErrorHandlers.class));

    public static ErrorHandler getIgnoreErrorHandler() {
        return IGNORE_ERROR;
    }

    public static ErrorHandler getIgnoreErrorHandler(Logger logger) {
        return new IgnoreErrorHandler(logger);
    }

    private static class IgnoreErrorHandler implements ErrorHandler {
        private Logger logger;

        private IgnoreErrorHandler(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void handle(Throwable throwable) {
            if (logger != null && logger.isErrorEnabled()) {
                logger.error(throwable.getMessage(), throwable);
            }
        }
    }
}
