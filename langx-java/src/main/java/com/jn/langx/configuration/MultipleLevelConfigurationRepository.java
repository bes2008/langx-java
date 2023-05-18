package com.jn.langx.configuration;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.NonDistinctTreeSet;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.*;
import com.jn.langx.util.struct.Holder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MultipleLevelConfigurationRepository<T extends Configuration, Loader extends ConfigurationLoader<T>, Writer extends ConfigurationWriter<T>> extends AbstractConfigurationRepository<T, Loader, Writer> {
    /**
     * key : the repository name
     * value: order
     */
    private Map<String, Integer> delegateOrderMap = new NonAbsentHashMap<String, Integer>(new Supplier<String, Integer>() {
        @Override
        public Integer get(String input) {
            return Integer.MAX_VALUE;
        }
    });

    private Supplier<String, Integer> delegateOrderSupplier = new Supplier<String, Integer>() {
        @Override
        public Integer get(String name) {
            return delegateOrderMap.get(name);
        }
    };
    private Comparator<String> orderComparator = new OrderedComparator<String>(delegateOrderSupplier);

    /**
     * key: the repository name
     */
    private Map<String, ConfigurationRepository> delegates = Collects.emptyHashMap(true);

    public void addRepository(ConfigurationRepository repository) {
        this.addRepository(repository, Integer.MAX_VALUE);
    }

    public void addRepository(ConfigurationRepository repository, int order) {
        delegateOrderMap.put(repository.getName(), order);
        delegates.put(repository.getName(), repository);
        // sort
        Set<String> set = new NonDistinctTreeSet<String>(orderComparator);
        set.addAll(delegateOrderMap.keySet());
        final Map<String, ConfigurationRepository> tmp = Collects.emptyHashMap(true);
        Pipeline.of(set)
                .map(new Function<String, ConfigurationRepository>() {
                    @Override
                    public ConfigurationRepository apply(String input) {
                        return delegates.get(input);
                    }
                })
                .forEach(new Consumer<ConfigurationRepository>() {
                    @Override
                    public void accept(ConfigurationRepository repository) {
                        tmp.put(repository.getName(), repository);
                    }
                });

        this.delegates = tmp;
    }

    public void removeRepository(String repositoryName) {
        delegateOrderMap.remove(repositoryName);
        delegates.remove(repositoryName);
    }

    @Override
    public void doStart() {
        // reverse it
        Pipeline.of(delegates.values()).reverse(true).forEach(new Consumer2<Integer, ConfigurationRepository>() {
            @Override
            public void accept(Integer index, ConfigurationRepository repository) {
                repository.startup();
            }
        });
        super.doStart();
    }

    @Override
    public void doStop() {
        super.doStop();
        Pipeline.of(delegates.values()).forEach(new Consumer<ConfigurationRepository>() {
            @Override
            public void accept(ConfigurationRepository configurationRepository) {
                configurationRepository.shutdown();
            }
        });
    }

    @Override
    public T getById(final String id) {
        T t = super.getById(id);

        if (t == null) {
            final Holder<T> holder = new Holder<T>();
            Collects.forEach(delegates.values(), new Consumer<ConfigurationRepository>() {
                @Override
                public void accept(ConfigurationRepository configurationRepository) {
                    holder.set((T) configurationRepository.getById(id));
                }
            }, new Predicate<ConfigurationRepository>() {
                @Override
                public boolean test(ConfigurationRepository configurationRepository) {
                    return !holder.isNull();
                }
            });
            t = holder.get();
        }
        return t;
    }

    public T getById(String repositoryName, String id) {
        ConfigurationRepository repository = delegates.get(repositoryName);
        if (repository != null) {
            return (T) repository.getById(id);
        }
        return null;
    }

    public T getById(final String id, Function<List<T>, T> mapper) {
        return mapper.apply(Pipeline.of(delegates.keySet()).map(new Function<String, T>() {
            @Override
            public T apply(String repositoryName) {
                return getById(repositoryName, id);
            }
        }).asList());
    }

    @Override
    public void removeById(final String id, final boolean sync) {
        super.removeById(id, sync);
        Collects.forEach(delegates.values(), new Consumer<ConfigurationRepository>() {
            @Override
            public void accept(ConfigurationRepository configurationRepository) {
                configurationRepository.removeById(id, sync);
            }
        });
    }

    private ConfigurationRepository findFirstRepository(final String id) {
        if (super.getById(id) != null) {
            return this;
        }
        return Collects.findFirst(delegates.values(), new Predicate<ConfigurationRepository>() {
            @Override
            public boolean test(ConfigurationRepository value) {
                return value.getById(id) != null;
            }
        });
    }


    @Override
    public T add(T configuration, boolean sync) {
        return super.add(configuration, sync);
    }

    @Override
    public void update(T configuration, boolean sync) {
        ConfigurationRepository old = findFirstRepository(configuration.getId());
        if (old != null) {
            old.update(configuration, sync);
        } else {
            super.update(configuration, sync);
        }
    }

    @Override
    public Map<String, T> getAll() {
        final Map<String, T> map = Collects.emptyHashMap(true);
        Pipeline.of(delegates.values()).reverse(true).forEach(new Consumer<ConfigurationRepository>() {
            @Override
            public void accept(ConfigurationRepository configurationRepository) {
                map.putAll(configurationRepository.getAll());
            }
        });
        return map;
    }
}
