package com.jn.langx.classpath.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {
    private static final Logger log = LoggerFactory.getLogger(ExceptionIgnoringAccessor.class);

    public Class loadClass(String fqcn) {
        Class clazz = null;
        ClassLoader cl = getClassLoader();
        if (cl != null) {
            try {
                //SHIRO-767: Use Class.forName instead of cl.loadClass(), as byte arrays would fail otherwise.
                clazz = Class.forName(fqcn, false, cl);
            } catch (ClassNotFoundException e) {
                if (log.isTraceEnabled()) {
                    log.trace("Unable to load clazz named [" + fqcn + "] from class loader [" + cl + "]");
                }
            }
        }
        return clazz;
    }

    public InputStream getResourceStream(String name) {
        InputStream is = null;
        ClassLoader cl = getClassLoader();
        if (cl != null) {
            is = cl.getResourceAsStream(name);
        }
        return is;
    }

    protected final ClassLoader getClassLoader() {
        try {
            return doGetClassLoader();
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to acquire ClassLoader.", t);
            }
        }
        return null;
    }

    protected abstract ClassLoader doGetClassLoader() throws Throwable;
}