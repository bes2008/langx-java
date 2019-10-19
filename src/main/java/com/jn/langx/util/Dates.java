package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * https://www.iso.org/obp/ui#iso:std:iso:8601:-1:ed-1:v1:en
 * <p>
 * <p>
 * 标准时间：GMT时间，也叫格林威治平时，也叫 UTC时间。
 * Java 6 ：
 * <p>
 * <pre>
 * Date:
 * 表示一个瞬时值，单位是毫秒。它的结果是一个标准的GMT瞬时值。它和时区（timezone）地域（Locale）没有关系。
 * 在同一时刻，地球上两个不同时区国家的人使用Date API获取到的毫秒数完全一样的。
 *
 * Timezone:
 * 表示时区，其实是各个时区与GMT的毫秒数之差, 也即Offset。
 * 因为不同时区的时间看到的是不一样的，但是通过Date获取到的毫秒数是一样的，怎么做到的呢？
 * Date#toString 或者 SimpleDateFormat 都会根据Timezone 和 Locale 来进行处理，具体处理如下：
 *    1） 使用GMT 毫秒数 + Timezone.offset 计算出当地的实际毫秒数
 *    2） 格式化显示时会使用本土语言（Locale）进行处理
 *
 * Calender：
 *  日历，它综合了 GMT millis（Date）, timezone, locale，也就是说它是用来表示某个时区的某个国家的时间。
 *  并在此基础上提供了时间的加减运算。
 *  setTimeInMillis，setTimeInMillis 这两个方法的参数或者返回值是 UTC millis。
 *  提供的时间的加减运算都是针对 Locale Time的。
 *
 * SimpleDateFormat 又是基于Calender（即基于 GMT millis（Date）, timezone, locale）的一个日期格式化工具
 * Pattern 涉及的符号：https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
 * </pre>
 */
public class Dates {
    public static final int MINUTES_TO_SECONDS = 60;
    public static final int HOURS_TO_SECONDS = 60 * MINUTES_TO_SECONDS;
    public static final int DAY_TO_SECONDS = 24 * HOURS_TO_SECONDS;
    public static final long SECONDS_TO_MILLIS = 1000;
    public static final long MINUTES_TO_MILLIS = MINUTES_TO_SECONDS * SECONDS_TO_MILLIS;
    public static final long HOURS_TO_MILLIS = HOURS_TO_SECONDS * SECONDS_TO_MILLIS;
    public static final long DAY_TO_MILLIS = DAY_TO_SECONDS * SECONDS_TO_MILLIS;

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String dd_MM_yyyy = "dd/MM/yyyy";
    public static final String HH_mm_ss = "HH:mm:ss";

    public static String format(long millis, @NonNull String pattern) {
        Preconditions.checkTrue(millis >= 0);
        return format(new Date(millis), pattern);
    }

    public static String format(@NonNull Date date, @NonNull String pattern) {
        Preconditions.checkNotEmpty(pattern, "pattern is empty");
        Preconditions.checkNotNull(date);
        return InternalThreadLocalMap.getSimpleDateFormat(pattern).format(date);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable Locale locale) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern, timeZone);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern, timeZoneId);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern, timeZone, locale);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        return InternalThreadLocalMap.getSimpleDateFormat(pattern, timeZoneId, locale);
    }

    public static Date parse(String dateString, String pattern) {
        try {
            return InternalThreadLocalMap.getSimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }


    /**
     * Adds a number of years to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendars.YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of months to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendars.MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of weeks to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addWeeks(final Date date, final int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of days to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendars.DAY, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of hours to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendars.HOUR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of minutes to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendars.MINUTE, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of seconds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendars.SECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMilliseconds(final Date date, final int amount) {
        return add(date, Calendars.MILLIS, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date          the date, not null
     * @param calendarField the calendar field to add to
     * @param amount        the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date add(final Date date, final int calendarField, final int amount) {
        Preconditions.checkNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static Date add(final Date date, final Calendars.DateField calendarField, final int amount) {
        Preconditions.checkNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendars.addField(c, calendarField, amount);
        return c.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the years field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setYears(final Date date, final int amount) {
        return set(date, Calendars.YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the months field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setMonths(final Date date, final int amount) {
        return setMonths(date, amount, false);
    }

    public static Date setMonths(final Date date, final int amount, boolean valueIsActual) {
        return set(date, Calendars.MONTH, valueIsActual ? (amount - 1) : amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the day of month field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setDays(final Date date, final int amount) {
        return set(date, Calendars.DAY, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the hours field to a date returning a new object.  Hours range
     * from  0-23.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setHours(final Date date, final int amount) {
        return set(date, Calendars.HOUR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the minute field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setMinutes(final Date date, final int amount) {
        return set(date, Calendars.MINUTE, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the seconds field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setSeconds(final Date date, final int amount) {
        return set(date, Calendars.SECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the milliseconds field to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date setMilliseconds(final Date date, final int amount) {
        return set(date, Calendars.DateField.MILLIS, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the specified field to a date returning a new object.
     * This does not use a lenient calendar.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param field  the {@code Calendars.DateField} field to set the amount to
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date set(final Date date, final Calendars.DateField field, final int amount) {
        Preconditions.checkNotNull(date);
        // getInstance() returns a new object, so this method is thread safe.
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        Calendars.setField(c, field, amount);
        return c.getTime();
    }

    public static int get(@NonNull Date date, final Calendars.DateField field) {
        return Calendars.getField(toCalendar(date), field);
    }

    public static int getYears(@NonNull Date date) {
        return Calendars.getYears(toCalendar(date));
    }

    public static int getMonths(@NonNull Date date) {
        return getMonths(date, false);
    }

    public static int getMonths(@NonNull Date date, boolean getActualMonth) {
        return Calendars.getMonths(toCalendar(date), getActualMonth);
    }

    public static int getDays(@NonNull Date date) {
        return Calendars.getDays(toCalendar(date));
    }

    public static int getHours(@NonNull Date date) {
        return Calendars.getHours(toCalendar(date));
    }

    public static int getMinutes(@NonNull Date date) {
        return Calendars.getMinutes(toCalendar(date));
    }

    public static int getSeconds(@NonNull Date date) {
        return Calendars.getSeconds(toCalendar(date));
    }

    public static int getMillis(@NonNull Date date) {
        return Calendars.getMillis(toCalendar(date));
    }

    public static long nextTime(long durationInMills) {
        Preconditions.checkTrue(durationInMills >= 0);
        long now = System.currentTimeMillis();
        if (Long.MAX_VALUE - now <= durationInMills) {
            return Long.MAX_VALUE;
        }
        return now + durationInMills;
    }

    //-----------------------------------------------------------------------

    /**
     * Converts a {@code Date} into a {@code Calendar}.
     *
     * @param date the date to convert to a Calendar
     * @return the created Calendar
     * @throws NullPointerException if null is passed in
     */
    public static Calendar toCalendar(final Date date) {
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        return c;
    }

    //-----------------------------------------------------------------------

    /**
     * Converts a {@code Date} of a given {@code TimeZone} into a {@code Calendar}
     *
     * @param date the date to convert to a Calendar
     * @param tz   the time zone of the {@code date}
     * @return the created Calendar
     * @throws NullPointerException if {@code date} or {@code tz} is null
     */
    public static Calendar toCalendar(final Date date, final TimeZone tz) {
        final Calendar c = Calendar.getInstance(tz);
        c.setTime(date);
        return c;
    }
}
