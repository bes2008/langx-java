package com.jn.langx.test.util.datetime;

import com.jn.langx.util.datetime.calendarist.Calendarist;
import com.jn.langx.util.datetime.calendarist.CycleDate;
import com.jn.langx.util.datetime.calendarist.LunarDate;
import com.jn.langx.util.datetime.calendarist.SolarDate;

public class CalendaristTests {
    public static void test() {
        // 设置要转换的阳历日期
        Calendarist calendarist = Calendarist.fromSolar(2021, 8, 17, 12, 15, 55, 58);
        // 转阴历
        LunarDate lunarDate = calendarist.toLunar();
        System.out.println(lunarDate);
        // 转阳历
        SolarDate solarDate = calendarist.toSolar();
        System.out.println(solarDate);
        // 转干支历
        CycleDate cycleDate = calendarist.toCycle();
        System.out.println(cycleDate);
    }
}
