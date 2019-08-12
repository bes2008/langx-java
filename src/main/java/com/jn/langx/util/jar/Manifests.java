package com.jn.langx.util.jar;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.IOs;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
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


    public static Manifest loadManifest(String jarPath) {
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
}
