package com.jn.langx.java8.tests.util.time;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DatesTests {
    @Test
    public void test() {
        Date date = new Date();
        System.out.println(Dates.format(date));
        System.out.println(Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss));
        System.out.println(Dates.format(date, Dates.yyyy_MM_dd_HH_mm_ss_SSS));
    }

    @Test
    public void dateFormatterTests() {
        System.out.println(((SimpleDateFormat) SimpleDateFormat.getInstance()).toPattern());
        System.out.println(((SimpleDateFormat) SimpleDateFormat.getInstance()).toPattern());
    }

    @Test
    public void patternParse() {
        Date date = new Date();
        long d = date.getTime();
        System.out.println(d);
        String str = date.toLocaleString();
        System.out.println(str);
        //long d1 = Date.parse(str);
        //System.out.println(d1);
        str = date.toGMTString();
        System.out.println(str);
        long d2 = Date.parse(str);
        System.out.println(d2);
        str = date.toString();
        System.out.println(str);
        long d3 = Date.parse(str);
        System.out.println(d3);
    }

    @Test
    public void test2() {
        for (int i = 0; i < 100; i++) {
            DateTimeParsedResult r = Dates.parseDateTime(true, "2022-09-09 15:49:00GMT+8", null, null, Collects.asList("yyyy-MM-dd HH:mm:ss"));
            System.out.println(StringTemplates.formatWithPlaceholder("{}, {} , {}", r.getTimestamp(), r.getLocale(), r.getTimeZone()));
        }
    }

    /**
     * 经过验证 jdk 8 time API 解析 GMT时间时，会报错，在 jdk 9 开始 该问题解决
     */
    @Test
    public void testGMTParse() {
        Date now = new Date();
        System.out.println(now.getTime());
        System.out.println(now.toInstant().toEpochMilli());
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        datetime = datetime + "GMT+8";
        System.out.println(datetime);
        System.out.println(Locale.getDefault());
        System.out.println(ZoneId.systemDefault());
        List<String> patterns = Lists.newArrayList("yyyy-MM-dd HH:mm:ss.SSSO"
        );
        for (String pattern : patterns) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            try {
                ZonedDateTime dt = ZonedDateTime.parse(datetime, formatter);
                System.out.println(StringTemplates.formatWithPlaceholder("timestamp: {}, timezone: {} , offset: {}", dt.toInstant().toEpochMilli(), TimeZone.getTimeZone(dt.getZone()), dt.getOffset().getTotalSeconds()));
                System.out.println((dt.toInstant().toEpochMilli() - now.getTime() / 1000 * 1000) / 1000);
                System.out.println(28800 - dt.getOffset().getTotalSeconds());
                // 解析完不是一个时区时，需要校正。
                // 经过测试，使用 小写 z 来 format, 然后解析时，会有偏差，需要进行校正。 z 是美国专用
                if (TimeZone.getTimeZone(dt.getZone()).getRawOffset() != TimeZone.getTimeZone(ZoneId.systemDefault()).getRawOffset()) {
                    long timestamp = dt.toInstant().toEpochMilli() - (TimeZone.getTimeZone(ZoneId.systemDefault()).getRawOffset() - dt.getOffset().getTotalSeconds() * 1000L);
                    System.out.println(timestamp);
                    System.out.println(now.getTime() == timestamp);
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test3() {
        Date now = new Date();
        System.out.println(now.getTime());
        System.out.println(now.toInstant().toEpochMilli());
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX", Locale.getDefault()).format(now);
        System.out.println(datetime);
        System.out.println(Locale.getDefault());
        System.out.println(ZoneId.systemDefault());
        List<String> patterns = Lists.newArrayList("yyyy-MM-dd HH:mm:ss.SSSX"
        );
        ZoneId expected = ZoneId.of("UTC");
        for (String pattern : patterns) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.getDefault());
            try {
                ZonedDateTime dt = ZonedDateTime.parse(datetime, formatter);
                System.out.println(StringTemplates.formatWithPlaceholder("timestamp: {}, timezone: {} , offset: {}", dt.toInstant().toEpochMilli(), TimeZone.getTimeZone(dt.getZone()), dt.getOffset().getTotalSeconds()));
                System.out.println((dt.toInstant().toEpochMilli() - now.getTime() / 1000 * 1000) / 1000);
                System.out.println(28800 - dt.getOffset().getTotalSeconds());
                // 解析完不是一个时区时，需要校正。
                // 经过测试，使用 小写 z 来 format, 然后解析时，会有偏差，需要进行校正。 z 是美国专用
                if (TimeZone.getTimeZone(dt.getZone()).getRawOffset() != TimeZone.getTimeZone(expected).getRawOffset()) {
                    long timestamp = dt.toInstant().toEpochMilli() - (TimeZone.getTimeZone(expected).getRawOffset() - dt.getOffset().getTotalSeconds() * 1000L);
                    System.out.println(timestamp);
                    System.out.println(now.getTime() == timestamp);
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }
    }



    @Test
    public void test4() {
        List<String> datetimes = Lists.newArrayList("2022-09-09 15:49:00GMT+8",
                "2022-09-09 15:49:00GMT+08:00",
                "2022-09-09 15:49:00",
                "2022-09-09 15:49:00+08",
                "2022-09-09 15:49:00+0800",
                "2022-09-09 15:49:00+08:00",
                "2022-09-09 15:49:00+080000",
                "2022-09-09 15:49:00+08:00:00"
        );

        for (String datetime : datetimes) {
            DateTimeParsedResult r = Dates.parseDateTime(true, datetime, null, null, Collects.asList("yyyy-MM-dd HH:mm:ss"));
            System.out.println(StringTemplates.formatWithPlaceholder("{}, {} , {}", r.getTimestamp(), r.getLocale(), r.getTimeZone()));
        }
    }

}
