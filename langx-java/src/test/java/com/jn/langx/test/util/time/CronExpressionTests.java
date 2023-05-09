package com.jn.langx.test.util.time;

import com.jn.langx.util.Dates;
import com.jn.langx.util.Objs;
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
            Date nextDate;
            nextDate = CronExpressions.nextTime(cronExpression, date);

            Date nextDate2 = cronExpression.getTimeAfter(date);

            String nextDate1String = Dates.format(nextDate, Dates.yyyy_MM_dd_HH_mm_ss);
            String nextDate2String = Dates.format(nextDate2, Dates.yyyy_MM_dd_HH_mm_ss);
            boolean equals = Objs.equals(nextDate1String, nextDate2String);
            if(equals){
                System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + nextDate1String);
            }else {
                System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + nextDate1String + "\t" + "next2: " + nextDate2String);
            }

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
            Date nextDate;
            nextDate = CronExpressions.nextTime(cronExpression, date);
            Date nextDate2 = cronExpression.getTimeAfter(date);

            String nextDate1String = Dates.format(nextDate, Dates.yyyy_MM_dd_HH_mm_ss);
            String nextDate2String = Dates.format(nextDate2, Dates.yyyy_MM_dd_HH_mm_ss);
            boolean equals = Objs.equals(nextDate1String, nextDate2String);
            if(equals){
                System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + nextDate1String);
            }else {
                System.out.println("date: " + Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss) + "\t" + "next: " + nextDate1String + "\t" + "next2: " + nextDate2String);
            }
            i++;
        }
        System.out.println("======test02 end=========");
    }

    @Test
    public void testUseCronReplaceSimpleTrigger(){
        CronExpression cronExpression = new CronExpressionBuilder()
                .type(CronExpressionType.QUARTZ)
                .expression("0 */45 * * * ?")
                .build();
        int i = 20;
        Date previous = new Date();
        Date next;

        while (i>0){
            next = CronExpressions.nextTime(cronExpression, previous);
            System.out.println(Dates.format(next, Dates.yyyy_MM_dd_HH_mm_ss));
            previous=next;
            i--;
        }

    }

    @Test
    public void test3(){
        CronExpression cronExpression = new CronExpressionBuilder()
                .type(CronExpressionType.QUARTZ)
                .expression("* * * * * ?")
                .build();
        int i = 1000;
        Date previous = new Date();
        Date next;

        while (i>0){
            next = CronExpressions.nextTime(cronExpression, previous);
            System.out.println(Dates.format(next, Dates.yyyy_MM_dd_HH_mm_ss));
            previous=next;
            i--;
        }

    }

}
