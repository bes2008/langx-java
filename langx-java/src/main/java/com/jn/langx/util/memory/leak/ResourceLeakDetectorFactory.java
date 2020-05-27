package com.jn.langx.util.memory.leak;


import com.jn.langx.util.Preconditions;

/**
 * This static factory should be used to load {@link ResourceLeakDetector}s as needed
 */
public abstract class ResourceLeakDetectorFactory {

    private static volatile ResourceLeakDetectorFactory FACTORY_INSTANCE = new DefaultResourceLeakDetectorFactory();

    /**
     * Get the singleton instance of this factory class.
     *
     * @return the current {@link ResourceLeakDetectorFactory}
     */
    public static ResourceLeakDetectorFactory instance() {
        return FACTORY_INSTANCE;
    }

    /**
     * Set the factory's singleton instance. This has to be called before the static initializer of the
     * {@link ResourceLeakDetector} is called by all the callers of this factory. That is, before initializing a
     * Netty Bootstrap.
     *
     * @param factory the instance that will become the current {@link ResourceLeakDetectorFactory}'s singleton
     */
    public static void setResourceLeakDetectorFactory(ResourceLeakDetectorFactory factory) {
        FACTORY_INSTANCE = Preconditions.checkNotNull(factory, "factory");
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
     * Returns a new instance of a {@link ResourceLeakDetector} with the given resource class.
     *
     * @param resource         the resource class used to initialize the {@link ResourceLeakDetector}
     * @param samplingInterval the interval on which sampling takes place
     * @param <T>              the type of the resource class
     * @return a new instance of {@link ResourceLeakDetector}
     */
    @SuppressWarnings("deprecation")
    public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval);

}
