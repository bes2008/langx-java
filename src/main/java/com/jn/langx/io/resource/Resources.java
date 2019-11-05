package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.io.InputStream;

public class Resources {
    public static <V> Resource<V> loadResource(@NonNull String location) {
        return loadResource(location, null);
    }

    public static <V> Resource<V> loadResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        return new DefaultResourceLoader(classLoader).<V>loadResource(location);
    }

    public static <V> Resource<V> loadFileResource(@NonNull String location) {
        return loadFileResource(location, null);
    }

    public static <V> Resource<V> loadFileResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, FileResource.PATTERN)) {
            location = FileResource.PATTERN + location;
        }
        return new DefaultResourceLoader(classLoader).<V>loadResource(location);
    }


    public static <V> Resource<V> loadClassPathResource(@NonNull String location) {
        return loadClassPathResource(location, null);
    }

    public static <V> Resource<V> loadClassPathResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, ClassPathResource.PATTERN)) {
            location = ClassPathResource.PATTERN + location;
        }
        return new DefaultResourceLoader(classLoader).<V>loadResource(location);
    }


    public static ByteArrayResource asByteArrayResource(@NonNull byte[] byteArray) {
        return new ByteArrayResource(byteArray);
    }

    public static ByteArrayResource asByteArrayResource(@NonNull byte[] byteArray, @Nullable String description) {
        return new ByteArrayResource(byteArray, description);
    }

    public static InputStreamResource asInputStreamResource(@NonNull InputStream inputStream) {
        return new InputStreamResource(inputStream);
    }

    public static InputStreamResource asInputStreamResource(@NonNull InputStream inputStream, @Nullable String description) {
        return new InputStreamResource(inputStream, description);
    }
}
