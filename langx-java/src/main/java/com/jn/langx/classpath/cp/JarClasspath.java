package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.ResourceNotFoundException;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.file.Filenames;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClasspath implements Classpath {
    /**
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


    private String getSuffix(String path) {
        String suffix = Filenames.getSuffix(path);
        return Emptys.isNotEmpty(suffix) ? suffix : "__langx_other__";
    }

    public JarClasspath(String jarPath) {
        this(new File(jarPath));
    }

    public JarClasspath(File file){
        JarFile jarfile = null;
        try {
            jarfile = new JarFile(file);
            for (JarEntry entry : Collections.list(jarfile.entries())) {
                String suffix = getSuffix(entry.getName());
                fileEntries.get(suffix).add(entry.getName());
            }

            this.jarfileURL = file.getCanonicalFile().toURI().toURL().toString();
        } catch (IOException e) {
            throw new ResourceNotFoundException(file.getName());
        } finally {
            IOs.close(jarfile);
        }
    }



    @Override
    public Resource find(String filepath, boolean isClass) {
        filepath = Classpaths.getPath(filepath, isClass);
        String suffix = getSuffix(filepath);

        if (this.fileEntries.get(suffix).contains(filepath)) {
            String url = StringTemplates.format("jar:{}!/{}", this.jarfileURL, filepath);
            return Resources.loadUrlResource(url);
        }
        return null;
    }
}
