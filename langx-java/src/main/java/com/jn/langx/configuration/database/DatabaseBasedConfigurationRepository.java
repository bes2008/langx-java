package com.jn.langx.configuration.database;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.diff.MapDiffResult;
import com.jn.langx.util.comparator.Comparators;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.Map;

public class DatabaseBasedConfigurationRepository<T extends Configuration> extends AbstractConfigurationRepository<T, DatabaseBasedConfigurationLoader<T>, DatabaseBasedConfigurationWriter<T>> {

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            super.init();
            Preconditions.checkNotNull(loader, "the configuration load is null");
            Logger logger = Loggers.getLogger(getClass());
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
        Map<String, T> newConfigs = loader.loadAll();
        Map<String, T> oldConfigs = getAll();
        MapDiffResult<String, T> differResult = Collects.<String, T, Map<String, T>>diff(oldConfigs, newConfigs, getComparator(), Comparators.STRING_COMPARATOR);
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
