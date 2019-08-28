package com.jn.langx.util;

import com.jn.langx.util.io.IOs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {
    private static final Logger logger = LoggerFactory.getLogger(Throwables.class);

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            IOs.close(printWriter);
        }
    }


    public static Throwable throwIfError(Throwable ex) {
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        return ex;
    }

    public static Throwable throwIfRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        return ex;
    }

    public static Throwable throwIfIOException(Throwable ex) throws IOException {
        if (ex instanceof IOException) {
            throw (IOException) ex;
        }
        return ex;
    }

    public static RuntimeException wrapAsRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        }
        return new RuntimeException(ex);
    }

    public static void throwAsRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new RuntimeException(ex);
    }

    public static Throwable getRootCause(Throwable ex) {
        while (ex.getCause() != null) {
            ex = ex.getCause();
        }
        return ex;
    }

    /**
     * step1 : get root cause
     * step2 : throwIfIOException(root cause)
     */
    public static Throwable throwRootCauseIfIOException(Throwable ex) throws IOException {
        return throwIfIOException(getRootCause(ex));
    }

    public static void log(Throwable ex) {
        log(null, null, null, ex);
    }

    public static void log(Logger logger, Level level, String message, Throwable ex) {
        Preconditions.checkNotNull(ex);
        message = Emptys.isEmpty(message) ? ex.getMessage() : message;
        logger = logger == null ? Throwables.logger : logger;
        level = level == null ? Level.ERROR : level;
        switch (level) {
            case TRACE:
                if (logger.isTraceEnabled()) {
                    logger.trace(message, ex);
                }
                break;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(message, ex);
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(message, ex);
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn(message, ex);
                }
                break;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(message, ex);
                }
                break;
            default:
                logger.warn(message, ex);
                break;
        }
    }
}
