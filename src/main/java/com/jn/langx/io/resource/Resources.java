package com.jn.langx.io.resource;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

public class Resources {
    public static <V> Resource<V> loadResource(@NonNull String location) {
        return loadResource(location, null);
    }

    public static <V> Resource<V> loadResource(@NonNull String location, @Nullable ClassLoader classLoader) {
        return new DefaultResourceLoader(classLoader).loadResource(location);
    }
}
