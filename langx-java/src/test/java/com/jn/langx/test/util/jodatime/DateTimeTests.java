package com.jn.langx.test.util.jodatime;

import com.jn.langx.util.jodatime.DateTime;
import org.junit.Test;

public class DateTimeTests {
    @Test
    public void test() {
        DateTime dt = DateTime.now();
        System.out.println(System.currentTimeMillis());
        System.out.println(dt.getMillis());

        System.out.println(dt.getZone().getID());

        System.out.println(dt.getYear());
        System.out.println(dt.getMonthOfYear());
    }
}
