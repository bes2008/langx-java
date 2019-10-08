package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

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

    public static void log(@NonNull Throwable ex) {
        log(null, null, null, ex);
    }

    public static void log(@Nullable Logger logger, @Nullable Level level, @Nullable String message, @NonNull Throwable ex) {
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

    /**
     * JavaLangAccess class name to load using reflection
     */
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";

    /**
     * SharedSecrets class name to load using reflection
     */
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    private static final Object jla = getJLA();

    private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
        try {
            return Class.forName(JAVA_LANG_ACCESS_CLASSNAME, false, null).getMethod(name, parameterTypes);
        } catch (ThreadDeath death) {
            throw death;
        } catch (Throwable t) {
            /*
             * Either the JavaLangAccess class itself is not found, or the method is not supported on the
             * JVM.
             */
            return null;
        }
    }


    /**
     * Returns the JavaLangAccess class that is present in all Sun JDKs. It is not whitelisted for
     * AppEngine, and not present in non-Sun JDKs.
     */
    @Nullable
    private static Object getJLA() {
        try {
            /*
             * We load sun.misc.* classes using reflection since Android doesn't support these classes and
             * would result in compilation failure if we directly refer to these classes.
             */
            Class<?> sharedSecrets = Class.forName(SHARED_SECRETS_CLASSNAME, false, null);
            Method langAccess = sharedSecrets.getMethod("getJavaLangAccess");
            return langAccess.invoke(null);
        } catch (ThreadDeath death) {
            throw death;
        } catch (Throwable t) {
            /*
             * This is not one of AppEngine's whitelisted classes, so even in Sun JDKs, this can fail with
             * a NoClassDefFoundError. Other apps might deny access to sun.misc packages.
             */
            return null;
        }
    }

    /**
     * Returns the stack trace of {@code throwable}, possibly providing slower iteration over the full
     * trace but faster iteration over parts of the trace. Here, "slower" and "faster" are defined in
     * comparison to the normal way to access the stack trace, {@link Throwable#getStackTrace()
     * throwable.getStackTrace()}. Note, however, that this method's special implementation is not
     * available for all platforms and configurations. If that implementation is unavailable, this
     * method falls back to {@code getStackTrace}. Callers that require the special implementation can
     * check its availability with {@link #lazyStackTraceIsLazy()}.
     * <p>
     * <p>The expected (but not guaranteed) performance of the special implementation differs from
     * {@code getStackTrace} in one main way: The {@code lazyStackTrace} call itself returns quickly
     * by delaying the per-stack-frame work until each element is accessed. Roughly speaking:
     * <p>
     * <ul>
     * <li>{@code getStackTrace} takes {@code stackSize} time to return but then negligible time to
     * retrieve each element of the returned list.
     * <li>{@code lazyStackTrace} takes negligible time to return but then {@code 1/stackSize} time
     * to retrieve each element of the returned list (probably slightly more than {@code
     * 1/stackSize}).
     * </ul>
     * <p>
     * <p>Note: The special implementation does not respect calls to {@link Throwable#setStackTrace
     * throwable.setStackTrace}. Instead, it always reflects the original stack trace from the
     * exception's creation.
     */
    public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
        return lazyStackTraceIsLazy()
                ? jlaStackTrace(throwable)
                : unmodifiableList(asList(throwable.getStackTrace()));
    }

    /**
     * Returns whether {@link #lazyStackTrace} will use the special implementation described in its
     * documentation.
     */
    public static boolean lazyStackTraceIsLazy() {
        return getStackTraceElementMethod != null && getStackTraceDepthMethod != null;
    }

    @Nullable
    private static final Method getStackTraceElementMethod = (jla == null) ? null : getGetMethod();

    @Nullable
    private static final Method getStackTraceDepthMethod = (jla == null) ? null : getDepthMethod();


    private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
        Preconditions.checkNotNull(t);
        /*
         * AOSP grief.
         */
        return new AbstractList<StackTraceElement>() {
            @Override
            public StackTraceElement get(int n) {
                return (StackTraceElement)
                        invokeAccessibleNonThrowingMethod(getStackTraceElementMethod, jla, t, n);
            }

            @Override
            public int size() {
                return (Integer) invokeAccessibleNonThrowingMethod(getStackTraceDepthMethod, jla, t);
            }
        };
    }

    @Nullable
    private static Method getGetMethod() {
        return getJlaMethod("getStackTraceElement", Throwable.class, int.class);
    }

    @Nullable
    private static Method getDepthMethod() {
        return getJlaMethod("getStackTraceDepth", Throwable.class);
    }

    private static Object invokeAccessibleNonThrowingMethod(
            Method method, Object receiver, Object... params) {
        return Reflects.invoke(method, receiver, params, true, true);
    }

    public static <I, O> O ignoreThrowable(@Nullable Logger logger,
                                           @Nullable O valueIfError,
                                           @NonNull ThrowableFunction<I, O> func,
                                           @Nullable I input) {
        try {
            return func.apply(input);
        } catch (Throwable ex) {
            logger = logger == null ? Throwables.logger : logger;
            logger.error(ex.getMessage(), ex);
            return valueIfError;
        }
    }

    public static <I1, I2, O> O ignoreThrowable(@Nullable Logger logger,
                                                @Nullable O valueIfError,
                                                @NonNull ThrowableFunction2<I1, I2, O> func,
                                                @Nullable I1 input1,
                                                @Nullable I2 input2) {
        try {
            return func.apply(input1, input2);
        } catch (Throwable ex) {
            logger = logger == null ? Throwables.logger : logger;
            logger.error(ex.getMessage(), ex);
            return valueIfError;
        }
    }

    public static <I, O> O ignoreExceptions(@Nullable Logger logger,
                                            @Nullable O valueIfError,
                                            @NonNull List<Class<Throwable>> throwables,
                                            @NonNull ThrowableFunction<I, O> func,
                                            @Nullable I input) {
        try {
            return func.apply(input);
        } catch (Throwable ex) {
            final Class exClass = ex.getClass();
            if (Collects.noneMatch(throwables, new Predicate<Class<Throwable>>() {
                @Override
                public boolean test(Class<Throwable> exceptionClass) {
                    return exceptionClass.isAssignableFrom(exClass);
                }
            })) {
                logger = logger == null ? Throwables.logger : logger;
                logger.error(ex.getMessage(), ex);
            }
            return valueIfError;
        }
    }

    public static <I1, I2, O> O ignoreExceptions(@Nullable Logger logger,
                                                 @Nullable O valueIfError,
                                                 @NonNull List<Class<Throwable>> throwables,
                                                 @NonNull ThrowableFunction2<I1, I2, O> func,
                                                 @Nullable I1 input1,
                                                 @Nullable I2 input2) {
        try {
            return func.apply(input1, input2);
        } catch (Throwable ex) {
            final Class exClass = ex.getClass();
            if (Collects.noneMatch(throwables, new Predicate<Class<Throwable>>() {
                @Override
                public boolean test(Class<Throwable> exceptionClass) {
                    return exceptionClass.isAssignableFrom(exClass);
                }
            })) {
                logger = logger == null ? Throwables.logger : logger;
                logger.error(ex.getMessage(), ex);
            }
            return valueIfError;
        }
    }
}
