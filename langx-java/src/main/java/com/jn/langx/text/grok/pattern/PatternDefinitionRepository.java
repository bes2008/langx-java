package com.jn.langx.text.grok.pattern;

import com.jn.langx.configuration.ConfigurationRepository;

import java.util.Map;

/**
 * @since 4.5.1
 */
public interface PatternDefinitionRepository extends ConfigurationRepository<PatternDefinition, PatternDefinitionLoader, PatternDefinitionWriter> {
    @Override
    void setConfigurationLoader(PatternDefinitionLoader loader);

    @Override
    PatternDefinitionLoader getConfigurationLoader();

    @Override
    void setConfigurationWriter(PatternDefinitionWriter writer);

    @Override
    PatternDefinitionWriter getConfigurationWriter();

    @Override
    PatternDefinition getById(String id);

    @Override
    void add(PatternDefinition configuration);

    @Override
    PatternDefinition add(PatternDefinition configuration, boolean sync);

    @Override
    void update(PatternDefinition configuration);

    @Override
    void update(PatternDefinition configuration, boolean sync);

    @Override
    Map<String, PatternDefinition> getAll();
}
