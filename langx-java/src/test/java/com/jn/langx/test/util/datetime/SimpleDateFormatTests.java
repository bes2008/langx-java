package com.jn.langx.test.util.datetime;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatTests {
    @Test
    public void testParseTime() throws Throwable{
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("23:45:12.123");
        System.out.println(date.getTime());
        System.out.println(date);

        pattern = "hh:mm:ss a";
        simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.parse("11:45:12 下午");
        System.out.println(date.getTimezoneOffset());
        System.out.println(date);
    }
}
