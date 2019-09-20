package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.jodatime.Interval;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Calenders {
    private static int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static String[] week_cn = {"日", "一", "二", "三", "四", "五", "六"};
    private static int[] DAY_OF_WEEK = {
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };


    /**
     * 求每个月的第一天是星期几
     *
     * @return
     */
    private static int getNumberOfFirstDayInMonth(int year, int month) {
        if (month < 3) {
            year--;
            month += 12;
        }

        int y = year % 100;
        int m = month;

        int result = (1 + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7 + 1;
        return result;
    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        return year % 400 == 0 ? false : (year % 4 == 0 || year % 100 == 0);
    }

    /**
     * 获取某个月份的天数
     *
     * @return
     */
    public static int getMaxDay(int year, int month) {
        return month == 2 && isLeapYear(year) ? 29 : arr[month - 1];
    }

    public static int getNumberInWeek(int year, int month, int day) {
        return (getNumberOfFirstDayInMonth(year, month) + day - 1) % 7;
    }

    /**
     * 获取一个时间点所在那一周的起止时间
     * 有的国家是从 周一到周日
     * 有的国家是从 周日到周六
     * 有的国家是从 周六到周五
     * <pre>
     *
     * 星期一：Monday（Mon.）
     * 星期二：Tuesday（Tues.）
     * 星期三：Wednesday（Wed.）
     * 星期四：Thursday（Thur./Thurs.）
     * 星期五：Friday（Fri.）
     * 星期六：Saturday（Sat.）
     * 星期日：Sunday（Sun.）
     * </pre>
     *
     * @param millis time millis
     */
    public static Interval getWeek(long millis, RecentIntervalType recentWeekType) {
        Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        calendar.getFirstDayOfWeek();

        return null;
    }

    private static enum IntervalType{
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE,
        SECOND
    }

    private static enum RecentIntervalType {
        NATURE_INTERVAL,
        TO_YESTERDAY,
        TO_NOW,
        TO_TODAY;
    }

    /**
     * Java 6 Calendar API 已经做了这个事情
     */
    private static enum WeekType {
        MONDAY_TO_SUNDAY(Locale.CHINA, Locale.CHINESE),
        SUNDAY_TO_SATUREDAY(Locale.ENGLISH, Locale.JAPAN, Locale.JAPANESE, Locale.US, Locale.UK),
        SATUREDAY_TO_Friday;

        private List<Locale> locales;

        WeekType(Locale... locales) {
            this.locales = Collects.asList(locales);
        }
    }

}
