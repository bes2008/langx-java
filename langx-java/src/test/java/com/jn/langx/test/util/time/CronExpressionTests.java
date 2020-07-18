package com.jn.langx.test.util.time;

import com.jn.langx.util.Dates;
import com.jn.langx.util.timing.cron.CronExpression;
import com.jn.langx.util.timing.cron.CronExpressionBuilder;
import com.jn.langx.util.timing.cron.CronExpressionType;
import com.jn.langx.util.timing.cron.CronExpressions;
import org.junit.Test;

import java.util.Date;

public class CronExpressionTests {
    Date date = new Date();

    @Test
    public void test01() throws Throwable {
        System.out.println("======test01 start=========");
        System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss));
        CronExpression cronExpression = new CronExpressionBuilder().type(CronExpressionType.QUARTZ).expression("30 3/2 * * * ?").build();
        System.out.println(cronExpression.getCronExpression());
        int i = 0;
        while (i < 100) {
            date = Dates.addMinutes(date, 1);
            date = Dates.addSeconds(date, 13);
            Date nextDate = null;
            nextDate = CronExpressions.nextTime(cronExpression, date);
            System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + Dates.format(nextDate, Dates.yyyy_MM_dd_HH_mm_ss));
            i++;
        }
        System.out.println("======test01 end=========");
    }

    @Test
    public void test02() throws Throwable {
        System.out.println("======test02 start=========");
        System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss));
        CronExpression cronExpression = new CronExpressionBuilder().type(CronExpressionType.QUARTZ).expression("2/17 3/2 23 * * ?").build();
        System.out.println(cronExpression.getCronExpression());
        int i = 0;
        while (i < 200) {
            date = Dates.addHours(date, 1);
            date = Dates.addMinutes(date, 1);
            date = Dates.addSeconds(date, 13);
            Date nextDate = null;
            nextDate = CronExpressions.nextTime(cronExpression, date);
            System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + Dates.format(nextDate, Dates.yyyy_MM_dd_HH_mm_ss));
            i++;
        }
        System.out.println("======test02 end=========");
    }

}
