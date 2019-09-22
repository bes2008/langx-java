package com.jn.langx.test.util;

import com.jn.langx.util.Calendars;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

/**
 * 标准时间：GMT时间，也叫格林威治平时，也叫 UTC时间。
 * Java 6 ：
 *
 * <pre>
 * Date:
 * 表示一个瞬时值，单位是毫秒。它的结果是一个标准的GMT瞬时值。它和时区（timezone）地域（Locale）没有关系。
 * 在同一时刻，地球上两个不同时区国家的人使用Date API获取到的毫秒数完全一样的。
 *
 * Timezone:
 * 表示时区，其实是各个时区与GMT的毫秒数之差, 也即Offset。
 * 因为不同时区的时间看到的是不一样的，但是通过Date获取到的毫秒数是一样的，怎么做到的呢？
 * Date#toString 或者 SimpleDateFormat 都会根据Timezone 和 Locale 来进行处理，具体处理如下：
 *    1） 使用GMT 毫秒数 + Timezone.offset 计算出当地的实际毫秒数
 *    2） 格式化显示时会使用本土语言（Locale）进行处理
 *
 * Calender：
 *  日历，它综合了 GMT millis（Date）, timezone, locale，也就是说它是用来表示某个时区的某个国家的时间。
 *  并在此基础上提供了时间的加减运算。
 *
 * SimpleDateFormat 又是基于Calender（即基于 GMT millis（Date）, timezone, locale）的一个日期格式化工具
 * Pattern 涉及的符号：https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
 * </pre>
 */
public class CalendersTests {
    @Test
    public void testCalender() {
        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        System.out.println(timeZone.getRawOffset());
        System.out.println(8 * 3600 * 1000);

        long now = new Date().getTime();
        long actual_now = now + timeZone.getRawOffset();
        long actual_zero = (now - actual_now % Dates.DAY_TO_MILLIS);
        System.out.println(now + ", " + Dates.format(new Date(now), Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(actual_now + ", " + Dates.format(new Date(actual_now), Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(actual_zero + ", " + Dates.format(new Date(actual_zero), Dates.yyyy_MM_dd_HH_mm_ss_SSS));


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long todayZero = c.getTimeInMillis();
        System.out.println(todayZero + ", " + Dates.format(new Date(todayZero), Dates.yyyy_MM_dd_HH_mm_ss_SSS));

        long standardZero = now - now % Dates.DAY_TO_MILLIS;
        System.out.println(standardZero + "," + new Date(standardZero));
        long t1 = now / Dates.DAY_TO_MILLIS * Dates.DAY_TO_MILLIS;
        System.out.println(t1 + "," + new Date(t1));
        long t2 = actual_zero / Dates.DAY_TO_MILLIS * Dates.DAY_TO_MILLIS;
        System.out.println(t2 + "," + new Date(t2));
    }

    @Test
    public void testTimeZone() {
        Collection<String> timeZoneIds = Collects.sort(Collects.asList(TimeZone.getAvailableIDs()), new ComparableComparator<String>());
        Collects.forEach(timeZoneIds, new Consumer<String>() {
            @Override
            public void accept(String id) {
                System.out.println(id);
            }
        });
        System.out.println("default::::::" + TimeZone.getDefault().getID());
    }


    @Test
    public void testTimeZone2() {
        Calendar c1 = Calendar.getInstance();
        long t1 = c1.getTimeInMillis();
        System.out.println(c1.getTimeZone().getID() + " : " + t1);
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        long t2 = c2.getTimeInMillis();
        System.out.println(c2.getTimeZone().getID() + " : " + t2);

        System.out.println("delta:" + (t2 - t1));
    }

    @Test
    public void test2() {
        Calendar calendar = Calendar.getInstance();
        showCalendar(calendar);
        System.out.println("======================");
        Calendars.addField(calendar, Calendars.MONTH, 1);
        showCalendar(calendar);
        System.out.println("======================");
        Calendars.setField(calendar, Calendars.MONTH, 10);
        showCalendar(calendar);
        System.out.println("======================");
        Calendars.setMonth(calendar,15);
        showCalendar(calendar);
    }

    private void showCalendar(Calendar calendar){
        System.out.println("ERA:   " + calendar.get(Calendars.ERA.getField()));
        System.out.println("YEAR:  " + calendar.get(Calendars.YEAR.getField()));
        System.out.println("MONTH: " + calendar.get(Calendars.MONTH.getField()));
        System.out.println("DAY:   " + calendar.get(Calendars.DAY.getField()));
        System.out.println("HOUR:  " + calendar.get(Calendars.HOUR.getField()));
        System.out.println("MINUTE:" + calendar.get(Calendars.MINUTE.getField()));
        System.out.println("SECOND:" + calendar.get(Calendars.SECOND.getField()));
        System.out.println("MILLIS:" + calendar.get(Calendars.MILLIS.getField()));

        System.out.println(Dates.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
