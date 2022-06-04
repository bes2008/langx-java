package com.jn.langx.test.util.time.scheduling;

import com.jn.langx.util.timing.scheduling.ImmediateTrigger;
import com.jn.langx.util.timing.scheduling.Triggers;
import org.junit.Assert;
import org.junit.Test;

public class TriggerBuilderTests {
    @Test
    public void test() {

        Assert.assertTrue(Triggers.newTrigger("immediate") instanceof ImmediateTrigger);
        Assert.assertTrue(Triggers.newTrigger("immediate:xxx") instanceof ImmediateTrigger);


    }
}
