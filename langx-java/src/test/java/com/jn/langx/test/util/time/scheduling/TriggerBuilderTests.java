package com.jn.langx.test.util.time.scheduling;

import com.jn.langx.util.timing.cron.CronTrigger;
import com.jn.langx.util.timing.scheduling.*;
import org.junit.Assert;
import org.junit.Test;

public class TriggerBuilderTests {
    @Test
    public void test() {

        Assert.assertTrue(newAndNext("immediate") instanceof ImmediateTrigger);
        Assert.assertTrue(newAndNext("immediate:xxx") instanceof ImmediateTrigger);

        Assert.assertTrue(Triggers.newTrigger("periodic:false 12 seconds") instanceof PeriodicTrigger);
        Assert.assertTrue(newAndNext("periodic:12 seconds") instanceof PeriodicTrigger);

        Assert.assertTrue(newAndNext("instant:2022-06-07 12:00:00") instanceof InstantTrigger);

        Assert.assertTrue(newAndNext("cron:0 * * * * ? ") instanceof CronTrigger);
    }

    private Trigger newAndNext(String expression) {
        Trigger trigger = Triggers.newTrigger(expression);
        trigger.nextExecutionTime(new SimpleTriggerContext());
        return trigger;
    }
}
