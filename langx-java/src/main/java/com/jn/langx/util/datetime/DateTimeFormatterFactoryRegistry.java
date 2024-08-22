package com.jn.langx.util.datetime;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.registry.Registry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.*;

/**
 * @since 4.5.2
 */
@Singleton
public class DateTimeFormatterFactoryRegistry implements Registry<Class, DateTimeFormatterFactory> {

    private final Map<Class, DateTimeFormatterFactory> map;

    private DateTimeFormatterFactoryRegistry() {
        this.map = new HashMap<Class, DateTimeFormatterFactory>();
        init();
    }

    private volatile static DateTimeFormatterFactoryRegistry INSTANCE;

    public static DateTimeFormatterFactoryRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (DateTimeFormatterFactoryRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DateTimeFormatterFactoryRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private void init() {
        Collects.forEach(CommonServiceProvider.loadService(DateTimeFormatterFactory.class), new Consumer<DateTimeFormatterFactory>() {
            @Override
            public void accept(DateTimeFormatterFactory factory) {
                register(factory);
            }
        });
    }

    @Override
    public DateTimeFormatterFactory get(final Class clazz) {
        DateTimeFormatterFactory factory = map.get(clazz);
        if (factory == null) {
            Set<Class> classSet = map.keySet();
            Class key = Collects.findFirst(classSet, new Predicate<Class>() {
                @Override
                public boolean test(Class value) {
                    return Reflects.isSubClassOrEquals(value, clazz);
                }
            });
            if (key != null) {
                factory = map.get(key);
            }
        }
        return factory;
    }

    @Override
    public void register(final DateTimeFormatterFactory dateTimeFormatterFactory) {
        List<Class> supported = dateTimeFormatterFactory.supported();
        Collects.forEach(supported, new Consumer<Class>() {
            @Override
            public void accept(Class clazz) {
                register(clazz, dateTimeFormatterFactory);
            }
        });
    }

    @Override
    public void register(Class key, DateTimeFormatterFactory dateTimeFormatterFactory) {
        map.put(key, dateTimeFormatterFactory);
    }

    @Override
    public void unregister(Class key) {
        map.remove(key);
    }

    @Override
    public boolean contains(Class key) {
        return map.containsKey(key);
    }
}
