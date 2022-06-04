package com.jn.langx.util.timing.scheduling;

/**
 * @since 4.6.7
 */
public class ImmediateTriggerFactory implements TriggerFactory {
    private static final String NAME = "immediate";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Trigger get(String expression) {
        return new ImmediateTrigger();
    }
}
