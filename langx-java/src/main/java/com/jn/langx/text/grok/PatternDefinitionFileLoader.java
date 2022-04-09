package com.jn.langx.text.grok;

import com.jn.langx.configuration.FullLoadConfigurationLoader;
import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @since 4.5.0
 */
public class PatternDefinitionFileLoader implements FullLoadConfigurationLoader<PatternDefinition> {
    private Resource resource;
    private final Map<String, PatternDefinition> cache = Collects.emptyHashMap(true);
    private long lastModified = 0;

    public PatternDefinitionFileLoader(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<PatternDefinition> loadAll() {
        read();
        return Pipeline.of(this.cache.values()).asList();
    }

    private void read() {
        boolean needRead = false;

        if (resource.exists() && resource.isReadable()) {
            if (cache.isEmpty()) {
                needRead = true;
            } else if (resource instanceof FileResource) {
                File file = ((FileResource) resource).getRealResource();
                if (this.lastModified < file.lastModified()) {
                    needRead = true;
                }
            }
        }
        if (needRead) {
            cache.putAll(PatternDefinitions.readDefinitions(resource));
        }
    }

    @Override
    public PatternDefinition load(final String id) {
        read();
        return this.cache.get(id);
    }
}
