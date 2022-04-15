package com.jn.langx.test.util;

import com.jn.langx.util.Dates;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesTests {
    @Test
    public void test() {
        Date date = new Date();
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
        long d2= Date.parse(str);
        System.out.println(d2);
        str = date.toString();
        System.out.println(str);
        long d3 = Date.parse(str);
        System.out.println(d3);
    }
}
