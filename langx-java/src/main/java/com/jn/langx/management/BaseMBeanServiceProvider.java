package com.jn.langx.management;

import com.jn.langx.registry.Registry;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class BaseMBeanServiceProvider implements Registry<Class, MBeanService> {
    private static final Logger logger = LoggerFactory.getLogger(BaseMBeanServiceProvider.class);
    private static final Map<Class<?>, MBeanService> registry = new ConcurrentHashMap<Class<?>, MBeanService>();

    @Override
    public void register(MBeanService mBeanService) {
        // ignore it
    }

    @Override
    public void register(Class key, MBeanService mBeanService) {
        registry.put(key, mBeanService);
    }

    @Override
    public MBeanService get(Class input) {
        return registry.get(input);
    }

    public static <S extends MBeanService> S getService(final Class<S> serviceClazz, final ClassLoader classLoader) {
        if (serviceClazz == null) {
            return null;
        }
        final MBeanService service = registry.get(serviceClazz);
        if (service != null && Reflects.isSubClassOrEquals(serviceClazz, service.getClass())) {
            return (S) service;
        }
        final ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
        try {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(classLoader);
            }
            final ServiceLoader<S> loader = ServiceLoader.load(serviceClazz);
            final Iterator<S> iter = loader.iterator();
            while (iter.hasNext()) {
                try {
                    final S svc = (S) iter.next();
                    if (svc.isServiceMatch()) {
                        registry.put((Class<?>) serviceClazz, svc);
                        return svc;
                    }
                    continue;
                } catch (Throwable ex) {
                    logger.warn("Exception occured when get the service [" + serviceClazz + "]", ex);
                }
            }
            logger.warn("Can't find the service [" + serviceClazz + "]");
        } finally {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(originalCL);
            }
        }
        return null;
    }

}
