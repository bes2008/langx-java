package com.jn.langx.text.grok.logstash;

import com.jn.langx.configuration.AbstractConfigurationLoader;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.PatternDefinition;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.file.filter.IsFileFilter;
import com.jn.langx.util.net.URLs;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class LogStashLocalPatternDefinitionsLoader extends AbstractConfigurationLoader<PatternDefinition> {
    private volatile Resource basedir;
    private EcsCompatibility ecsCompatibility = EcsCompatibility.disabled;

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
        if (basedir == null) {
            boolean ecsv1Enabled = false;
            if (ecsCompatibility == EcsCompatibility.v1) {
                ecsv1Enabled = true;
            }
            basedir = Resources.loadClassPathResource("patterns/" + (ecsv1Enabled ? "ecs-v1" : "legacy"), LogStashLocalPatternDefinitionsLoader.class);
        }
        List<File> findDefinitionFiles = findDefinitionFiles();
        return null;
    }

    private List<File> findDefinitionFiles() {
        File basedir = null;
        if (this.basedir instanceof FileResource) {
            FileResource fileResource = (FileResource) this.basedir;
            basedir = fileResource.getRealResource();
        } else if (this.basedir instanceof ClassPathResource) {
            ClassPathResource classPathResource = (ClassPathResource) this.basedir;
            URL baseUrl = classPathResource.getRealResource();
            basedir = URLs.getFile(baseUrl);
        }
        if (basedir != null) {
            return Collects.asList(basedir.listFiles((FileFilter) new IsFileFilter()));
        }
        return Collects.emptyArrayList();
    }
}
