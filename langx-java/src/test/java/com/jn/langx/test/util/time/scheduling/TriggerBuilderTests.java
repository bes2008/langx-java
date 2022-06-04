package com.jn.langx.test.util.time.scheduling;

import com.jn.langx.util.timing.cron.CronTrigger;
import com.jn.langx.util.timing.scheduling.ImmediateTrigger;
import com.jn.langx.util.timing.scheduling.PeriodicTrigger;
import com.jn.langx.util.timing.scheduling.Triggers;
import org.junit.Assert;
import org.junit.Test;

public class TriggerBuilderTests {
    @Test
    public void test() {

        Assert.assertTrue(Triggers.newTrigger("immediate") instanceof ImmediateTrigger);
        Assert.assertTrue(Triggers.newTrigger("immediate:xxx") instanceof ImmediateTrigger);

        Assert.assertTrue(Triggers.newTrigger("periodic:false 12 seconds") instanceof PeriodicTrigger);
        Assert.assertTrue(Triggers.newTrigger("periodic:12 seconds") instanceof PeriodicTrigger);
        Assert.assertTrue(Triggers.newTrigger("cron:0 * * * * ? ") instanceof CronTrigger);
    }
}
