package com.jn.langx.classpath.classloader;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.io.InputStream;

public abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

    public Class loadClass(String fqcn) {
        Class clazz = null;
        ClassLoader cl = getClassLoader();
        if (cl != null) {
            try {
                clazz = Class.forName(fqcn, false, cl);
            } catch (ClassNotFoundException e) {
                Logger log = Loggers.getLogger(ExceptionIgnoringAccessor.class);
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
            Logger log = Loggers.getLogger(ExceptionIgnoringAccessor.class);
            if (log.isDebugEnabled()) {
                log.debug("Unable to acquire ClassLoader.", t);
            }
        }
        return null;
    }

    protected abstract ClassLoader doGetClassLoader() throws Throwable;
}