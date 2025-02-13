package com.jn.langx.configuration;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NullableIf;
import com.jn.langx.cache.Cache;
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
import java.util.concurrent.TimeUnit;


@SuppressWarnings("unchecked")
public abstract class AbstractConfigurationRepository<T extends Configuration, Loader extends ConfigurationLoader<T>, Writer extends ConfigurationWriter<T>> extends BaseConfigurationRepository<T,Loader,Writer>{

    @NonNull
    protected Cache<String, T> cache;

    /**
     * units: seconds
     * scan interval, if <=0, will not refresh
     */
    protected int reloadIntervalInSeconds = -1;

    public void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        this.reloadIntervalInSeconds = reloadIntervalInSeconds;
    }

    public void setCache(Cache<String, T> cache) {
        this.cache = cache;
    }

    @NullableIf("reloadIntervalInSeconds>0")
    private Timer timer;

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    protected void doStart() {
        if (!inited) {
            init();
        }
        Preconditions.checkNotNull(cache);
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
        cache.clean();
    }


    @Override
    public T getById(String id) {
        return cache.get(id);
    }


    @Override
    public void removeById(String id, boolean sync) {
        T configuration = cache.getIfPresent(id);
        if (configuration != null) {
            logMutation(ConfigurationEventType.REMOVE, configuration);
            if (sync && writer != null && writer.isSupportsRemove()) {
                writer.remove(id);
            }
            cache.remove(id);
            if (eventPublisher != null && eventFactory != null) {
                eventPublisher.publish(eventFactory.createEvent(ConfigurationEventType.ADD, configuration));
            }
        }
    }

    @Override
    public T add(T configuration, boolean sync) {
        if (isRunning()) {
            logMutation(ConfigurationEventType.ADD, configuration);
            if (sync && writer != null && writer.isSupportsWrite()) {
                writer.write(configuration);
            }
            cache.set(configuration.getId(), configuration);
            if (eventPublisher != null && eventFactory != null) {
                eventPublisher.publish(eventFactory.createEvent(ConfigurationEventType.ADD, configuration));
            }
        }
        return configuration;
    }

    @Override
    public void update(T configuration, boolean sync) {
        if (isRunning()) {
            logMutation(ConfigurationEventType.UPDATE, configuration);
            if (sync && writer != null && writer.isSupportsRewrite()) {
                writer.rewrite(configuration);
            }
            cache.set(configuration.getId(), configuration);
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

    public Map<String, T> getAll() {
        return Collects.immutableMap(cache.toMap());
    }

}
