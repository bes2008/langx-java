package com.jn.langx.util.timing.scheduling;

import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.3
 */
public class ImmediateTrigger extends PeriodicTrigger{
    public static ImmediateTrigger INSTANCE = new ImmediateTrigger();
    public ImmediateTrigger() {
        super(0, TimeUnit.MILLISECONDS);
    }
}
