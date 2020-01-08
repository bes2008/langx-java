package com.jn.langx.test.util.jodatime;

import com.jn.langx.util.Dates;
import com.jn.langx.util.jodatime.DateTime;
import com.jn.langx.util.jodatime.LocalDateTime;
import org.junit.Test;

public class LocalDateTimeTests {
    @Test
    public void test() {
        LocalDateTime dt = new LocalDateTime();
        DateTime dt2 = DateTime.now();
        long time = System.currentTimeMillis();
        System.out.println(dt.millisOfDay().get());
        System.out.println(dt2.millisOfDay().get());
        System.out.println(dt.toString(Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(dt2.toString(Dates.yyyy_MM_dd_HH_mm_ss_SSS));
        System.out.println(time);
        System.out.println(dt.toDate().getTime());

    }
}
