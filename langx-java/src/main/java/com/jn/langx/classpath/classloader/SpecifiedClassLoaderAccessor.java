package com.jn.langx.classpath.classloader;

public class SpecifiedClassLoaderAccessor extends ExceptionIgnoringAccessor {
    private ClassLoader classLoader;

    public SpecifiedClassLoaderAccessor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected ClassLoader doGetClassLoader() throws Throwable {
        return classLoader;
    }
}
