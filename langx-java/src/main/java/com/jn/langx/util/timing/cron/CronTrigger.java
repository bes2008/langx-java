package com.jn.langx.util.timing.cron;

import com.jn.langx.util.timing.scheduling.Trigger;
import com.jn.langx.util.timing.scheduling.TriggerContext;

import java.util.Date;

public class CronTrigger implements Trigger {

    private CronExpression expression;

    public CronTrigger(String cron) {
        this.expression = new CronExpressionBuilder().type(CronExpressionType.QUARTZ).expression(cron).build();
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date lastExecution = triggerContext.lastScheduledExecutionTime();
        if (lastExecution == null) {
            return CronExpressions.nextTime(expression, new Date(0));
        }
        return CronExpressions.nextTime(expression, lastExecution);
    }
}
