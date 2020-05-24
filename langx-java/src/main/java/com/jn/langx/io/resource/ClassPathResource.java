package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.file.Filenames;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.reflect.Reflects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * classpath:/xx/yy/zz
 * classpath:xx/yy/zz
 */
public class ClassPathResource extends AbstractLocatableResource<URL> {
    public static final String PREFIX = "classpath:";

    @Nullable
    private ClassLoader classLoader;

    @Nullable
    private Class<?> clazz;

    /**
     * Create a new {@code ClassPathResource} for {@code ClassLoader} usage.
     * A leading slash will be removed, as the ClassLoader resource access
     * methods will not accept it.
     * <p>The thread context class loader will be used for
     * loading the resource.
     *
     * @param path the absolute path within the class path
     * @see java.lang.ClassLoader#getResourceAsStream(String)
     */
    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    /**
     * Create a new {@code ClassPathResource} for {@code ClassLoader} usage.
     * A leading slash will be removed, as the ClassLoader resource access
     * methods will not accept it.
     *
     * @param path        the absolute path within the classpath
     * @param classLoader the class loader to load the resource with,
     *                    or {@code null} for the thread context class loader
     * @see ClassLoader#getResourceAsStream(String)
     */
    public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(path, "Path must not be null");
        this.classLoader = (classLoader != null ? classLoader : ClassLoaders.getDefaultClassLoader());

        path = Classpaths.getPath(path, path.endsWith(".class"));
        if (!path.startsWith(PREFIX)) {
            // classpath下的绝对路径
            if (path.startsWith("/")) {
                path = PREFIX + path.substring(1);
            } else {
                path = PREFIX + path;
            }
        }

        Preconditions.checkTrue(path.startsWith(PREFIX), "not a classpath resource");

        path = path.substring(PREFIX.length());

        String pathToUse = Filenames.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        setLocation(PREFIX, pathToUse);
    }

    /**
     * Create a new {@code ClassPathResource} for {@code Class} usage.
     * The path can be relative to the given class, or absolute within
     * the classpath via a leading slash.
     *
     * @param path  relative or absolute path within the class path
     * @param clazz the class to load resources with
     * @see java.lang.Class#getResourceAsStream
     */
    public ClassPathResource(String path, @Nullable Class<?> clazz) {
        Preconditions.checkNotNull(path, "Path must not be null");
        if (clazz == null) {
            this.classLoader = ClassLoaders.getDefaultClassLoader();
        }
        path = Classpaths.getPath(path, path.endsWith(".class"));
        if (!path.startsWith(PREFIX)) {
            // classpath下的绝对路径
            if (path.startsWith("/")) {
                path = PREFIX + path.substring(1);
            } else {
                // 相对于指定的类的路径
                if (clazz != null) {
                    String packageName = Reflects.getPackageName(clazz);
                    packageName = Classpaths.getPath(packageName, false);
                    if (path.startsWith(packageName)) {
                        path = PREFIX + path;
                    } else {
                        path = PREFIX + packageName + "/" + path;
                    }
                } else {
                    path = PREFIX + path;
                }
            }
        }

        Preconditions.checkTrue(path.startsWith(PREFIX), "not a classpath resource");

        path = path.substring(PREFIX.length());
        String pathToUse = Filenames.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        this.clazz = clazz;
        setLocation(PREFIX, pathToUse);
    }

    @Override
    public String getAbsolutePath() {
        return getPath();
    }

    @Override
    public URL getRealResource() {
        try {
            return getUrl();
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * This implementation returns a URL for the underlying class path resource,
     * if available.
     *
     * @see java.lang.ClassLoader#getResource(String)
     * @see java.lang.Class#getResource(String)
     */
    public URL getUrl() throws IOException {
        URL url = resolveURL();
        if (url == null) {
            throw new FileNotFoundException(toString() + " cannot be resolved to URL because it does not exist");
        }
        return url;
    }

    @Override
    public boolean exists() {
        try {
            getUrl();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    @Override
    public long contentLength() {
        try {
            return URLs.getContentLength(getUrl());
        } catch (IOException ex) {
            return -1;
        }
    }

    public final ClassLoader getClassLoader() {
        return (this.clazz != null ? this.clazz.getClassLoader() : this.classLoader);
    }

    protected URL resolveURL() {
        if (this.clazz != null) {
            return this.clazz.getResource(getPath());
        } else if (this.classLoader != null) {
            return this.classLoader.getResource(getPath());
        } else {
            return ClassLoader.getSystemResource(getPath());
        }
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.clazz != null) {
            is = this.clazz.getResourceAsStream(getPath());
        } else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(getPath());
        } else {
            is = ClassLoader.getSystemResourceAsStream(getPath());
        }
        if (is == null) {
            throw new FileNotFoundException(toString() + " cannot be opened because it does not exist");
        }
        return is;
    }

    @Override
    public String toString() {
        return getLocation().getLocation();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != ClassPathResource.class) {
            return false;
        }
        ClassPathResource o2 = (ClassPathResource) obj;

        if (!Objects.equals(this.getLocation(), o2.getLocation())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    public URL getUrlOrNull() {
        try {
            return getUrl();
        } catch (IOException e) {
            return null;
        }
    }
}
