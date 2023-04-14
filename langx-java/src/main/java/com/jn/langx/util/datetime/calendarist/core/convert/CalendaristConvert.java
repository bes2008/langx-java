package com.jn.langx.util.datetime.calendarist.core.convert;


import com.jn.langx.util.datetime.calendarist.CalendaristConstants;
import com.jn.langx.util.datetime.calendarist.Lunars;
import com.jn.langx.util.datetime.calendarist.pojo.CycleDate;
import com.jn.langx.util.datetime.calendarist.pojo.LunarDate;
import com.jn.langx.util.datetime.calendarist.pojo.SolarDate;

import java.util.Calendar;

/**
 * 转换工具类
 *
 * 参考：
 * https://blog.csdn.net/FengRenYuanDeFZ/article/details/100162807
 *
 * https://zhuanlan.zhihu.com/p/57261062
 * https://github.com/CutePandaSh/zhdate/blob/master/zhdate/__init__.py
 *
 * https://github.com/isee15/Lunar-Solar-Calendar-Converter/blob/master/Java/cn/z/LunarSolarConverter.java
 *
 * https://blog.csdn.net/qq784515681/article/details/80861706#commentsedit
 *
 * https://www.cnblogs.com/doubleWin/p/10690127.html
 *
 */
public class CalendaristConvert {

    /**
     * 阳历时间戳转农历时间
     *
     * @param timeMillis 阳历时间戳
     * @return {@link LunarDate}
     */
    public static LunarDate toLunar(long timeMillis) {
        int year, month, day, leapMonth;
        boolean leap;

        // 求出和1900年1月31日相差的天数
        int offset = (int)((timeMillis - CalendaristConstants.LUNAR_INIT_TIMESTAMP) / 86400000L);

        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = CalendaristConstants.MIN_YEAR; iYear < CalendaristConstants.MAX_YEAR && offset > 0; iYear++) {
            daysOfYear = Lunars.daysOfYear(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }

        // 农历年份
        year = iYear;

        leapMonth = Lunars.leapMonth(iYear); // 闰哪个月,1-12
        leap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = Lunars.daysOfLeapMonth(year);
            } else {
                daysOfMonth = Lunars.daysOfMonth(year, iMonth);
            }

            offset -= daysOfMonth;

            // 解除闰月
            if (leap && iMonth == (leapMonth + 1)) {
                leap = false;
            }
        }

        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
            }
        }

        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }

        month = iMonth;
        day = offset + 1;

        Calendar calendar = Lunars.getCalendarInstance();
        calendar.setTimeInMillis(timeMillis);

        return new LunarDate(
            year,
            month,
            day,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            calendar.get(Calendar.MILLISECOND),
            leap);
    }

    /**
     * 农历日期转阳历日期
     *
     * @param lunarDate 农历日期 {@link LunarDate}
     * @return {@link SolarDate}
     */
    public static SolarDate toSolar(LunarDate lunarDate) {
        if (lunarDate == null) {
            throw new IllegalArgumentException("the param 'lunarDate' must not be null");
        }

        int days = CalendaristConstants.LUNAR_MONTH_DAYS[lunarDate.getYear() - 1887];
        int leap = Lunars.getBitInt(days, 4, 13);
        int offset = 0;
        int loopend = leap;

        if (!lunarDate.isItsLeapMonth()) {
            if (lunarDate.getMonth() <= leap || leap == 0) {
                loopend = lunarDate.getMonth() - 1;
            } else {
                loopend = lunarDate.getMonth();
            }
        }

        for (int i = 0; i < loopend; i++) {
            offset += Lunars.getBitInt(days, 1, 12 - i) == 1 ? 30 : 29;
        }
        offset += lunarDate.getDay();

        int solar11 = CalendaristConstants.SOLAR_CODE[lunarDate.getYear() - 1887];

        int y = Lunars.getBitInt(solar11, 12, 9);
        int m = Lunars.getBitInt(solar11, 4, 5);
        int d = Lunars.getBitInt(solar11, 5, 0);

        long g = Lunars.solarToInt(y, m, d) + offset - 1;

        long year = (10000 * g + 14780) / 3652425;
        long ddd = g - (365 * year + year / 4 - year / 100 + year / 400);
        if (ddd < 0) {
            year--;
            ddd = g - (365 * year + year / 4 - year / 100 + year / 400);
        }
        long mi = (100 * ddd + 52) / 3060;
        long mm = (mi + 2) % 12 + 1;
        year = year + (mi + 2) / 12;
        long dd = ddd - (mi * 306 + 5) / 10 + 1;

        return new SolarDate(
            (int)year, (int)mm, (int)dd,
            lunarDate.getHour(), lunarDate.getMinute(), lunarDate.getSecond(),
            lunarDate.getMillis());
    }

    /**
     * 农历时间转干支时间
     *
     * @param lunarDate 农历时间
     * @return {@link CycleDate}
     */
    public static CycleDate toCycle(LunarDate lunarDate) {
        if (lunarDate == null) {
            throw new IllegalArgumentException("the param 'lunarDate' must not be null");
        }

        // 阳历日期
        SolarDate solarDate = toSolar(lunarDate);

        // 月柱 1900年1月小寒以前为 丙子月(60进制12)
        int firstNode = Lunars.getFirstTerm(solarDate.getYear(), solarDate.getMonth()); // 返回当月「节」为几日开始
        int cM = (solarDate.getYear() - 1900) * 12 + (solarDate.getMonth() - 1) + 12;

        // 依节气月柱, 以「节」为界
        if (solarDate.getDay() >= firstNode) {
            cM++;
        }

        // 1970年01月01日 00:00:00的时间戳
        long startMillis = -28800000L;

        // 获取本月1号的时间戳
        Calendar calendar = Lunars.getCalendarInstance();
        calendar.set(solarDate.getYear(), solarDate.getMonth() - 1, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int cD = (int)((calendar.getTimeInMillis() - startMillis) / 86400000L) + 25567 + 10 + solarDate.getDay() - 1;

        return new CycleDate(lunarDate.getYear() - 1864, cM, cD, solarDate.getHour(), 0, 0, 0);
    }

    /**
     * 阳历日期转干支日期
     *
     * @param solarDate 阳历日期
     * @return {@link CycleDate}
     */
    public static CycleDate toCycle(SolarDate solarDate) {
        if (solarDate == null) {
            throw new IllegalArgumentException("missing the param 'solarDate'");
        }

        // 月柱 1900年1月小寒以前为 丙子月(60进制12)
        // 返回当月「节」为几日开始
        int firstNode = Lunars.getFirstTerm(solarDate.getYear(), solarDate.getMonth());
        int cM = (solarDate.getYear() - 1900) * 12 + (solarDate.getMonth() - 1) + 12;

        // 依节气月柱, 以「节」为界
        if (solarDate.getDay() >= firstNode) {
            cM++;
        }

        // 1970年01月01日 00:00:00的时间戳
        long startMillis = -28800000L;

        // 获取本月1号的时间戳
        Calendar calendar = Lunars.getCalendarInstance();
        calendar.set(solarDate.getYear(), solarDate.getMonth() - 1, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int cD = (int)((solarDate.getTimestamp() - startMillis) / 86400000L) + 25567 + 10 + solarDate.getDay() - 1;

        // 阴历日期
        LunarDate lunarDate = toLunar(solarDate.getTimestamp());

        return new CycleDate(lunarDate.getYear() - 1864, cM, cD, solarDate.getHour(), 0, 0, 0);
    }

}
