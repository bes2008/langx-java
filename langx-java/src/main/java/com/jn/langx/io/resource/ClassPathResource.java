package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.file.Filenames;
import com.jn.langx.util.io.file.Files;
import com.jn.langx.util.net.URLs;
import com.jn.langx.util.reflect.Reflects;

import java.io.File;
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

    private static ClassLoader globalClassLoader;

    public static void setGlobalClassLoader(ClassLoader classLoader) {
        globalClassLoader = classLoader;
    }

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
        String path0 = path;
        if (path.endsWith(".class")) {
            path = Classpaths.classNameToPath(path);
        }
        if (path.startsWith(PREFIX)) {
            path = path.substring(PREFIX.length());
        }
        if (!path.startsWith(PREFIX)) {
            // classpath下的绝对路径
            if (path.startsWith("/")) {
                path = PREFIX + path.substring(1);
            } else {
                path = PREFIX + path;
            }
        } else {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal path {}", path0));
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
        String path0 = path;
        if (clazz == null) {
            this.classLoader = ClassLoaders.getDefaultClassLoader();
        }
        if (path.endsWith(".class")) {
            path = Classpaths.classNameToPath(path);
        }

        if (path.startsWith(PREFIX)) {
            path = path.substring(PREFIX.length());
        }

        if (!path.startsWith(PREFIX)) {
            if (path.startsWith("/")) {
                // classpath下的绝对路径
                path = PREFIX + path.substring(1);
            } else {
                if (clazz != null) {
                    String packageName = Reflects.getPackageName(clazz);
                    packageName = Classpaths.packageToPath(packageName);
                    if (path.startsWith(packageName)) {
                        // 绝对路径
                        path = PREFIX + path;
                    } else {
                        // 相对路径
                        path = PREFIX + packageName + "/" + path;
                    }
                } else {
                    // 没有 指定类时，就认为这是个绝对路径
                    path = PREFIX + path;
                }
            }
        } else {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("illegal path {}", path0));
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
        URL url = getUrlOrNull();
        File file = null;
        if (url != null) {
            try {
                file = new File(url.toURI());
            } catch (Exception ex) {

            }
        }
        if (file != null) {
            return Files.getCanonicalPath(file);
        }
        if (url != null) {
            return url.toString();
        }
        return getLocation().getLocation();
    }

    @Override
    public URL getRealResource() {
        try {
            return getUrl();
        } catch (Exception ex) {
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
            return this.clazz.getResource("/" + getPath());
        } else if (this.classLoader != null) {
            return this.classLoader.getResource(getPath());
        } else {
            return ClassLoader.getSystemResource(getPath());
        }
    }

    public InputStream getInputStream() throws IOException {
        InputStream is = null;
        if (this.clazz != null) {
            String path = getPath();
            if (!path.startsWith("/")) {
                String packageName = Reflects.getPackageName(clazz);
                packageName = Classpaths.packageToPath(packageName);
                path = path.replace(packageName + "/", "");
            }
            try {
                is = this.clazz.getResourceAsStream(path);
            } catch (Exception ex) {
                // ignore
            }
            if (is == null) {
                is = this.clazz.getClassLoader().getResourceAsStream(getPath());
            }
        } else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(getPath());
        } else {
            is = ClassLoader.getSystemResourceAsStream(getPath());
        }
        if (is == null) {
            is = globalClassLoader.getResourceAsStream(getPath());
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
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ClassPathResource o2 = (ClassPathResource) obj;

        return Objs.equals(this.getLocation(), o2.getLocation());
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
