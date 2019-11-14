package com.jn.langx.io.resource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.net.URLs;

import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {
    protected ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassLoaders.getDefaultClassLoader();
    }

    /**
     * Create a new DefaultResourceLoader.
     *
     * @param classLoader the ClassLoader to load class path resources with, or {@code null}
     *                    for using the thread context class loader at the time of actual resource access
     */
    public DefaultResourceLoader(@Nullable ClassLoader classLoader) {
        this.classLoader = Emptys.isNull(classLoader) ? ClassLoaders.getDefaultClassLoader() : classLoader;
    }

    @Override
    public <V extends Resource> V loadResource(String location) {
        Preconditions.checkNotNull(location);
        if (location.startsWith(ClassPathResource.PREFIX)) {
            return (V) new ClassPathResource(location, classLoader);
        }
        if (location.startsWith(FileResource.PREFIX) && !location.startsWith(FileResource.FILE_URL_PATTERN)) {
            return (V) new FileResource(location);
        }
        URL url = URLs.newURL(location);
        if (url != null) {
            return (V) new UrlResource(url);
        }
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
