package com.jn.langx.test.util.jodatime;

import com.jn.langx.util.Dates;
import com.jn.langx.util.jodatime.DateTime;
import com.jn.langx.util.jodatime.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class LocalDateTimeTests {
    @BeforeClass
    public static void init() {
        System.out.println(new LocalDateTime().getChronology());
        System.out.println(DateTime.now().getChronology());
    }

    @Test
    public void test() {
        // LocalDateTime 用于替换 java.util.Date
        // DateTime 用于替换 java.util.Calendars
        LocalDateTime dt = new LocalDateTime();
        DateTime dt2 = DateTime.now();
        long time = System.currentTimeMillis();
        Date dt3 = new Date(time);
        System.out.println(dt.millisOfDay().get());
        System.out.println(dt2.millisOfDay().get());
        System.out.println(dt.toString(Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(dt2.toString(Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(Dates.format(dt3, Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(time);
        System.out.println(dt.toDate().getTime());

    }
}
