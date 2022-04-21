package com.jn.langx.java8.tests.util.time;

import com.jn.langx.java8.util.datetime.Dates8;
import com.jn.langx.util.Dates;
import org.junit.Test;

import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeTests {
    @Test
    public void testLocalDateTimeByMills() {
        long now = System.currentTimeMillis();
        System.out.println(now);
        Date date = new Date(now);

        Instant instant = Instant.ofEpochMilli(now);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        System.out.println(Dates.format(date, Dates.yyyy_MM_dd_T_HH_mm_ssZone));
        System.out.println(Dates8.format(localDateTime, Dates.yyyy_MM_dd_T_HH_mm_ssZone));

        // 下面几个方法都是为了获取 UTC 秒数：
        System.out.println(date.getTime() / 1000);
        System.out.println(instant.getEpochSecond());
        System.out.println(localDateTime.toEpochSecond(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000)));
        System.out.println(localDateTime.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(localDateTime)));

        System.out.println(Dates8.toDate(localDateTime).getTime());

    }

    @Test
    public void testFormat(){
        OffsetDateTime dateTime = OffsetDateTime.now();
        System.out.println(Dates.format(dateTime));
    }
}
