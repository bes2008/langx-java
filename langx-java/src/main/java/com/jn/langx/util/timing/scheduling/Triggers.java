package com.jn.langx.util.timing.scheduling;

public class Triggers {
    public static Trigger newTrigger(String expression){
        return new TriggerBuilder().expression(expression).build();
    }
}
