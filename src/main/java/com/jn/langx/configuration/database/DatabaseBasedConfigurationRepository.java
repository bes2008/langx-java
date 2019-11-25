package com.jn.langx.configuration.database;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.diff.CollectionDiffResult;
import com.jn.langx.util.collection.diff.KeyBuilder;
import com.jn.langx.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class DatabaseBasedConfigurationRepository<T extends Configuration> extends AbstractConfigurationRepository<T, DatabaseBasedConfigurationLoader<T>, DatabaseBasedConfigurationWriter<T>> {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBasedConfigurationRepository.class);

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            super.init();
            Preconditions.checkNotNull(loader, "the configuration load is null");

            if (writer == null) {
                logger.warn("The writer is not specified for the repository ({}), will disable write configuration to storage", name);
            }
            // enable refresh
            if (reloadIntervalInSeconds > 1) {
                logger.info("The configuration refresh task is disabled for repository: {}", name);
            }
            inited = true;
        }
    }

    @Override
    public void reload() {
        List<T> newConfigs = Collects.asList(loader.loadAll());
        Collection<T> oldConfigs = getAll().values();
        CollectionDiffResult<T> differResult = Collects.diff(oldConfigs, newConfigs, null, new KeyBuilder<String, T>() {
            @Override
            public String getKey(T object) {
                return object.getId();
            }
        });
        if (differResult.hasDifference()) {
            Collects.forEach(differResult.getAdds(), new Consumer<T>() {
                @Override
                public void accept(T newConfig) {
                    add(newConfig, false);
                }
            });
            Collects.forEach(differResult.getUpdates(), new Consumer<T>() {
                @Override
                public void accept(T t) {
                    update(t, false);
                }
            });
            Collects.forEach(differResult.getRemoves(), new Consumer<T>() {
                @Override
                public void accept(T t) {
                    removeById(t.getId());
                }
            });
        }
    }
}
