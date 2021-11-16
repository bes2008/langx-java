package com.jn.langx.util.os;

import com.jn.langx.util.Objs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

public final class Signals {
    private static final Logger logger = LoggerFactory.getLogger(Signals.class);

    private Signals() {
    }

    /**
     * @param name    the signal, CONT, STOP, etc...
     * @param handler the callback to run
     * @return an object that needs to be passed to the {@link #unregister(String, Object)}
     * method to unregister the handler
     */
    public static Object register(String name, Runnable handler) {
        Objs.requireNonNull(handler);
        return register(name, handler, handler.getClass().getClassLoader());
    }

    public static Object register(final String name, final Runnable handler, ClassLoader loader) {
        try {
            final Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
            // Implement signal handler
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
                                logger.debug("Calling handler {} for signal {}", toStringx(handler), name);
                                handler.run();
                            }
                            return null;
                        }
                    });
            return doRegister(name, signalHandler);
        } catch (Exception e) {
            // Ignore this one too, if the above failed, the signal API is incompatible with what we're expecting
            logger.debug("Error registering handler for signal ", name, e);
            return null;
        }
    }

    public static Object registerDefault(String name) {
        try {
            Class<?> signalHandlerClass = Class.forName("sun.misc.SignalHandler");
            return doRegister(name, signalHandlerClass.getField("SIG_DFL").get(null));
        } catch (Exception e) {
            // Ignore this one too, if the above failed, the signal API is incompatible with what we're expecting
            logger.debug("Error registering default handler for signal ", name, e);
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
            // Ignore
            logger.debug("Error unregistering handler for signal ", name, e);
        }
    }

    private static Object doRegister(String name, Object handler) throws Exception {
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
                logger.debug("Error registering handler for signal ", name, e);
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
        } catch (Throwable t) {
            // ignore
        }
        return handler != null ? handler.toString() : "null";
    }

}
