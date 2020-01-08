package com.jn.langx.util.leak;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.util.Strings;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

@Singleton
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
                    return SystemPropertys.getAccessor().getString("langx.customResourceLeakDetector");
                }
            });
        } catch (Throwable cause) {
            logger.error("Could not access System property: langx.customResourceLeakDetector", cause);
            customLeakDetector = null;
        }
        if (Strings.isBlank(customLeakDetector)) {
            obsoleteCustomClassConstructor = null;
            customClassConstructor = null;
        } else {
            obsoleteCustomClassConstructor = find3ArgumentsResourceLeakDetectorConstructor(customLeakDetector);
            customClassConstructor = find2ArgumentsResourceLeakDetectorConstructor(customLeakDetector);
        }
    }

    private static Constructor<?> find3ArgumentsResourceLeakDetectorConstructor(String customLeakDetector) {
        try {
            final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                    ClassLoader.getSystemClassLoader());

            if (Reflects.isSubClassOrEquals(ResourceLeakDetector.class, detectorClass)) {
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

    private static Constructor<?> find2ArgumentsResourceLeakDetectorConstructor(String customLeakDetector) {
        try {
            final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                    ClassLoader.getSystemClassLoader());

            if (Reflects.isSubClassOrEquals(ResourceLeakDetector.class, detectorClass)) {
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