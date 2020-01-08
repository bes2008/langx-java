package com.jn.langx.management;

import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class BaseMBeanServiceProvider {
    private static final Logger logger;
    protected static final Map<Class<?>, MBeanService> provider;

    public static <S extends MBeanService> S getService(final Class<S> serviceClazz, final ClassLoader classLoader) {
        if (serviceClazz == null) {
            return null;
        }
        final MBeanService service = (MBeanService) BaseMBeanServiceProvider.provider.get(serviceClazz);
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
                        BaseMBeanServiceProvider.provider.put((Class<?>) serviceClazz, svc);
                        return svc;
                    }
                    continue;
                } catch (Throwable ex) {
                    BaseMBeanServiceProvider.logger.warn("Exception occured when get the service [" + serviceClazz + "]", ex);
                }
            }
            BaseMBeanServiceProvider.logger.warn("Can't find the service [" + serviceClazz + "]");
        } finally {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(originalCL);
            }
        }
        return null;
    }

    static {
        logger = LoggerFactory.getLogger((Class) BaseMBeanServiceProvider.class);
        provider = new HashMap<Class<?>, MBeanService>();
    }
}
