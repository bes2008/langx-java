package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.classpath.classloader.ClassLoaderAccessor;
import com.jn.langx.classpath.classloader.ExceptionIgnoringAccessor;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaders {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaders.class);

    public static Class loadClass(@NonNull String className) throws ClassNotFoundException {
        return loadClass(className, (ClassLoader) null);
    }

    public static Class loadClass(@NonNull String className, @Nullable Class basedClass) throws ClassNotFoundException {
        return loadClass(className, basedClass.getClassLoader());
    }

    public static Class loadClass(@NonNull String className, @Nullable ClassLoader classLoader) throws ClassNotFoundException {
        Class clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException ex) {
            // NOOP
        }
        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        if (clazz == null && currentThreadClassLoader != null) {
            clazz = Class.forName(className, true, currentThreadClassLoader);
        }
        if (clazz == null) {
            throw new ClassNotFoundException("Failed to load class" + className);
        }
        return clazz;
    }

    public static Class loadImplClass(@NonNull final String className, @Nullable ClassLoader classLoader, final Class superClass) throws ClassNotFoundException {
        Class clazz = loadClass(className, classLoader);
        if (!Reflects.isSubClass(superClass, clazz)) {
            final String error = "Class " + Reflects.getFQNClassName(clazz) + " is not cast to " + Reflects.getFQNClassName(superClass);
            throw new ClassCastException(error);
        }
        return clazz;
    }

    public static <I, O> O doAction(@NonNull ClassLoader threadContextClassLoader, @NonNull Function<I, O> action, @Nullable I input) {
        Preconditions.checkNotNull(threadContextClassLoader);
        Preconditions.checkNotNull(action);

        ClassLoader originalThreadCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(threadContextClassLoader);
        try {
            return action.apply(input);
        } finally {
            Thread.currentThread().setContextClassLoader(originalThreadCL);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaders.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static ClassLoader getSystemClassLoader() {
        try {
            return ClassLoader.getSystemClassLoader();
        } catch (final SecurityException se) {
            return null;
        }
    }

    public static boolean hasClass(@NonNull String classFQN, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(classFQN, "the calss name is null or empty");

        while (classFQN.endsWith(".class")) {
            classFQN = classFQN.substring(0, classFQN.length() - ".class".length());
        }

        Class c = null;
        try {
            c = loadClass(classFQN, classLoader);
        } catch (Throwable ex) {
            // ignore it
        }
        return c != null;
    }

    /**
     * @since 3.4.1
     */
    private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return Thread.currentThread().getContextClassLoader();
        }
    };

    /**
     * @since 3.4.1
     */
    private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ClassLoaders.class.getClassLoader();
        }
    };

    /**
     * @since 3.4.1
     */
    private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ClassLoader.getSystemClassLoader();
        }
    };

    /**
     * Attempts to load the specified class name from the current thread's
     * {@link Thread#getContextClassLoader() context class loader}, then the
     * current ClassLoader (<code>ClassUtils.class.getClassLoader()</code>), then the system/application
     * ClassLoader (<code>ClassLoader.getSystemClassLoader()</code>, in that order.  If any of them cannot locate
     * the specified class, an <code>UnknownClassException</code> is thrown (our RuntimeException equivalent of
     * the JRE's <code>ClassNotFoundException</code>.
     *
     * @param fqcn the fully qualified class name to load
     * @return the located class
     * @throws ClassNotFoundException if the class cannot be found.
     * @since 3.4.1
     */
    public static Class<?> forName(String fqcn) throws ClassNotFoundException {

        Class<?> clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);

        if (clazz == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Unable to load class named [" + fqcn +
                        "] from the thread context ClassLoader.  Trying the current ClassLoader...");
            }
            clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Unable to load class named [" + fqcn + "] from the current ClassLoader.  " +
                        "Trying the system/application ClassLoader...");
            }
            clazz = SYSTEM_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            clazz = Primitives.get(fqcn);
        }

        if (clazz == null) {
            String msg = "Unable to load class named [" + fqcn + "] from the thread context, current, or " +
                    "system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.";
            throw new ClassNotFoundException(msg);
        }

        return clazz;
    }

}
