package com.jn.langx.text.grok.pattern;

import com.jn.langx.annotation.NullableIf;
import com.jn.langx.configuration.BaseConfigurationRepository;
import com.jn.langx.configuration.ConfigurationEventType;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 4.7.4
 */
public class SimplePatternDefinitionRepository extends BaseConfigurationRepository<PatternDefinition, PatternDefinitionLoader, PatternDefinitionWriter> implements PatternDefinitionRepository {
    private Map<String, PatternDefinition> definitions = new ConcurrentHashMap<String, PatternDefinition>();
    /**
     * units: seconds
     * scan interval, if <=0, will not refresh
     */
    protected int reloadIntervalInSeconds = -1;

    public void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        this.reloadIntervalInSeconds = reloadIntervalInSeconds;
    }

    @NullableIf("reloadIntervalInSeconds > 0")
    private Timer timer;

    @Override
    protected void doStart() {
        if (!inited) {
            init();
        }
        final Logger logger = Loggers.getLogger(getClass());
        logger.info("Startup configuration repository: {}", getName());

        if (reloadIntervalInSeconds > 0) {
            try {
                reload();
            } catch (Throwable ex) {
                logger.warn(ex.getMessage(), ex);
            }
            if (timer == null) {
                logger.warn("The timer is not specified for the repository ({}) , will use a simple timer", getName());
                timer = new HashedWheelTimer(new CommonThreadFactory("Configuration", true), 50, TimeUnit.MILLISECONDS);
            }

            timer.newTimeout(new TimerTask() {
                @Override
                public void run(Timeout timeout) throws Exception {
                    try {
                        reload();
                    } catch (Throwable ex) {
                        logger.error(ex.getMessage(), ex);
                    } finally {
                        if (isRunning()) {
                            timer.newTimeout(this, reloadIntervalInSeconds, TimeUnit.SECONDS);
                        }
                    }
                }
            }, reloadIntervalInSeconds, TimeUnit.SECONDS);
        } else {
            reload();
        }
    }

    @Override
    protected void doStop() {
        Logger logger = Loggers.getLogger(getClass());
        logger.info("Shutdown configuration repository: {}", getName());
        definitions.clear();
    }


    @Override
    public PatternDefinition getById(String id) {
        return definitions.get(id);
    }


    @Override
    public void removeById(String id, boolean sync) {
        PatternDefinition configuration = definitions.get(id);
        if (configuration != null) {
            logMutation(ConfigurationEventType.REMOVE, configuration);
            if (sync && writer != null && writer.isSupportsRemove()) {
                writer.remove(id);
            }
            definitions.remove(id);
            if (eventPublisher != null && eventFactory != null) {
                eventPublisher.publish(eventFactory.createEvent(ConfigurationEventType.ADD, configuration));
            }
        }
    }

    @Override
    public PatternDefinition add(PatternDefinition configuration, boolean sync) {
        if (isRunning()) {
            logMutation(ConfigurationEventType.ADD, configuration);
            if (sync && writer != null && writer.isSupportsWrite()) {
                writer.write(configuration);
            }
            definitions.put(configuration.getId(), configuration);
            if (eventPublisher != null && eventFactory != null) {
                eventPublisher.publish(eventFactory.createEvent(ConfigurationEventType.ADD, configuration));
            }
        }
        return configuration;
    }

    @Override
    public void update(PatternDefinition configuration, boolean sync) {
        if (isRunning()) {
            logMutation(ConfigurationEventType.UPDATE, configuration);
            if (sync && writer != null && writer.isSupportsRewrite()) {
                writer.rewrite(configuration);
            }
            definitions.put(configuration.getId(), configuration);
            if (eventPublisher != null && eventFactory != null) {
                eventPublisher.publish(eventFactory.createEvent(ConfigurationEventType.UPDATE, configuration));
            }
        }
    }

    @Override
    protected void doInit() {
        Preconditions.checkNotNull(getName(), "Repository has no named");
        Logger logger = Loggers.getLogger(getClass());
        logger.info("Initial configuration repository: {}", getName());
    }

    @Override
    public Map<String, PatternDefinition> getAll() {
        return Collects.immutableMap(definitions);
    }
}
