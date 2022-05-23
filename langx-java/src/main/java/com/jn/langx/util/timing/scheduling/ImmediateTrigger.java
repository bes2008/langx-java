package com.jn.langx.util.timing.scheduling;

import java.util.concurrent.TimeUnit;

public class ImmediateTrigger extends PeriodicTrigger{
    public static ImmediateTrigger INSTANCE = new ImmediateTrigger();
    public ImmediateTrigger() {
        super(0, TimeUnit.MILLISECONDS);
    }
}
