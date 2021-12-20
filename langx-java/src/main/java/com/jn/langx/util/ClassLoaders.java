package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.classpath.classloader.ClassLoaderAccessor;
import com.jn.langx.classpath.classloader.ExceptionIgnoringAccessor;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.jar.JarFile;

public class ClassLoaders {
    private ClassLoaders(){

    }

    public static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        } else {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return clazz.getClassLoader();
                }
            });
        }
    }

    public static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        }
    }

    public static ClassLoader getExtClassLoader() {
        return getSystemClassLoader().getParent();
    }

    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return ClassLoader.getSystemClassLoader();
                }
            });
        }
    }

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
    public static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return Thread.currentThread().getContextClassLoader();
        }
    };

    /**
     * @since 3.4.1
     */
    public static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ClassLoaders.class.getClassLoader();
        }
    };

    /**
     * @since 3.4.1
     */
    public static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
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
        Logger logger = Loggers.getLogger(ClassLoaders.class);
        if (clazz == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Unable to load class named [{}] from the thread context ClassLoader. Trying the current ClassLoader...", fqcn);
            }
            clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Unable to load class named [{}] from the current ClassLoader. Trying the system/application ClassLoader...", fqcn);
            }
            clazz = SYSTEM_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            clazz = Primitives.get(fqcn);
        }

        if (clazz == null) {
            String msg = StringTemplates.formatWithPlaceholder("Unable to load class named [{}] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.", fqcn);
            throw new ClassNotFoundException(msg);
        }

        return clazz;
    }

    /**
     * @since 3.4.1
     */
    public static boolean isAvailable(String fullyQualifiedClassName) {
        try {
            forName(fullyQualifiedClassName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @since 3.4.2
     */
    public static InputStream getResourceAsStream(String name) {

        InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);
        Logger logger = Loggers.getLogger(ClassLoaders.class);
        if (is == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resource [{}] was not found via the thread context ClassLoader.  Trying the current ClassLoader...", name);
            }
            is = CLASS_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resource [{}] was not found via the current class loader.  Trying the system/application ClassLoader...", name);
            }
            is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null && logger.isDebugEnabled()) {
            logger.debug("Resource [{}] was not found via the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.", name);
        }

        return is;
    }

    /**
     * key: Class<UrlClassLoader>
     *
     * @since 3.6.6
     */
    private static final Map<Class<?>, Method> addURLMethodMap = new NonAbsentHashMap<Class<?>, Method>(new Supplier<Class<?>, Method>() {
        @Override
        public Method get(Class<?> urlClassLoaderClass) {
            if (!Reflects.isSubClassOrEquals(URLClassLoader.class, urlClassLoaderClass)) {
                return null;
            }

            Method addURLMethod = Reflects.getAnyMethod(urlClassLoaderClass, "addURL", URL.class);
            if (addURLMethod != null) {
                addURLMethod.setAccessible(true);
            }
            return addURLMethod;
        }
    });

    /**
     * @param urlClassLoader a url class loader
     * @param jarUrl         the url for a jar
     * @since 3.6.6
     */
    public static boolean addUrl(URLClassLoader urlClassLoader, URL jarUrl) {
        return addUrl(urlClassLoader, jarUrl, false);
    }

    /**
     * @param urlClassLoader a url class loader
     * @param jarUrl         the url for a jar
     * @since 3.6.6
     */
    public static boolean addUrl(URLClassLoader urlClassLoader, URL jarUrl, boolean force) {
        if (urlClassLoader == null || jarUrl == null) {
            return false;
        }
        // 判断是否为 ext class loader
        if (ClassLoaders.getExtClassLoader() == urlClassLoader) {
            if (!force) {
                return false;
            }
        }

        Method addURLMethod = addURLMethodMap.get(urlClassLoader.getClass());

        if (addURLMethod != null) {
            try {
                Reflects.invoke(addURLMethod, urlClassLoader, new Object[]{jarUrl}, true, true);
            } catch (Throwable ex) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param klass
     * @return
     * @since 3.6.6
     */
    public static URL getJarUrl(Class<?> klass) {
        URL location = Reflects.getCodeLocation(klass);
        return location;
    }

    /**
     * @param klass
     * @return
     * @since 3.6.6
     */
    public static JarFile getJarFile(Class<?> klass) {
        URL location = Reflects.getCodeLocation(klass);
        if (location != null) {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(new File(location.toURI()));
                return jarFile;
            } catch (Throwable ex) {
                Logger logger = Loggers.getLogger(ClassLoaders.class);
                logger.warn("Can't find the jar for class: {}", Reflects.getFQNClassName(klass));
                IOs.close(jarFile);
            }
        }
        return null;
    }
}
