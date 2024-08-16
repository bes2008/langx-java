package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.datetime.*;
import com.jn.langx.util.datetime.parser.CandidateDateTimeParseService;
import com.jn.langx.util.datetime.parser.Java6CandidateDateTimeParseService;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.jn.langx.util.datetime.DateField.*;

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
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class Dates {
    public static final int MINUTES_TO_SECONDS = 60;
    public static final int HOURS_TO_SECONDS = 60 * MINUTES_TO_SECONDS;
    public static final int DAY_TO_SECONDS = 24 * HOURS_TO_SECONDS;
    public static final long SECONDS_TO_MILLIS = 1000;
    public static final long MINUTES_TO_MILLIS = MINUTES_TO_SECONDS * SECONDS_TO_MILLIS;
    public static final long HOURS_TO_MILLIS = HOURS_TO_SECONDS * SECONDS_TO_MILLIS;
    public static final long DAY_TO_MILLIS = DAY_TO_SECONDS * SECONDS_TO_MILLIS;

    public static final String yyyyMMdd = DateTimePatterns.YYYYMMDD;
    public static final String yyyy_MM_dd = DateTimePatterns.YYYY_MM_DD;
    public static final String yyyy_MM_dd_1 = DateTimePatterns.YYYY_M_D;
    public static final String yyyy_MM_dd_CN = DateTimePatterns.YYYY_MM_DD_CN;
    public static final String yyyy_MM_dd_CN_1 = DateTimePatterns.YYYY_M_D_CN;
    public static final String dd_MM_yyyy = DateTimePatterns.dd_MM_yyyy;
    public static final String MM_dd_yyyy = DateTimePatterns.MM_dd_yyyy;
    public static final String MM_dd_yyyy_1 = DateTimePatterns.M_d_yyyy;

    public static final String yyyyMMddHHmmss = DateTimePatterns.YYYYMMDDHHMMSS;
    public static final String yyyy_MM_dd_HH_mm_ss = DateTimePatterns.YYYY_MM_DD_HH_MM_SS;
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = DateTimePatterns.YYYY_MM_DD_HH_MM_SS_SSS;
    // iso-8601
    public static final String yyyy_MM_dd_T_HH_mm_ss = DateTimePatterns.YYYY_MM_DD_T_HH_MM_SS;
    // iso-8601 with zone
    public static final String yyyy_MM_dd_T_HH_mm_ssZone = DateTimePatterns.YYYY_MM_DD_T_HH_MM_SS_Z;

    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String HH_mm_ss_CN = "HH时mm分ss秒";
    public static final String HH_MM_ssZone = "HH:mm:ssZZ";
    /**
     * @see SimpleDateFormat
     */
    private static final char[] DATE_FORMAT_FLAGS = {'G', 'y', 'Y', 'M', 'w', 'W', 'D', 'd', 'F', 'E', 'u', 'a', 'H', 'k', 'K', 'h', 'm', 's', 'S', 'z', 'Z', 'X'};

    private static final Map<String, CandidateDateTimeParseService> candidateDateTimeParseServiceMap = new HashMap<String, CandidateDateTimeParseService>();

    static {
        Collects.forEach(CommonServiceProvider.loadService(CandidateDateTimeParseService.class), new Consumer<CandidateDateTimeParseService>() {
            @Override
            public void accept(CandidateDateTimeParseService candidateDateTimeParseService) {
                candidateDateTimeParseServiceMap.put(candidateDateTimeParseService.getName(), candidateDateTimeParseService);
            }
        });
    }

    @Deprecated
    public static String format(long millis, @NonNull String pattern) {
        Preconditions.checkTrue(millis >= 0);
        return format(millis, pattern, null, null);
    }

    @Deprecated
    public static String format(@NonNull Date date) {
        return format((Object) date);
    }

    @Deprecated
    public static String format(@NonNull Date date, @NotEmpty String pattern) {
        return format(date, pattern, null, null);
    }

    /**
     * @since 4.5.2
     */
    public static <DATE_TIME> String format(DATE_TIME dateTime) {
        return format(dateTime, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * @since 4.5.2
     */
    public static <DATE_TIME> String format(DATE_TIME dateTime, String pattern) {
        return format(dateTime, pattern, null, null);
    }

    /**
     * @since 4.5.2
     */
    public static <DATE_TIME> String format(DATE_TIME dateTime, String pattern, TimeZone timeZone) {
        return format(dateTime, pattern, timeZone, null);
    }

    /**
     * @since 4.5.2
     */
    public static <DATE_TIME> String format(DATE_TIME dateTime, String pattern, Locale locale) {
        return format(dateTime, pattern, null, locale);
    }

    /**
     * @since 4.5.2
     */
    public static <DATE_TIME> String format(@NonNull DATE_TIME dateTime, @NotEmpty String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        Preconditions.checkNotEmpty(pattern, "pattern is empty");
        Preconditions.checkNotNull(dateTime);
        DateTimeFormatterFactory<DATE_TIME> factory = DateTimeFormatterFactoryRegistry.getInstance().get(dateTime.getClass());
        if (factory == null) {
            throw new NotFoundDateTimeFormatterException(Reflects.getFQNClassName(dateTime.getClass()));
        }
        DateTimeFormatter formatter = factory.get();
        if (timeZone != null && formatter instanceof TimeZoneAware) {
            ((TimeZoneAware) formatter).setTimeZone(timeZone);
        }
        formatter.setPattern(pattern);
        formatter.setLocal(locale);
        return formatter.format(dateTime);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern, @Nullable Locale locale) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern, @Nullable TimeZone timeZone) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern, timeZone);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern, @Nullable String timeZoneId) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern, timeZoneId);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern, timeZone, locale);
    }

    public static SimpleDateFormat getSimpleDateFormat(@NotEmpty String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        return GlobalThreadLocalMap.getSimpleDateFormat(pattern, timeZoneId, locale);
    }

    public static Date parse(String dateString, @NotEmpty String pattern) {
        return parse(dateString, new String[]{pattern});
    }

    public static Date parse(String dateString, String... patterns) {
        return parse(dateString, Collects.asList(patterns));
    }

    public static Date parse(final String dateString, List<String> patterns) {
        return parse(false, dateString, null, null, patterns);
    }

    public static Date parse(final String dateString, TimeZone tz, Locale locale, String pattern) {
        return parse(dateString, tz, locale, Collects.asList(pattern));
    }

    public static Date parse(final String dateString, TimeZone tz, Locale locale, List<String> patterns) {
        return parse(false, dateString, tz, locale, patterns);
    }

    /**
     * @since 5.2.5
     */
    public static Date parse(boolean autoInferTimeZone, final String dateString, TimeZone tz, Locale locale, String... patterns) {
        return parse(autoInferTimeZone, dateString, tz, locale, Collects.asList(patterns));
    }

    /**
     * @since 5.0.1
     */
    public static Date parse(boolean autoInferTimeZone, final String dateString, TimeZone tz, Locale locale, List<String> patterns) {
        tz = tz == null ? TimeZone.getDefault() : tz;
        locale = locale == null ? Locale.getDefault() : locale;
        DateTimeParsedResult dateTimeParsedResult = parseDateTime(autoInferTimeZone, dateString, Collects.asList(tz), Collects.asList(locale), patterns);
        if (dateTimeParsedResult != null) {
            return new Date(dateTimeParsedResult.getTimestamp());
        }
        return null;
    }

    private static final List<String> simple_timezone_suffixes = Collects.newArrayList(
            "XXX",
            "XX",
            "X",
            "Z",
            "z"
    );
    private static final List<String> timezone_suffixes = Platform.is8VMOrGreater() ? Collects.newArrayList("XXX", "XX", "X", "x", "Z", "z", "OOOO", "O", "V") : Collects.newArrayList("X", "Z", "z");

    /**
     * @since 5.0.1
     */
    public static DateTimeParsedResult parseDateTime(boolean autoInferTimeZone, final String dateString, List<TimeZone> candidateTZs, List<Locale> candidateLocals, List<String> candidatePatterns) {
        return parseDateTime(autoInferTimeZone, null, dateString, candidateTZs, candidateLocals, candidatePatterns);
    }

    /**
     * @since 5.2.10
     */
    public static DateTimeParsedResult parseDateTime(boolean autoInferTimeZone, @Nullable CandidateDateTimeParseService parseService, final String dateString, List<TimeZone> candidateTZs, List<Locale> candidateLocals, List<String> candidatePatterns) {
        CandidateDateTimeParseService parser = parseService;
        if (parser == null) {
            parser = getCandidateDateTimeParseServiceForParse(dateString);
        }
        if (!autoInferTimeZone) {
            return parser.parse(dateString, candidatePatterns, candidateTZs, candidateLocals);
        }

        final List<String> ps = Collects.newArrayList();
        final CandidateDateTimeParseService dateTimeParseService = parser;
        Collects.forEach(candidatePatterns, new Consumer<String>() {
            @Override
            public void accept(final String pattern) {
                Collects.forEach(dateTimeParseService instanceof Java6CandidateDateTimeParseService ? simple_timezone_suffixes : timezone_suffixes, new Consumer<String>() {
                    @Override
                    public void accept(String suffix) {
                        ps.add(pattern + suffix);
                    }
                });
                ps.add(pattern);
            }
        });

        // java 6 SimpleDateFormat , java8 DateTimeFormatter 都支持自动推断时区的
        return dateTimeParseService.parse(dateString, ps, candidateTZs, candidateLocals);
    }


    public static CandidateDateTimeParseService getCandidateDateTimeParseServiceForParse(String datetime) {
        // jdk 8 上 在pattern 中使用 O 时解析 GMT 时间时是有问题的， jdk 9 中修复了
        if (!Platform.is9VMOrGreater() && datetime.contains("GMT")) {
            return getSimpleCandidateDateTimeParseService();
        }
        return getCandidateDateTimeParseService();
    }

    public static CandidateDateTimeParseService getCandidateDateTimeParseService() {
        CandidateDateTimeParseService service = null;
        if (Platform.is8VMOrGreater()) {
            service = candidateDateTimeParseServiceMap.get("Java8CandidateDateTimeParseService");
        }
        if (service == null) {
            service = candidateDateTimeParseServiceMap.get("Java6CandidateDateTimeParseService");
        }
        return service;
    }

    public static CandidateDateTimeParseService getSimpleCandidateDateTimeParseService() {
        return candidateDateTimeParseServiceMap.get("Java6CandidateDateTimeParseService");
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
        return add(date, YEAR, amount);
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
        return add(date, MONTH, amount);
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
        return add(date, DAY, amount);
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
        return add(date, HOUR, amount);
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
        return add(date, MINUTE, amount);
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
        return add(date, SECOND, amount);
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
        return add(date, MILLIS, amount);
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

    public static Date add(final Date date, final DateField calendarField, final int amount) {
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
        return set(date, YEAR, amount);
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
        return set(date, MONTH, valueIsActual ? (amount - 1) : amount);
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
        return set(date, DAY, amount);
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
        return set(date, HOUR, amount);
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
        return set(date, MINUTE, amount);
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
        return set(date, SECOND, amount);
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
        return set(date, DateField.MILLIS, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the specified field to a date returning a new object.
     * This does not use a lenient calendar.
     * The original {@code Date} is unchanged.
     *
     * @param date   the date, not null
     * @param field  the {@code DateField} field to set the amount to
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date set(final Date date, final DateField field, final int amount) {
        Preconditions.checkNotNull(date);
        // getInstance() returns a new object, so this method is thread safe.
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        Calendars.setField(c, field, amount);
        return c.getTime();
    }

    public static int get(@NonNull Date date, final DateField field) {
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

    public static long nextTime(long baselineInMills, long durationInMills) {
        Preconditions.checkTrue(baselineInMills >= 0);
        Preconditions.checkTrue(durationInMills >= 0);
        if (Long.MAX_VALUE - baselineInMills <= durationInMills) {
            return Long.MAX_VALUE;
        }
        return baselineInMills + durationInMills;
    }

    public static long nextTime(long durationInMills) {
        if (durationInMills < 0) {
            return Long.MAX_VALUE;
        }
        return nextTime(System.currentTimeMillis(), durationInMills);
    }

    public static long nowMills() {
        return System.currentTimeMillis();
    }

    public static Date now() {
        return new Date();
    }

    public static String nowReadableString() {
        return format(new Date(), yyyy_MM_dd_HH_mm_ss);
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

    public static TimeZone localTimeZone() {
        return TimeZone.getDefault();
    }

    private Dates() {

    }
}
