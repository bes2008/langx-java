/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.configuration;

import com.jn.langx.Reloadable;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.cache.Cache;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked"})
public abstract class AbstractConfigurationRepository<T extends Configuration, Loader extends ConfigurationLoader<T>, Writer extends ConfigurationWriter<T>> implements ConfigurationRepository<T, Loader, Writer>, Reloadable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConfigurationRepository.class);
    @NonNull
    protected String name;
    @NonNull
    protected Loader loader;
    @Nullable
    protected Writer writer;
    protected volatile boolean inited = false;
    protected volatile boolean running = false;
    @Nullable
    private EventPublisher eventPublisher;

    @Nullable
    private ConfigurationEventFactory<T> eventFactory;

    @NonNull
    private Cache<String, T> cache;

    private Comparator<T> comparator;

    /**
     * units: seconds
     * scan interval, if <=0, will not refresh
     */
    protected int reloadIntervalInSeconds = -1;

    public void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        this.reloadIntervalInSeconds = reloadIntervalInSeconds;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public void setCache(Cache<String, T> cache) {
        this.cache = cache;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventFactory(ConfigurationEventFactory<T> eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    private Timer timer;

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void startup() {
        if (!inited) {
            init();
        }
        Preconditions.checkNotNull(cache);
        running = true;
        logger.info("Startup configuration repository: {}", name);

        if (reloadIntervalInSeconds > 0) {
            try {
                reload();
            } catch (Throwable ex) {
                logger.warn(ex.getMessage(), ex);
            }
            if (timer == null) {
                logger.warn("The timer is not specified for the repository ({}) , will use a simple timer", name);
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
                        if (running) {
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
    public void shutdown() {
        running = false;
        logger.info("Shutdown configuration repository: {}", name);
        cache.clean();
    }

    @Override
    public void setConfigurationLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void setConfigurationWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public T getById(String id) {
        return cache.get(id);
    }

    @Override
    public void removeById(String id) {
        removeById(id, true);
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
    public void add(T configuration) {
        add(configuration, true);
    }

    @Override
    public T add(T configuration, boolean sync) {
        if (running) {
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
    public void update(T configuration) {
        update(configuration, true);
    }

    @Override
    public void update(T configuration, boolean sync) {
        if (running) {
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

    private void logMutation(ConfigurationEventType eventType, T configuration) {
        if (logger.isInfoEnabled()) {
            String template = eventFactory == null ? " a configuration: {}" : (Strings.startsWithVowelLetter(eventFactory.getDomain()) ? " an {} configuration: {}" : " a {} configuration: {}");
            template = Strings.upperCase(eventType.name().toLowerCase(), 0, 1) + template;
            if (eventFactory != null) {
                logger.info(template, eventFactory.getDomain(), configuration);
            } else {
                logger.info(template, configuration);
            }
        }
    }

    @Override
    public void init() throws InitializationException {
        Preconditions.checkNotNull(name, "Repository has no named");
        logger.info("Initial configuration repository: {}", name);
    }

    public Map<String, T> getAll() {
        return Collections.unmodifiableMap(cache.toMap());
    }

    @Override
    public void reload() {
        logger.info("Reload repository {}", name);
    }
}
