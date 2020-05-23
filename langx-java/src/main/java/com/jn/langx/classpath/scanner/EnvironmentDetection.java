package com.jn.langx.classpath.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Detects whether certain features are available or not.
 */
public class EnvironmentDetection {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentDetection.class);

    /**
     * The ClassLoader to use.
     */
    private ClassLoader classLoader;

    /**
     * Creates a new FeatureDetector.
     *
     * @param classLoader The ClassLoader to use.
     */
    public EnvironmentDetection(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Flag indicating availability of JBoss VFS v2.
     */
    private Boolean jbossVFSv2;

    /**
     * Flag indicating availability of JBoss VFS v3.
     */
    private Boolean jbossVFSv3;

    /**
     * Flag indicating availability of the OSGi framework classes.
     */
    private Boolean osgi;

    /**
     * Checks whether JBoss VFS v2 is available.
     *
     * @return {@code true} if it is, {@code false if it is not}
     */
    public boolean isJBossVFSv2() {
        if (jbossVFSv2 == null) {
            jbossVFSv2 = isPresent("org.jboss.virtual.VFS", classLoader);
            logger.trace("... JBoss VFS v2 available: {}", jbossVFSv2);
        }

        return jbossVFSv2;
    }

    /**
     * Checks whether JBoss VFS is available.
     *
     * @return {@code true} if it is, {@code false if it is not}
     */
    public boolean isJBossVFSv3() {
        if (jbossVFSv3 == null) {
            jbossVFSv3 = isPresent("org.jboss.vfs.VFS", classLoader);
            logger.trace("... JBoss VFS v3 available: {}", jbossVFSv3);
        }

        return jbossVFSv3;
    }

    /**
     * Checks if OSGi framework is available.
     *
     * @return {@code true} if it is, {@code false if it is not}
     */
    public boolean isOsgi() {
        if (osgi == null) {
            osgi = isPresent("org.osgi.framework.Bundle", classLoader);
            logger.trace("... OSGi framework available: {}", osgi);
        }

        return osgi;
    }


    /**
     * Determine whether the {@link Class} identified by the supplied name is present
     * and can be loaded. Will return {@code false} if either the class or
     * one of its dependencies is not present or cannot be loaded.
     *
     * @param className   the name of the class to check
     * @param classLoader The ClassLoader to use.
     * @return whether the specified class is present
     */
    protected static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }
}
