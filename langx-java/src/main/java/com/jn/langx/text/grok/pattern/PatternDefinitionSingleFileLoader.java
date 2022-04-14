package com.jn.langx.text.grok.pattern;

import com.jn.langx.io.resource.FileResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.collection.Collects;

import java.io.File;
import java.util.Map;

/**
 * @since 4.5.0
 */
public class PatternDefinitionSingleFileLoader implements PatternDefinitionLoader {
    private Resource resource;
    private final Map<String, PatternDefinition> cache = Collects.emptyHashMap(true);
    private long lastModified = 0;

    public PatternDefinitionSingleFileLoader(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Map<String, PatternDefinition> loadAll() {
        read();
        return this.cache;
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
