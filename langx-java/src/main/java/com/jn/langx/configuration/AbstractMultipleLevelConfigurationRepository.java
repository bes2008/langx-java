package com.jn.langx.configuration;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractMultipleLevelConfigurationRepository<T extends Configuration, Loader extends ConfigurationLoader<T>, Writer extends ConfigurationWriter<T>> extends AbstractConfigurationRepository<T, Loader, Writer> {
    private Map<String, Integer> delegateOrderMap = new NonAbsentHashMap<String, Integer>(new Supplier<String, Integer>() {
        @Override
        public Integer get(String input) {
            return Integer.MAX_VALUE;
        }
    });
    private Map<String, ConfigurationRepository> delegates = new TreeMap<String, ConfigurationRepository>(new Comparator<String>() {
        @Override
        public int compare(String repositoryName1, String repositoryName2) {
            return delegateOrderMap.get(repositoryName1) - delegateOrderMap.get(repositoryName2);
        }
    });

    @Override
    public void startup() {
        // reverse it
        Collects.forEach(Collects.reverse(Collects.asList(delegates.values()),true), new Consumer2<Integer, ConfigurationRepository>() {
            @Override
            public void accept(Integer index, ConfigurationRepository repository) {
                repository.startup();
            }
        });
    }
}
