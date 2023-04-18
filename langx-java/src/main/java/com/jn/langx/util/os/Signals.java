package com.jn.langx.util.os;

import com.jn.langx.util.Objs;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.lang.reflect.*;

/**
 * 操作系统 signal 工具<br/>
 *
 * linux 支持的signal，可以用 kill -l 命令查看.
 * <pre>
 *  1) SIGHUP	    2) SIGINT	    3) SIGQUIT	    4) SIGILL	    5) SIGTRAP
 *  6) SIGABRT	    7) SIGBUS	    8) SIGFPE	    9) SIGKILL	    10) SIGUSR1
 * 11) SIGSEGV	    12) SIGUSR2	    13) SIGPIPE	    14) SIGALRM	    15) SIGTERM
 * 16) SIGSTKFLT	17) SIGCHLD	    18) SIGCONT	    19) SIGSTOP	    20) SIGTSTP
 * 21) SIGTTIN	    22) SIGTTOU	    23) SIGURG	    24) SIGXCPU	    25) SIGXFSZ
 * 26) SIGVTALRM	27) SIGPROF	    28) SIGWINCH	29) SIGIO	    30) SIGPWR
 * 31) SIGSYS	    34) SIGRTMIN	35) SIGRTMIN+1	36) SIGRTMIN+2	37) SIGRTMIN+3
 * 38) SIGRTMIN+4	39) SIGRTMIN+5	40) SIGRTMIN+6	41) SIGRTMIN+7	42) SIGRTMIN+8
 * 43) SIGRTMIN+9	44) SIGRTMIN+10	45) SIGRTMIN+11	46) SIGRTMIN+12	47) SIGRTMIN+13
 * 48) SIGRTMIN+14	49) SIGRTMIN+15	50) SIGRTMAX-14	51) SIGRTMAX-13	52) SIGRTMAX-12
 * 53) SIGRTMAX-11	54) SIGRTMAX-10	55) SIGRTMAX-9	56) SIGRTMAX-8	57) SIGRTMAX-7
 * 58) SIGRTMAX-6	59) SIGRTMAX-5	60) SIGRTMAX-4	61) SIGRTMAX-3	62) SIGRTMAX-2
 * 63) SIGRTMAX-1	64) SIGRTMAX
 * </pre>
 *
 *
 * https://blog.csdn.net/abc123lzf/article/details/101245167
 *
 */
public final class Signals {

    private Signals() {
    }

    /**
     * @param signalName    the signal, CONT, STOP, etc...
     * @param signalHandler the callback to run
     * @return an object that needs to be passed to the {@link #unregister(String, Object)}
     * method to unregister the handler
     */
    public static Object register(String signalName, Runnable signalHandler) {
        Objs.requireNonNull(signalHandler);
        return register(signalName, signalHandler, signalHandler.getClass().getClassLoader());
    }

    public static Object register(final String signalName, final Runnable handler, ClassLoader loader) {
        final Logger logger = Loggers.getLogger(Signals.class);
        try {
            final Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
            // Implement signal handler interface
            Object signalHandler = Proxy.newProxyInstance(loader,
                    new Class<?>[]{signalHandlerClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // only method we are proxying is handle()
                            if (method.getDeclaringClass() == Object.class) {
                                if ("toString".equals(method.getName())) {
                                    return handler.toString();
                                }
                            } else if (method.getDeclaringClass() == signalHandlerClass) {
                                logger.debug("Calling handler {} for signal {}", toStringx(handler), signalName);
                                handler.run();
                            }
                            return null;
                        }
                    });
            return doRegister(signalName, signalHandler);
        } catch (Exception e) {
            // Ignore this one too, if the above failed, the signal API is incompatible with what we're expecting
            logger.error("Error registering handler for signal {}  ", signalName, e);
            return null;
        }
    }

    public static Object registerDefault(String name) {
        try {
            Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
            return doRegister(name, signalHandlerClass.getField("SIG_DFL").get(null));
        } catch (Exception e) {
            final Logger logger = Loggers.getLogger(Signals.class);
            // Ignore this one too, if the above failed, the signal API is incompatible with what we're expecting
            logger.error("Error registering default handler for signal {} ", name, e);
            return null;
        }
    }

    public static void unregister(String name, Object previous) {
        try {
            // We should make sure the current signal is the one we registered
            if (previous != null) {
                doRegister(name, previous);
            }
        } catch (Exception e) {
            final Logger logger = Loggers.getLogger(Signals.class);
            // Ignore
            logger.error("Error unregistering handler for signal {}", name, e);
        }
    }

    private static Object doRegister(String name, Object handler) throws Exception {
        final Logger logger = Loggers.getLogger(Signals.class);
        logger.debug("Registering signal {} with handler ", toStringx(handler));
        Class<?> signalClass = Class.forName("sun.misc.Signal");
        Constructor<?> constructor = signalClass.getConstructor(String.class);
        Object signal;
        try {
            signal = constructor.newInstance(name);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                logger.debug("Ignoring unsupported signal {}", name);
            } else {
                logger.error("Error registering handler for signal {}", name, e);
            }
            return null;
        }
        Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
        return signalClass.getMethod("handle", signalClass, signalHandlerClass).invoke(null, signal, handler);
    }

    private static String toStringx(Object handler) {
        try {
            Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
            if (handler == signalHandlerClass.getField("SIG_DFL").get(null)) {
                return "SIG_DFL";
            }
            if (handler == signalHandlerClass.getField("SIG_IGN").get(null)) {
                return "SIG_IGN";
            }
        } catch (Exception t) {
            // ignore
        }
        return handler != null ? handler.toString() : "null";
    }

}
