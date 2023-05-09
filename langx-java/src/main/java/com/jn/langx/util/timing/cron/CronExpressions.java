package com.jn.langx.util.timing.cron;

import java.util.Date;

public class CronExpressions {
    private CronExpressions(){}
    public static Date nextTime(String cron, Date time) {
        return nextTime(cron, CronExpressionType.QUARTZ, time);
    }

    public static Date nextTime(String cron, CronExpressionType type, Date time) {
        return nextTime(new CronExpressionBuilder().type(type).expression(cron).build(), time);
    }

    /**
     * 根据cron来获取下一个时间
     *
     * @return 获取 time参数的下一个时间
     */
    public static Date nextTime(CronExpression cron, Date time) {
        return cron.getTimeAfter(time);
    }
}
