package com.jn.langx.util.leak;


import com.jn.langx.util.Preconditions;
import com.jn.langx.util.SystemPropertys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * This static factory should be used to load {@link ResourceLeakDetector}s as needed
 */
public abstract class ResourceLeakDetectorFactory {
    private static final Logger logger = LoggerFactory.getLogger(ResourceLeakDetectorFactory.class);

    private static volatile ResourceLeakDetectorFactory factoryInstance = new ResourceLeakDetectorFactory.DefaultResourceLeakDetectorFactory();

    /**
     * Get the singleton instance of this factory class.
     *
     * @return the current {@link ResourceLeakDetectorFactory}
     */
    public static ResourceLeakDetectorFactory instance() {
        return factoryInstance;
    }

    /**
     * Set the factory's singleton instance. This has to be called before the static initializer of the
     * {@link ResourceLeakDetector} is called by all the callers of this factory. That is, before initializing a
     * Netty Bootstrap.
     *
     * @param factory the instance that will become the current {@link ResourceLeakDetectorFactory}'s singleton
     */
    public static void setResourceLeakDetectorFactory(ResourceLeakDetectorFactory factory) {
        factoryInstance = Preconditions.checkNotNull(factory, "factory");
    }

    /**
     * Returns a new instance of a {@link ResourceLeakDetector} with the given resource class.
     *
     * @param resource the resource class used to initialize the {@link ResourceLeakDetector}
     * @param <T>      the type of the resource class
     * @return a new instance of {@link ResourceLeakDetector}
     */
    public final <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource) {
        return newResourceLeakDetector(resource, ResourceLeakDetector.SAMPLING_INTERVAL);
    }

    /**
     * @param resource         the resource class used to initialize the {@link ResourceLeakDetector}
     * @param samplingInterval the interval on which sampling takes place
     * @param maxActive        This is deprecated and will be ignored.
     * @param <T>              the type of the resource class
     * @return a new instance of {@link ResourceLeakDetector}
     * @deprecated Use {@link #newResourceLeakDetector(Class, int)} instead.
     * <p>
     * Returns a new instance of a {@link ResourceLeakDetector} with the given resource class.
     */
    @Deprecated
    public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(
            Class<T> resource, int samplingInterval, long maxActive);

    /**
     * Returns a new instance of a {@link ResourceLeakDetector} with the given resource class.
     *
     * @param resource         the resource class used to initialize the {@link ResourceLeakDetector}
     * @param samplingInterval the interval on which sampling takes place
     * @param <T>              the type of the resource class
     * @return a new instance of {@link ResourceLeakDetector}
     */
    @SuppressWarnings("deprecation")
    public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval) {
        return newResourceLeakDetector(resource, ResourceLeakDetector.SAMPLING_INTERVAL, Long.MAX_VALUE);
    }

    /**
     * Default implementation that loads custom leak detector via system property
     */
    private static final class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory {
        private final Constructor<?> obsoleteCustomClassConstructor;
        private final Constructor<?> customClassConstructor;

        DefaultResourceLeakDetectorFactory() {
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

        @SuppressWarnings("deprecation")
        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval,
                                                                   long maxActive) {
            if (obsoleteCustomClassConstructor != null) {
                try {
                    @SuppressWarnings("unchecked")
                    ResourceLeakDetector<T> leakDetector =
                            (ResourceLeakDetector<T>) obsoleteCustomClassConstructor.newInstance(
                                    resource, samplingInterval, maxActive);
                    logger.debug("Loaded custom ResourceLeakDetector: {}",
                            obsoleteCustomClassConstructor.getDeclaringClass().getName());
                    return leakDetector;
                } catch (Throwable t) {
                    logger.error(
                            "Could not load custom resource leak detector provided: {} with the given resource: {}",
                            obsoleteCustomClassConstructor.getDeclaringClass().getName(), resource, t);
                }
            }

            ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval,
                    maxActive);
            logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
            return resourceLeakDetector;
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
}
