package com.jn.langx.classpath;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public class Classpaths {
    public static String getPath(@NonNull String path, boolean isClass) {
        return getPath(path, isClass, null);
    }


    public static String getPath(@NonNull String path, boolean isClass, String pathSeparator) {
        pathSeparator = pathSeparator == null ? "/" : pathSeparator;
        Preconditions.checkNotNull(path, "path is null or empty");
        if (path.endsWith(".class")) {
            path = path.substring(0, path.length() - ".class".length());
        }
        path = path.replace(".", pathSeparator);
        if (isClass && !path.endsWith(".class")) {
            path = path + ".class";
        }
        return path;
    }
}
