package com.jn.langx.configuration.database;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.diff.CollectionDiffResult;
import com.jn.langx.util.collection.diff.KeyBuilder;
import com.jn.langx.util.function.Consumer;

import java.util.Collection;
import java.util.List;

public class DatabaseBasedConfigurationRepository<T extends Configuration> extends AbstractConfigurationRepository<T, DatabaseBasedConfigurationLoader<T>, DatabaseBasedConfigurationWriter<T>> {
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
