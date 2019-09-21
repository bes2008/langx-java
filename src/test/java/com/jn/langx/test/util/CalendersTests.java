package com.jn.langx.test.util;

import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

public class CalendersTests {
    @Test
    public void name() {
        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        System.out.println(timeZone.getRawOffset());
        System.out.println(8 * 3600 * 1000);

        long now = new Date().getTime();
        long actual_now = now + timeZone.getRawOffset();
        long actual_zero = (now - actual_now % Dates.DAY_TO_MILLIS);
        System.out.println(now + ", " + Dates.format(new Date(now), Dates.YYYY_MM_DD_HH_mm_ss_SSS));
        System.out.println(actual_now + ", " + Dates.format(new Date(actual_now), Dates.YYYY_MM_DD_HH_mm_ss_SSS));
        System.out.println(actual_zero + ", " + Dates.format(new Date(actual_zero), Dates.YYYY_MM_DD_HH_mm_ss_SSS));


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long todayZero = c.getTimeInMillis();
        System.out.println(todayZero + ", " + Dates.format(new Date(todayZero), Dates.YYYY_MM_DD_HH_mm_ss_SSS));

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


}
