package com.jn.langx.classpath.classloader;


import com.jn.langx.util.ClassLoaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Enables correct ClassLoader lookup in various environments (e.g. JEE Servers, etc).
 *
 * @since 1.2
 */
public class ClassResolvingObjectInputStream extends ObjectInputStream {

    public ClassResolvingObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    /**
     * Resolves an {@link ObjectStreamClass} by delegating to Shiro's
     * {@link com.jn.langx.util.ClassLoaders#forName(String)} utility method, which is known to work in all ClassLoader environments.
     *
     * @param osc the ObjectStreamClass to resolve the class name.
     * @return the discovered class
     * @throws IOException            never - declaration retained for subclass consistency
     * @throws ClassNotFoundException if the class could not be found in any known ClassLoader
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        try {
            return ClassLoaders.forName(osc.getName());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Unable to load ObjectStreamClass [" + osc + "]: ", e);
        }
    }
}
