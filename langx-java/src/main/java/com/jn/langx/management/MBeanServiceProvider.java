package com.jn.langx.management;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.registry.AbstractRegistry;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class MBeanServiceProvider extends AbstractRegistry<Class, MBeanService> {
    private static MBeanServiceProvider INSTANCE;

    private MBeanServiceProvider() {
        super(new ConcurrentHashMap<Class, MBeanService>());
    }

    @Override
    public void register(MBeanService mBeanService) {
        // ignore it
    }

    public static MBeanServiceProvider getInstance() {
        if (INSTANCE == null) {
            synchronized (MBeanServiceProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MBeanServiceProvider();
                }
            }
        }
        return INSTANCE;
    }

    public static <S extends MBeanService> S getService(final Class<S> serviceClazz, final ClassLoader classLoader) {
        if (serviceClazz == null) {
            return null;
        }
        final MBeanService service = INSTANCE.get(serviceClazz);
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
            Logger logger = Loggers.getLogger(MBeanServiceProvider.class);
            while (iter.hasNext()) {
                try {
                    final S svc = iter.next();
                    if (svc.isServiceMatch()) {
                        INSTANCE.register(serviceClazz, svc);
                        return svc;
                    }
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
