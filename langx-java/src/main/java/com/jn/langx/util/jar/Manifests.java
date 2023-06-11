package com.jn.langx.util.jar;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@SuppressWarnings("JavaReflectionMemberAccess")
public class Manifests {
    private Manifests() {

    }

    public static Manifest loadManifest() {
        URL url = Reflects.getCodeLocation(Manifests.class);
        if (url != null) {
            if (url.getPath().endsWith("target/classes/")) {
                return null;
            }
            String jarPath = url.getPath();
            if (jarPath.startsWith("file:")) {
                jarPath = jarPath.replaceFirst("file:", "");
            }
            return loadManifest(jarPath);
        }
        return null;
    }


    public static Manifest loadManifest(@Nullable String jarPath) {
        if (jarPath == null) {
            return null;
        }

        String path0 = jarPath;

        String[] pathSegments = jarPath.split("!/");
        if (!Emptys.isEmpty(pathSegments)) {
            for (String segment : pathSegments) {
                if (!Emptys.isEmpty(segment)) {
                    jarPath = segment;
                    break;
                }
            }
        }

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            return jarFile.getManifest();
        } catch (Throwable ex) {
            Logger logger = Loggers.getLogger(Manifests.class);
            logger.warn("Can't find the jar : {}", path0);
            return null;
        } finally {
            IOs.close(jarFile);
        }
    }

    public static Manifest getManifest(Class<?> klass) {
        JarFile jarFile = null;
        try {
            jarFile = ClassLoaders.getJarFile(klass);
            if (jarFile != null) {
                return jarFile.getManifest();
            }
        } catch (Throwable ex) {
            Logger logger = Loggers.getLogger(Manifests.class);
            logger.warn("Can't find the jar for class: {}", Reflects.getFQNClassName(klass));
            IOs.close(jarFile);
        }
        return null;
    }

    /**
     * <p>Attempts to return the version of the jar/module for the given class.</p>
     * <p>First, retrieves the {@code Implementation-Version} main attribute of the manifest;
     * if that is missing, retrieves the JPMS module version (via reflection);
     * if that is missing, returns an empty Optional.</p>
     *
     * @param klass the class of the jar/module to retrieve the version
     * @return the jar/module version, or an empty Optional
     */
    public static String getClassVersion(Class<?> klass) {
        Package p = klass.getPackage();
        if (p != null) {
            return p.getImplementationVersion();
        }
        Manifest manifest = getManifest(klass);
        if (manifest == null) {
            return null;
        }
        Attributes attributes = manifest.getMainAttributes();
        String version = attributes.getValue("Implementation-Version");

        if (Emptys.isNotEmpty(version)) {
            return version;
        }

        try {
            Object module = klass.getClass().getMethod("getModule").invoke(klass);
            Object descriptor = module.getClass().getMethod("getDescriptor").invoke(module);
            return (String) descriptor.getClass().getMethod("rawVersion").invoke(descriptor);
        } catch (Throwable x) {
        }
        return null;
    }
}
