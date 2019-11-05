package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.URLs;
import com.jn.langx.util.io.file.Filenames;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * classpath:/xx/yy/zz
 * classpath:xx/yy/zz
 */
public class ClassPathResource extends AbstractPathableResource<URL> {
    public static final String PATTERN = "classpath:";

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
        String pathToUse = Filenames.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        setPath(pathToUse);
        this.classLoader = (classLoader != null ? classLoader : ClassLoaders.getDefaultClassLoader());
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
        setPath(Filenames.cleanPath(path));
        this.clazz = clazz;
    }

    @Override
    public String getAbsolutePath() {
        return getPath();
    }

    @Override
    public URL getRealResource() {
        try {
            return getURL();
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
    public URL getURL() throws IOException {
        URL url = resolveURL();
        if (url == null) {
            throw new FileNotFoundException(toString() + " cannot be resolved to URL because it does not exist");
        }
        return url;
    }


    @Override
    public long contentLength() {
        try {
            return URLs.getContentLength(getURL());
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
}
