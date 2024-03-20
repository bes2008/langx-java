package com.jn.langx.text.grok.pattern;


import com.jn.langx.propertyset.PropertySet;

/**
 * @since 4.5.0
 */
public class PatternDefinitionSource implements PropertySet {
    private PatternDefinitionRepository repository;

    @Override
    public Object getSource() {
        return repository;
    }

    @Override
    public void setName(String name) {
        this.repository.setName(name);
    }

    @Override
    public String getName() {
        return this.repository.getName();
    }

    public PatternDefinitionRepository getRepository() {
        return repository;
    }

    public void setRepository(PatternDefinitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean containsProperty(String name) {
        return repository.getById(name) != null;
    }

    @Override
    public Object getProperty(String name) {
        PatternDefinition definition = repository.getById(name);
        if (definition != null) {
            return definition.getExpr();
        }
        return null;
    }
}
