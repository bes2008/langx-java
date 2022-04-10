package com.jn.langx.text.grok.logstash;

import com.jn.langx.configuration.AbstractConfigurationLoader;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.PatternDefinition;
import com.jn.langx.text.grok.PatternDefinitions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.filter.IsFileFilter;
import com.jn.langx.util.net.URLs;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogStashLocalPatternDefinitionsLoader extends AbstractConfigurationLoader<PatternDefinition> {
    private List<Resource> basedirs = Collects.emptyArrayList();
    private EcsCompatibility ecsCompatibility = EcsCompatibility.disabled;
    private Map<File, Long> lastModifiedMap = new HashMap<File, Long>();
    private Map<String, PatternDefinition> cache = new HashMap<String, PatternDefinition>();

    public EcsCompatibility getEcsCompatibility() {
        return ecsCompatibility;
    }

    public void setEcsCompatibility(EcsCompatibility ecsCompatibility) {
        if (ecsCompatibility != null) {
            this.ecsCompatibility = ecsCompatibility;
        }
    }

    @Override
    public Map<String, PatternDefinition> loadAll() {
        List<File> definitionFiles = findDefinitionFiles();
        final Map<String, PatternDefinition> map = new LinkedHashMap<String, PatternDefinition>();
        Pipeline.of(definitionFiles)
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        // 为读过 或者有更新时
                        return !lastModifiedMap.containsKey(file) || file.lastModified() > lastModifiedMap.get(file);
                    }
                })
                .map(new Function<File, Map<String, PatternDefinition>>() {
                    @Override
                    public Map<String, PatternDefinition> apply(File file) {
                        lastModifiedMap.put(file, file.lastModified());
                        return PatternDefinitions.readDefinitions(file);
                    }
                })
                .forEach(new Consumer<Map<String, PatternDefinition>>() {
                    @Override
                    public void accept(Map<String, PatternDefinition> m) {
                        if (m != null) {
                            map.putAll(m);
                        }
                    }
                });
        this.cache = map;
        return this.cache;
    }

    private List<File> findDefinitionFiles() {
        if (basedirs.isEmpty()) {
            synchronized (basedirs) {
                if (basedirs.isEmpty()) {
                    basedirs.add(Resources.loadClassPathResource("patterns/legacy", LogStashLocalPatternDefinitionsLoader.class));
                    boolean ecsEnabled = ecsCompatibility == EcsCompatibility.v1;
                    if (ecsEnabled) {
                        basedirs.add(Resources.loadClassPathResource("patterns/ecs-v1", LogStashLocalPatternDefinitionsLoader.class));
                    }
                }
            }
        }
        final List<File> files = Collects.emptyArrayList();
        Collects.forEach(this.basedirs, new Consumer<Resource>() {
            @Override
            public void accept(Resource resource) {
                File basedir = null;
                if (resource instanceof FileResource) {
                    FileResource fileResource = (FileResource) resource;
                    basedir = fileResource.getRealResource();
                } else if (resource instanceof ClassPathResource) {
                    ClassPathResource classPathResource = (ClassPathResource) resource;
                    URL baseUrl = classPathResource.getRealResource();
                    basedir = URLs.getFile(baseUrl);
                }
                if (basedir != null) {
                    files.addAll(Collects.asList(basedir.listFiles((FileFilter) new IsFileFilter())));
                }
            }
        });

        return files;
    }

    @Override
    public PatternDefinition load(String id) {
        return this.cache.get(id);
    }
}
