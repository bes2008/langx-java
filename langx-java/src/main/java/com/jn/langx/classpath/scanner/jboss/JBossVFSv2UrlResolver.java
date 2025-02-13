package com.jn.langx.classpath.scanner.jboss;

import com.jn.langx.classpath.ClasspathScanException;
import com.jn.langx.util.net.UrlResolver;
import com.jn.langx.util.reflect.Reflects;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Resolves JBoss VFS v2 URLs into standard Java URLs.
 */
public class JBossVFSv2UrlResolver implements UrlResolver {

    private static final Class<?> VFS_CLASS;
    private static final Class<?> VFS_UTILS_CLASS;
    private static final Class<?> VIRTUAL_FILE_CLASS;
    private static final Method GET_ROOT_METHOD;
    private static final Method GET_REAL_URL_METHOD;

    static {
        try {
            ClassLoader classLoader = JBossVFSv2UrlResolver.class.getClassLoader();
            VFS_CLASS = Class.forName("org.jboss.virtual.VFS", true, classLoader);
            VFS_UTILS_CLASS = Class.forName("org.jboss.virtual.VFSUtils", true, classLoader);
            VIRTUAL_FILE_CLASS = Class.forName("org.jboss.virtual.VirtualFile", true, classLoader);

            GET_ROOT_METHOD = VFS_CLASS.getMethod("getRoot", URL.class);
            GET_REAL_URL_METHOD = VFS_UTILS_CLASS.getMethod("getRealURL", VIRTUAL_FILE_CLASS);
        } catch (ClassNotFoundException e){
            throw new ExceptionInInitializerError(e);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public URL toStandardJavaUrl(URL url) throws IOException {
        if (url == null) {
            return null;
        }

        try {
            Object root = Reflects.invokeMethod(GET_ROOT_METHOD, null, url);
            if (root == null) {
                throw new IOException("Failed to get root from URL: " + url);
            }
            URL realUrl = Reflects.invokeMethod(GET_REAL_URL_METHOD, null, root);
            if (realUrl == null) {
                throw new IOException("Failed to get real URL from root: " + root);
            }
            return realUrl;
        } catch (RuntimeException e) {
            throw new ClasspathScanException("JBoss VFS v2 call failed", e);
        }
    }
}
