package com.jn.langx.util.timing.scheduling;

import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;

import java.util.ServiceLoader;

/**
 * @since 4.6.7
 */
public class TriggerFactoryRegistry extends GenericRegistry<TriggerFactory> {

    public static final TriggerFactoryRegistry GLOBAL_TRIGGER_REGISTRY = new TriggerFactoryRegistry();

    @Override
    protected void doInit() throws InitializationException {
        Pipeline.of(ServiceLoader.load(TriggerFactory.class))
                .forEach(new Consumer<TriggerFactory>() {
                    @Override
                    public void accept(TriggerFactory triggerFactory) {
                        if (Strings.isNotBlank(triggerFactory.getName())) {
                            register(triggerFactory);
                        }
                    }
                });
    }
}
