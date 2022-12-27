package com.jn.langx.util.logging;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Loggers {

    public static void log(@NonNull Throwable ex) {
        log(1, null, null, ex, (Supplier<Object[], String>) null);
    }

    public static void log(@Nullable Logger logger, @Nullable Level level, final @Nullable Throwable ex, String message, Object... args) {
        log(logger, level, (Marker) null, ex, message, args);
    }

    public static void log(@Nullable Logger logger, @Nullable Level level, @Nullable Marker marker, final @Nullable Throwable ex, String message, Object... args) {
        log(1, logger, level, marker, ex, buildPlaceholderMessageSupplierOrNull(message, args), args);
    }

    public static void log(int count, @Nullable Logger logger, @Nullable Level level, final @Nullable Throwable ex, @Nullable String message, Object... args) {
        log(count, logger, level, ex, buildPlaceholderMessageSupplierOrNull(message, args), args);
    }

    public static void log(int count, @Nullable Logger logger, @Nullable Level level, final @Nullable Throwable ex, Supplier<Object[], String> messageSupplier, Object... args) {
        log(count, logger, level, null, ex, messageSupplier, args);
    }

    /**
     * 把消息记录到日志中
     *
     * @param logger          the logger，默认为 Throwables.logger
     * @param level           log level, 默认为 ERROR
     * @param messageSupplier 异常消息
     * @param ex              异常
     */
    public static void log(int count, @Nullable Logger logger, @Nullable Level level, @Nullable final Marker marker, final @Nullable Throwable ex, Supplier<Object[], String> messageSupplier, Object... args) {
        Preconditions.checkNotNull(messageSupplier, "the message supplier is null");
        if (logger == null) {
            if (ex == null) {
                logger = getLogger(Loggers.class);
            } else {
                logger = getLogger(Throwables.class);
            }
        }
        if (level == null) {
            if (ex == null) {
                level = Level.INFO;
            } else {
                level = Level.ERROR;
            }
        }
        if (count < 1) {
            count = 1;
        }
        if (count > 3) {
            count = 3;
        }

        final String message = messageSupplier.get(args);
        final Logger lgr = logger;
        switch (level) {
            case TRACE:
                if (logger.isTraceEnabled()) {
                    Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer index) {
                            if (marker != null) {
                                if (ex != null) {
                                    lgr.trace(marker, message, ex);
                                } else {
                                    lgr.trace(marker, message);
                                }
                            } else {
                                if (ex != null) {
                                    lgr.trace(message, ex);
                                } else {
                                    lgr.trace(message);
                                }
                            }
                        }
                    });

                }
                break;
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer index) {
                            if (marker != null) {
                                if (ex != null) {
                                    lgr.debug(marker, message, ex);
                                } else {
                                    lgr.debug(marker, message);
                                }
                            } else {
                                if (ex != null) {
                                    lgr.debug(message, ex);
                                } else {
                                    lgr.debug(message);
                                }
                            }
                        }
                    });
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer index) {
                            if (marker != null) {
                                if (ex != null) {
                                    lgr.info(marker, message, ex);
                                } else {
                                    lgr.info(marker, message);
                                }
                            } else {
                                if (ex != null) {
                                    lgr.info(message, ex);
                                } else {
                                    lgr.info(message);
                                }
                            }
                        }
                    });
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer index) {
                            if (marker != null) {
                                if (ex != null) {
                                    lgr.warn(marker, message, ex);
                                } else {
                                    lgr.warn(marker, message);
                                }
                            } else {
                                if (ex != null) {
                                    lgr.warn(message, ex);
                                } else {
                                    lgr.warn(message);
                                }
                            }
                        }
                    });
                }
                break;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                        @Override
                        public void accept(Integer index) {
                            if (marker != null) {
                                if (ex != null) {
                                    lgr.error(marker, message, ex);
                                } else {
                                    lgr.error(marker, message);
                                }
                            } else {
                                if (ex != null) {
                                    lgr.error(message, ex);
                                } else {
                                    lgr.error(message);
                                }
                            }
                        }
                    });
                }
                break;
            default:
                Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer index) {
                        if (marker != null) {
                            if (ex != null) {
                                lgr.warn(marker, message, ex);
                            } else {
                                lgr.warn(marker, message);
                            }
                        } else {
                            if (ex != null) {
                                lgr.warn(message, ex);
                            } else {
                                lgr.warn(message);
                            }
                        }
                    }
                });
                break;
        }
    }


    private static Supplier<Object[], String> buildPlaceholderMessageSupplierOrNull(final String message, Object args) {
        Supplier<Object[], String> supplier = null;
        if (Emptys.isNotEmpty(message)) {
            if (Emptys.isEmpty(args)) {
                supplier = new Supplier<Object[], String>() {
                    @Override
                    public String get(Object[] input) {
                        return message;
                    }
                };
            } else {
                supplier = new Supplier<Object[], String>() {
                    @Override
                    public String get(Object[] input) {
                        return StringTemplates.formatWithPlaceholder(message, input);
                    }
                };
            }
        }
        return supplier;
    }

    public static Logger getLogger(String loggerName) {
        return LoggerFactory.getLogger(loggerName);
    }

    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }


    public static boolean isEnabled(@NonNull Logger logger, @NonNull Level level) {
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(logger);

        boolean enabled = false;
        switch (level) {
            case TRACE:
                enabled = logger.isTraceEnabled();
                break;
            case DEBUG:
                enabled = logger.isDebugEnabled();
                break;
            case INFO:
                enabled = logger.isInfoEnabled();
                break;
            case WARN:
                enabled = logger.isWarnEnabled();
                break;
            case ERROR:
            default:
                enabled = logger.isErrorEnabled();
                break;
        }
        return enabled;
    }

    private static Logger doGetLogger(Object logger) {
        Logger lg = null;
        if (logger != null) {
            if (logger instanceof Logger) {
                lg = (Logger) logger;
            } else if (logger instanceof String) {
                lg = Loggers.getLogger((String) logger);
            } else if (logger instanceof Class) {
                lg = Loggers.getLogger((Class) logger);
            }

        }
        if (lg == null) {
            lg = Loggers.getLogger(Loggers.class);
        }
        return lg;
    }


    public static void debug(Object logger, String message, Object... parameters) {
        Logger lg = doGetLogger(logger);

        if (lg.isDebugEnabled()) {
            lg.debug(message, parameters);
        }
    }


    public static void info(Object logger, String message, Object... parameters) {
        Logger lg = doGetLogger(logger);

        if (lg.isInfoEnabled()) {
            lg.info(message, parameters);
        }
    }

    public static void warn(Object logger, String message, Object... parameters) {
        Logger lg = doGetLogger(logger);

        if (lg.isWarnEnabled()) {
            lg.warn(message, parameters);
        }
    }

    public static void error(Object logger, String message, Object... parameters) {
        Logger lg = doGetLogger(logger);

        if (lg.isErrorEnabled()) {
            lg.error(message, parameters);
        }
    }

}
