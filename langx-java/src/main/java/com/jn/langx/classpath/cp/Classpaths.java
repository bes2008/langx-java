package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public class Classpaths {
    public static String getPath(@NonNull String path, boolean isClass) {
        Preconditions.checkNotNull(path, "path is null or empty");
        if (isClass) {
            path = path.replace('.', '/') + ".class";
        }
        return path;
    }
}
