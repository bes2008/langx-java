package com.jn.langx.util.timing.scheduling;

import com.jn.langx.Factory;
import com.jn.langx.Named;

/**
 * @since 4.6.7
 */
public interface TriggerFactory extends Factory<String, Trigger>, Named {
    @Override
    String getName();

    @Override
    Trigger get(String expression);
}
