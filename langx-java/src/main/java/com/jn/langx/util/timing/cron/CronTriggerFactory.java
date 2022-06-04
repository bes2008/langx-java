package com.jn.langx.util.timing.cron;

import com.jn.langx.util.timing.scheduling.Trigger;
import com.jn.langx.util.timing.scheduling.TriggerFactory;

/**
 * @since 4.6.7
 */
public class CronTriggerFactory implements TriggerFactory {
    private static final String NAME = "cron";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Trigger get(String expression) {
        return new CronTrigger(expression);
    }
}
