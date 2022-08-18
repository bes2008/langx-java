package com.jn.langx.text.grok.pattern;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.configuration.*;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.WheelTimers;

import java.util.Comparator;
import java.util.Map;

/**
 * @since 4.5.1
 */
public class SimplePatternDefinitionRepository extends AbstractConfigurationRepository<PatternDefinition, PatternDefinitionLoader, PatternDefinitionWriter> implements PatternDefinitionRepository {

    public SimplePatternDefinitionRepository() {
        setName("Grok-Pattern-Definition-Simple-Repository");
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

    @Override
    protected void doInit() throws InitializationException {
        Timer timer = this.getTimer();
        if (timer == null) {
            timer = WheelTimers.newHashedWheelTimer();
        }

        this.setTimer(timer);

        Cache<String, PatternDefinition> cache = this.cache;
        if (cache == null) {
            cache = CacheBuilder.<String, PatternDefinition>newBuilder()
                    .timer(timer)
                    .build();
            this.setCache(cache);
        }
    }
}
