package com.jn.langx.util.id.snowflake;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class SnowflakeIdWorkerProviderLoader {
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdWorkerProviderLoader.class);
    private static final SnowflakeIdWorkerProvider DEFAULT_PROVIDER = new SystemEnvironmentSnowflakeIdWorkerProvider();
    private static volatile boolean loaded = false;

    private static final Map<String, SnowflakeIdWorkerProvider> LOADED_PROVIDER_MAP = new HashMap<String, SnowflakeIdWorkerProvider>();

    public static SnowflakeIdWorkerProvider getProvider() {
        if (!loaded) {
            synchronized (SnowflakeIdWorkerProviderLoader.class) {
                if (!loaded) {
                    ServiceLoader<SnowflakeIdWorkerProvider> loader = ServiceLoader.load(SnowflakeIdWorkerProvider.class);
                    Collects.forEach(loader, new Consumer<SnowflakeIdWorkerProvider>() {
                        @Override
                        public void accept(SnowflakeIdWorkerProvider provider) {
                            if (provider.getProviderId().equals(SystemEnvironmentSnowflakeIdWorkerProvider.SYSTEM_ENVIRONMENT_SNOWFLAKE)) {
                                LOADED_PROVIDER_MAP.put(provider.getProviderId(), provider);
                            }
                        }
                    });
                    loaded = true;
                    if (LOADED_PROVIDER_MAP.isEmpty()) {
                        logger.warn("Has not any SnowflakeIdWorkerProvider found, will use the SystemEnvironmentSnowflakeIdWorkerProvider: {}", LOADED_PROVIDER_MAP.keySet().toString());
                    } else if (LOADED_PROVIDER_MAP.size() > 1) {
                        logger.warn("Too many SnowflakeIdWorkerProvider instances found: {}, will use the first", LOADED_PROVIDER_MAP.keySet().toString());
                    }
                }
            }
        }
        if (LOADED_PROVIDER_MAP.isEmpty()) {
            return DEFAULT_PROVIDER;
        }
        return LOADED_PROVIDER_MAP.get(Collects.asList(LOADED_PROVIDER_MAP.keySet()).get(0));
    }

}
