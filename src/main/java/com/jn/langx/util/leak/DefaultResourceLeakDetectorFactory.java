package com.jn.langx.util.leak;

import com.jn.langx.util.SystemPropertys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultResourceLeakDetectorFactory.class);

    private final Constructor<?> obsoleteCustomClassConstructor;
    private final Constructor<?> customClassConstructor;

    public DefaultResourceLeakDetectorFactory() {
        String customLeakDetector;
        try {
            customLeakDetector = AccessController.doPrivileged(new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return SystemPropertys.getAccessor().getString("io.netty.customResourceLeakDetector");
                }
            });
        } catch (Throwable cause) {
            logger.error("Could not access System property: io.netty.customResourceLeakDetector", cause);
            customLeakDetector = null;
        }
        if (customLeakDetector == null) {
            obsoleteCustomClassConstructor = customClassConstructor = null;
        } else {
            obsoleteCustomClassConstructor = obsoleteCustomClassConstructor(customLeakDetector);
            customClassConstructor = customClassConstructor(customLeakDetector);
        }
    }

    private static Constructor<?> obsoleteCustomClassConstructor(String customLeakDetector) {
        try {
            final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                    ClassLoader.getSystemClassLoader());

            if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
                return detectorClass.getConstructor(Class.class, int.class, long.class);
            } else {
                logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
            }
        } catch (Throwable t) {
            logger.error("Could not load custom resource leak detector class provided: {}",
                    customLeakDetector, t);
        }
        return null;
    }

    private static Constructor<?> customClassConstructor(String customLeakDetector) {
        try {
            final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                    ClassLoader.getSystemClassLoader());

            if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
                return detectorClass.getConstructor(Class.class, int.class);
            } else {
                logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
            }
        } catch (Throwable t) {
            logger.error("Could not load custom resource leak detector class provided: {}",
                    customLeakDetector, t);
        }
        return null;
    }


    @Override
    public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval) {
        if (customClassConstructor != null) {
            try {
                @SuppressWarnings("unchecked")
                ResourceLeakDetector<T> leakDetector =
                        (ResourceLeakDetector<T>) customClassConstructor.newInstance(resource, samplingInterval);
                logger.debug("Loaded custom ResourceLeakDetector: {}",
                        customClassConstructor.getDeclaringClass().getName());
                return leakDetector;
            } catch (Throwable t) {
                logger.error(
                        "Could not load custom resource leak detector provided: {} with the given resource: {}",
                        customClassConstructor.getDeclaringClass().getName(), resource, t);
            }
        }

        ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval);
        logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
        return resourceLeakDetector;
    }
}