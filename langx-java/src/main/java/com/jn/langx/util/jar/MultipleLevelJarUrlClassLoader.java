package com.jn.langx.util.jar;

import com.jn.langx.util.jar.multiplelevel.JarFile;
import com.jn.langx.util.jar.multiplelevel.MultipleLevelURLStreamHandler;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

public class MultipleLevelJarUrlClassLoader extends URLClassLoader {

    static Method classLoader_RegisterAsParallelCapable_method;

    static {
        Method m = Reflects.getDeclaredMethod(ClassLoader.class, "registerAsParallelCapable");
        if (m != null) {
            classLoader_RegisterAsParallelCapable_method = m;
        }
    }

    /**
     * Create a new {@link MultipleLevelJarUrlClassLoader} instance.
     *
     * @param urls   the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     */
    public MultipleLevelJarUrlClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public URL findResource(String name) {
        MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(true);
        try {
            return super.findResource(name);
        } finally {
            MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(false);
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(true);
        try {
            return new UseFastConnectionExceptionsEnumeration(super.findResources(name));
        } finally {
            MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(false);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(true);
        try {
            try {
                definePackageIfNecessary(name);
            } catch (IllegalArgumentException ex) {
                // Tolerate race condition due to being parallel capable
                if (getPackage(name) == null) {
                    // This should never happen as the IllegalArgumentException indicates
                    // that the package has already been defined and, therefore,
                    // getPackage(name) should not return null.
                    throw new AssertionError("Package " + name + " has already been defined but it could not be found");
                }
            }
            return super.loadClass(name, resolve);
        } finally {
            MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(false);
        }
    }

    /**
     * Define a package before a {@code findClass} call is made. This is necessary to
     * ensure that the appropriate manifest for nested JARs is associated with the
     * package.
     *
     * @param className the class name being found
     */
    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (getPackage(packageName) == null) {
                try {
                    definePackage(className, packageName);
                } catch (IllegalArgumentException ex) {
                    // Tolerate race condition due to being parallel capable
                    if (getPackage(packageName) == null) {
                        // This should never happen as the IllegalArgumentException
                        // indicates that the package has already been defined and,
                        // therefore, getPackage(name) should not have returned null.
                        throw new AssertionError(
                                "Package " + packageName + " has already been defined but it could not be found");
                    }
                }
            }
        }
    }

    private void definePackage(final String className, final String packageName) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                @Override
                public Object run() throws Exception {
                    String packageEntryName = packageName.replace('.', '/') + "/";
                    String classEntryName = className.replace('.', '/') + ".class";
                    for (URL url : getURLs()) {
                        try {
                            URLConnection connection = URLs.openURL(url);
                            if (connection instanceof JarURLConnection) {
                                java.util.jar.JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                                if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null
                                        && jarFile.getManifest() != null) {
                                    definePackage(packageName, jarFile.getManifest(), url);
                                    return null;
                                }
                            }
                        } catch (IOException ex) {
                            // Ignore
                        }
                    }
                    return null;
                }
            }, AccessController.getContext());
        } catch (java.security.PrivilegedActionException ex) {
            // Ignore
        }
    }

    /**
     * Clear URL caches.
     */
    public void clearCache() {
        for (URL url : getURLs()) {
            try {
                URLConnection connection = URLs.openURL(url);
                if (connection instanceof java.net.JarURLConnection) {
                    clearCache(connection);
                }
            } catch (IOException ex) {
                // Ignore
            }
        }

    }

    private void clearCache(URLConnection connection) throws IOException {
        Object jarFile = ((JarURLConnection) connection).getJarFile();
        if (jarFile instanceof JarFile) {
            ((JarFile) jarFile).clearCache();
        }
    }

    private static class UseFastConnectionExceptionsEnumeration implements Enumeration<URL> {

        private final Enumeration<URL> delegate;

        UseFastConnectionExceptionsEnumeration(Enumeration<URL> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasMoreElements() {
            MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(true);
            try {
                return this.delegate.hasMoreElements();
            } finally {
                MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(false);
            }

        }

        @Override
        public URL nextElement() {
            MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(true);
            try {
                return this.delegate.nextElement();
            } finally {
                MultipleLevelURLStreamHandler.setUseFastConnectionExceptions(false);
            }
        }

    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
