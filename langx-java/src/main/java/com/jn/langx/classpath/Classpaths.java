package com.jn.langx.classpath;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.file.Filenames;

public class Classpaths {

    public static String classNameToPath(String className) {
        Preconditions.checkNotNull(className, "className is null or empty");
        if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - ".class".length());
        }
        className = className.replace(".", "/");
        className = className + ".class";
        return className;
    }

    public static String packageToPath(String packageName) {
        return packageName.replace(".", "/");
    }

    public static String getCanonicalFilePath(String path) {
        return Filenames.cleanPath(path);
    }

}
