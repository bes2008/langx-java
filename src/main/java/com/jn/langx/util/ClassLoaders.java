package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.reflect.Reflects;

public class ClassLoaders {

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

}
