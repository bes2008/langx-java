package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.URLs;

import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {
    protected ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassLoaders.getDefaultClassLoader();
    }

    /**
     * Create a new DefaultResourceLoader.
     * @param classLoader the ClassLoader to load class path resources with, or {@code null}
     * for using the thread context class loader at the time of actual resource access
     */
    public DefaultResourceLoader(@Nullable ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Resource loadResource(String location) {
        Preconditions.checkNotNull(location);
        if (location.startsWith(ClassPathResource.PATTERN)) {
            return new ClassPathResource(location);
        }
        if (location.startsWith(FileResource.PATTERN) && !location.startsWith(FileResource.FILE_URL_PATTERN)) {
            return new FileResource(location);
        }
        URL url = URLs.newURL(location);
        if (url != null) {
            return new UrlResource(url);
        }
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
