package com.jn.langx.text.grok.pattern;

import com.jn.langx.configuration.ConfigurationLoader;

import java.util.Map;

/**
 * @since 4.5.1
 */
public interface PatternDefinitionLoader extends ConfigurationLoader<PatternDefinition> {
    @Override
    PatternDefinition load(String id);

    @Override
    Map<String, PatternDefinition> loadAll();
}
