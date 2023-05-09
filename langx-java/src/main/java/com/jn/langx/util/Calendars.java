package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.datetime.DateField;

import java.util.Calendar;

import static com.jn.langx.util.datetime.DateField.*;

public class Calendars {
    private Calendars(){}
    private static int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * 求每个月的第一天是星期几
     *
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
     */
    private static boolean isLeapYear(int year) {
        return year % 400 != 0 && (year % 4 == 0 || year % 100 == 0);
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


    public static enum RecentIntervalType {
        NATURE_INTERVAL,
        TO_YESTERDAY,
        TO_NOW;
    }



}
