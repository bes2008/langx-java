package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.*;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Filenames;
import com.jn.langx.util.net.URLs;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WarFileClasspath extends AbstractClasspath {

    public static final String FILE_NAME_PREFIX = "WEB-INF/classes/";

    /**
     * 只是存放了  WEB-INF/classes 目录下的文件名
     * key: file suffix
     * values: file path relative the jar
     */
    private Map<String, Set<String>> fileEntries = Collects.emptyNonAbsentHashMap(new Supplier<String, Set<String>>() {
        @Override
        public Set<String> get(String key) {
            return Collects.emptyHashSet();
        }
    });
    private String jarfileURL;
    private Location root;

    private String getSuffix(String path) {
        String suffix = Filenames.getSuffix(path);
        return Emptys.isNotEmpty(suffix) ? suffix : "__langx_other__";
    }

    public WarFileClasspath(String jarPath) {
        this(new File(jarPath));
    }

    public WarFileClasspath(File file) {
        if (file.exists() && file.isFile() && file.canRead()) {
            JarFile jarfile = null;
            try {
                jarfile = new JarFile(file);
                for (JarEntry entry : Collections.list(jarfile.entries())) {
                    if (!entry.isDirectory()) {
                        if (entry.getName().startsWith(FILE_NAME_PREFIX)) {
                            String suffix = getSuffix(entry.getName());
                            fileEntries.get(suffix).add(entry.getName().substring(FILE_NAME_PREFIX.length()));
                        }
                    }
                }

                this.jarfileURL = file.getCanonicalFile().toURI().toURL().toString();
            } catch (IOException e) {
                throw new ResourceNotFoundException(file.getName());
            } finally {
                IOs.close(jarfile);
            }
        }

        root = new Location(URLs.URL_PREFIX_JAR, jarfileURL + URLs.JAR_URL_SEPARATOR + "WEB-INF/classes");
    }

    @Override
    public Resource findResource(String relativePath) {
        relativePath = Classpaths.getCanonicalFilePath(relativePath);
        String suffix = getSuffix(relativePath);
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        if (this.fileEntries.get(suffix).contains(relativePath)) {
            String url = getUrl(relativePath);
            return Resources.loadUrlResource(url);
        }
        return null;
    }

    @Override
    public Location getRoot() {
        return root;
    }

    @Override
    public Set<Location> allResources() {
        return Pipeline.of(fileEntries).flatMap(new Function<String, Location>() {
            @Override
            public Location apply(String relativePath) {
                relativePath = Classpaths.getCanonicalFilePath(relativePath);
                return Locations.newLocation(root, relativePath);
            }
        }).asSet(false);
    }

    private String getUrl(String relativePath) {
        relativePath = Classpaths.getCanonicalFilePath(relativePath);
        return StringTemplates.formatWithPlaceholder("{}/{}", this.root.getLocation(), relativePath);
    }
}
