package com.jn.langx.util.jar;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Manifests {
    private static final Logger logger = LoggerFactory.getLogger(Manifests.class);

    public static Manifest loadManifest() {
        URL url = Reflects.getCodeLocation(Manifests.class);
        if (url.getPath().endsWith("target/classes/")) {
            return null;
        }
        String jarPath = url.getPath();
        if (jarPath.startsWith("file:")) {
            jarPath = jarPath.replaceFirst("file:", "");
        }
        return loadManifest(jarPath);
    }


    public static Manifest loadManifest(@Nullable String jarPath) {
        if (jarPath == null) {
            return null;
        }

        String path0 = jarPath;

        String[] pathSegments = jarPath.split("!\\/");
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
            logger.warn("Can't find the jar : {}", path0);
            return null;
        } finally {
            IOs.close(jarFile);
        }
    }

    public static Manifest getManifest(Class<?> klass)
    {
        URL location = Reflects.getCodeLocation(klass);
        if (location != null){
            JarFile jarFile = null;
            try{
            jarFile = new JarFile(new File(location.toURI()));
            return jarFile.getManifest();
            }catch (Throwable ex){
                IOs.close(jarFile);
            }
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
    public static String getClassVersion(Class<?> klass)
    {
        Manifest manifest = getManifest(klass);
        if(manifest==null){
            return null;
        }
        Attributes attributes = manifest.getMainAttributes();
        String version = attributes.getValue("Implementation-Version");

        if (Emptys.isNotEmpty(version)) {
            return version;
        }
        return null;
    }
}
