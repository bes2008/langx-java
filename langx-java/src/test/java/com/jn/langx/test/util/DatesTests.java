package com.jn.langx.test.util;

import com.jn.langx.util.Dates;
import org.junit.Test;

import java.util.Date;

public class DatesTests {
    @Test
    public void test(){
        Date date = new Date();
        System.out.println(Dates.format(date,Dates.yyyy_MM_dd_HH_mm_ss));
        System.out.println(Dates.format(date,Dates.yyyy_MM_dd_HH_mm_ss_SSS));
    }
}
