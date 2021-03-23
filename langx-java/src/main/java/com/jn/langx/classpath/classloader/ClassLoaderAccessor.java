package com.jn.langx.classpath.classloader;

import java.io.InputStream;

public interface ClassLoaderAccessor {
    Class loadClass(String fqcn);

    InputStream getResourceStream(String name);
}
