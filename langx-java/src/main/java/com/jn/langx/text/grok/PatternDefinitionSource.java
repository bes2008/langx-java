package com.jn.langx.text.grok;

import com.jn.langx.configuration.ConfigurationRepository;
import com.jn.langx.text.PropertySource;

/**
 * @since 4.5.0
 */
public class PatternDefinitionSource implements PropertySource {
    private ConfigurationRepository<PatternDefinition,?,?> repository;

    @Override
    public void setName(String name) {
        this.repository.setName(name);
    }

    @Override
    public String getName() {
        return this.repository.getName();
    }

    public ConfigurationRepository<PatternDefinition,?,?>  getRepository() {
        return repository;
    }

    public void setRepository(ConfigurationRepository<PatternDefinition,?,?>  repository) {
        this.repository = repository;
    }

    @Override
    public boolean containsProperty(String name) {
        return repository.getById(name) != null;
    }

    @Override
    public String getProperty(String name) {
        PatternDefinition definition = repository.getById(name);
        if (definition != null) {
            return definition.getExpr();
        }
        return null;
    }
}
