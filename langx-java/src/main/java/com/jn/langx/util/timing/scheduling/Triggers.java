package com.jn.langx.util.timing.scheduling;

public final class Triggers {
    private Triggers(){}
    public static Trigger newTrigger(String expression){
        return new TriggerBuilder().expression(expression).build();
    }
}
