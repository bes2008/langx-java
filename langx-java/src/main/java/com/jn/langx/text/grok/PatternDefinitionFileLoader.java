package com.jn.langx.text.grok;

import com.jn.langx.configuration.FullLoadConfigurationLoader;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PatternDefinitionFileLoader implements FullLoadConfigurationLoader<PatternDefinition> {
    private String filepath;
    private final Map<String, PatternDefinition> cache = Collects.emptyHashMap(true);
    private long lastModified = 0;

    public PatternDefinitionFileLoader(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public List<PatternDefinition> loadAll() {
        read();
        return Pipeline.of(this.cache.values()).asList();
    }

    private void read() {
        File file = new File(filepath);
        boolean needRead = false;

        if (file.exists() && file.canRead()) {
            if (cache.isEmpty() || this.lastModified < file.lastModified()) {
                needRead = true;
            }
        }
        if (needRead) {
            cache.putAll(PatternDefinitions.readDefinitions(filepath));
        }
    }

    @Override
    public PatternDefinition load(final String id) {
        read();
        return this.cache.get(id);
    }
}
