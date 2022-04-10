package com.jn.langx.text.grok.logstash;

import com.jn.langx.configuration.AbstractConfigurationLoader;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.PatternDefinition;
import com.jn.langx.util.net.URLs;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class LogStashLocalPatternDefinitionsLoader extends AbstractConfigurationLoader<PatternDefinition> {
    private Resource basedir = Resources.loadClassPathResource("patterns", LogStashLocalPatternDefinitionsLoader.class);

    @Override
    public Map<String, PatternDefinition> loadAll() {

        definitionFilesLastModifiedMap();
        return null;
    }

    private Map<String, Long> definitionFilesLastModifiedMap() {
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

        }
        return null;
    }
}
