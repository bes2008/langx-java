package com.jn.langx.text.grok;

import com.jn.langx.cache.Cache;
import com.jn.langx.configuration.*;

import java.util.Comparator;
import java.util.Map;

public class PatternDefinitionRepository<Loader extends ConfigurationLoader<PatternDefinition>, Writer extends ConfigurationWriter<PatternDefinition>> extends AbstractConfigurationRepository<PatternDefinition, Loader, Writer> {

    public PatternDefinitionRepository(){
        setName("Grok-Pattern-Definition-Custom-Repository");
    }

    @Override
    public PatternDefinition getById(String id) {
        return super.getById(id);
    }

    @Override
    public void setCache(Cache<String, PatternDefinition> cache) {
        super.setCache(cache);
    }

    @Override
    public void setComparator(Comparator<PatternDefinition> comparator) {
        super.setComparator(comparator);
    }

    @Override
    public Comparator<PatternDefinition> getComparator() {
        return super.getComparator();
    }

    @Override
    public void add(PatternDefinition configuration) {
        super.add(configuration);
    }

    @Override
    public PatternDefinition add(PatternDefinition configuration, boolean sync) {
        return super.add(configuration, sync);
    }

    @Override
    public void update(PatternDefinition configuration) {
        super.update(configuration);
    }

    @Override
    public void update(PatternDefinition configuration, boolean sync) {
        super.update(configuration, sync);
    }

    @Override
    public Map<String, PatternDefinition> getAll() {
        return super.getAll();
    }

    @Override
    public void reload() {
        super.reload();
    }
}
