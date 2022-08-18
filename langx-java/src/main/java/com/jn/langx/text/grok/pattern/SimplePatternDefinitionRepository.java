package com.jn.langx.text.grok.pattern;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.configuration.*;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.WheelTimers;


/**
 * @since 4.5.1
 */
public class SimplePatternDefinitionRepository extends AbstractConfigurationRepository<PatternDefinition, PatternDefinitionLoader, PatternDefinitionWriter> implements PatternDefinitionRepository {

    public SimplePatternDefinitionRepository() {
        setName("Grok-Pattern-Definition-Simple-Repository");
    }


    /**
     * @since 4.7.3
     */
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
