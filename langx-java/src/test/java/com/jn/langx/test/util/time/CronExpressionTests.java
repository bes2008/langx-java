package com.jn.langx.test.util.time;

import com.jn.langx.util.timing.cron.CronExpression;
import org.junit.Test;

public class CronExpressionTests {
    @Test
    public void test() throws Throwable{
        CronExpression cronExpression = new CronExpression("30 * * * * ?");
        System.out.println(cronExpression.getCronExpression());
    }
}
