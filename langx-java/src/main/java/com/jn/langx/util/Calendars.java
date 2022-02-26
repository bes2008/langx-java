package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Calendars {
    private Calendars(){}
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
     */
    public static int getMaxDay(int year, int month) {
        return month == 2 && isLeapYear(year) ? 29 : arr[month - 1];
    }

    public static int getNumberInWeek(int year, int month, int day) {
        return (getNumberOfFirstDayInMonth(year, month) + day - 1) % 7;
    }


    public static int getField(@NonNull Calendar calendar, @NonNull DateField field) {
        Preconditions.checkNotNull(calendar);
        Preconditions.checkNotNull(field);
        return getField(calendar, field.getField());
    }


    public static int getField(@NonNull Calendar calendar, @NonNull int field){
        return calendar.get(field);
    }

    public static int getYears(@NonNull Calendar calendar) {
        return getField(calendar, YEAR);
    }

    public static int getMonths(@NonNull Calendar calendar) {
        return getMonths(calendar, false);
    }

    public static int getMonths(@NonNull Calendar calendar, boolean actual) {
        int month = getField(calendar, MONTH);
        return actual ? (month + 1) : month;
    }

    public static int getDays(@NonNull Calendar calendar) {
        return getField(calendar, DAY);
    }

    public static int getHours(@NonNull Calendar calendar) {
        return getField(calendar, HOUR);
    }

    public static int getMinutes(@NonNull Calendar calendar) {
        return getField(calendar, MINUTE);
    }

    public static int getSeconds(@NonNull Calendar calendar) {
        return getField(calendar, SECOND);
    }

    public static int getMillis(@NonNull Calendar calendar) {
        return getField(calendar, MILLIS);
    }

    public static void setField(@NonNull Calendar calendar, @NonNull DateField field, int value) {
        Preconditions.checkNotNull(calendar);
        Preconditions.checkNotNull(field);
        calendar.set(field.getField(), value);
        calendar.getTimeInMillis(); // just make sure recompute
    }

    public static void setYears(@NonNull Calendar calendar, int year) {
        Preconditions.checkTrue(year >= 1970);
        setField(calendar, YEAR, year);
    }

    public static void setMonths(@NonNull Calendar calendar, int month) {
        setMonths(calendar, month, false);
    }

    public static void setMonths(@NonNull Calendar calendar, int month, boolean valueIsActual) {
        month = valueIsActual ? (month - 1) : month;
        setField(calendar, MONTH, month);
    }

    public static void setDays(@NonNull Calendar calendar, int year) {
        setField(calendar, YEAR, year);
    }

    public static void setHours(@NonNull Calendar calendar, int year) {
        setField(calendar, YEAR, year);
    }

    public static void setMinutes(@NonNull Calendar calendar, int year) {
        setField(calendar, YEAR, year);
    }

    public static void setSeconds(@NonNull Calendar calendar, int year) {
        setField(calendar, SECOND, year);
    }

    public static void setMillis(@NonNull Calendar calendar, int millis) {
        setField(calendar, MILLIS, millis);
    }

    /**
     * add specified field, may be change other fields
     */
    public static void addField(@NonNull Calendar calendar, @NonNull DateField field, int value) {
        Preconditions.checkNotNull(calendar);
        Preconditions.checkNotNull(field);
        calendar.add(field.getField(), value);
        calendar.getTimeInMillis(); // just make sure recompute
    }

    /**
     * Just roll specified field, will not change other fields
     */
    public static void rollField(@NonNull Calendar calendar, @NonNull DateField field, int value) {
        Preconditions.checkNotNull(calendar);
        Preconditions.checkNotNull(field);
        calendar.roll(field.getField(), value);
        calendar.getTimeInMillis(); // just make sure recompute
    }

    public static enum DateField implements CommonEnum {
        ERA(Calendar.ERA, "era", "世纪"),
        YEAR(Calendar.YEAR, "year", "年"),
        MONTH(Calendar.MONTH, "month", "月"),
        WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR, "week_of_year", "周"),
        WEEK_OF_MONTH(Calendar.WEEK_OF_MONTH, "week_of_month", "周"),
        DAY(Calendar.DAY_OF_MONTH, "day","日"),
        DAY_OF_YEAR(Calendar.DAY_OF_YEAR,"day_of_year", "天"),
        DAY_OF_WEEK(Calendar.DAY_OF_WEEK,"day_of_week", "星期"),
        HOUR(Calendar.HOUR_OF_DAY, "hour", "时"),
        MINUTE(Calendar.MINUTE, "minute","分"),
        SECOND(Calendar.SECOND,"second","秒"),
        MILLIS(Calendar.MILLISECOND,"millisecond","毫秒");

        private EnumDelegate enumDelegate;

        DateField(int code, String name, String displayText) {
            this.enumDelegate = new EnumDelegate(code, name, displayText);
        }

        public int getField() {
            return getCode();
        }

        @Override
        public int getCode() {
            return this.enumDelegate.getCode();
        }

        @Override
        public String getName() {
            return  this.enumDelegate.getName();
        }

        @Override
        public String getDisplayText() {
            return this.enumDelegate.getDisplayText();
        }
    }

    public static final DateField ERA = DateField.ERA;
    public static final DateField YEAR = DateField.YEAR;
    public static final DateField MONTH = DateField.MONTH;
    public static final DateField DAY = DateField.DAY;
    public static final DateField HOUR = DateField.HOUR;
    public static final DateField MINUTE = DateField.MINUTE;
    public static final DateField SECOND = DateField.SECOND;
    public static final DateField MILLIS = DateField.MILLIS;

    public static enum RecentIntervalType {
        NATURE_INTERVAL,
        TO_YESTERDAY,
        TO_NOW;
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
