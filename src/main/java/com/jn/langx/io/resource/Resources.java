package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.net.URLs;

import java.io.InputStream;

public class Resources {
    public static <V extends Resource> V loadResource(@NonNull String location) {
        return loadResource(location, null);
    }

    public static <V extends Resource> V loadResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }

    public static FileResource loadFileResource(@NonNull String location) {
        return loadFileResource(location, null);
    }

    public static FileResource loadFileResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, FileResource.PREFIX)) {
            location = FileResource.PREFIX + location;
        }
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }


    public static ClassPathResource loadClassPathResource(@NonNull String location) {
        return loadClassPathResource(location, (ClassLoader) null);
    }

    public static ClassPathResource loadClassPathResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, ClassPathResource.PREFIX)) {
            location = ClassPathResource.PREFIX + location;
        }
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }

    public static ClassPathResource loadClassPathResource(@NonNull String location, @Nullable Class clazz) {
        Preconditions.checkNotNull(location);
        if (!Strings.startsWith(location, ClassPathResource.PREFIX)) {
            location = ClassPathResource.PREFIX + location;
        }
        return new ClassPathResource(location, clazz);
    }

    public static UrlResource loadUrlResource(@NonNull String location) {
        return loadUrlResource(location, null);
    }

    public static UrlResource loadUrlResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        Preconditions.checkNotNull(URLs.newURL(location), StringTemplates.formatWithPlaceholder("location : {} not a URL", location));
        return new DefaultResourceLoader(classLoader).loadResource(location);
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
