package com.jn.langx.java8.util.datetime;

import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.Holder;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.*;

@SuppressWarnings({"ALL", "Convert2Lambda"})
public class Dates8 {
    private static final ZoneOffset LOCAL_ZONE_OFFSET;
    /**
     * @see java.time.format.DateTimeFormatter
     */
    private static final char[] JAVA8_TIME_FORMAT_FLAGS = {'G', 'u', 'y', 'Y', 'M', 'L', 'w', 'W', 'D', 'd', 'Q', 'q', 'F', 'E', 'e', 'c', 'a', 'H', 'k', 'K', 'h', 'm', 's', 'S', 'A', 'n', 'N', 'V', 'z', 'Z', 'O', 'X', 'x'};
    private static final char[] JAVA8_TIME_FORMAT_UNIQUE_FLAGS = {'L', 'Q', 'q', 'e', 'c', 'A', 'n', 'N', 'V', 'O', 'x'};

    public static final LocalTime ZERO_TIME = LocalTime.of(0, 0, 0, 0);
    public static final Map<Class<? extends TemporalAccessor>, Holder<TemporalQuery<?>>> temporalQueryMap = new NonAbsentHashMap<>(new Supplier<Class<? extends TemporalAccessor>, Holder<TemporalQuery<?>>>() {
        @Override
        public Holder<TemporalQuery<?>> get(Class<? extends TemporalAccessor> tClass) {
            Holder<TemporalQuery<?>> holder = new Holder<>();
            Method method = Reflects.getDeclaredMethod(tClass, "from", TemporalAccessor.class);
            if (method != null) {
                Reflects.makeAccessible(method);
                TemporalQuery<?> query = new TemporalQuery<Object>() {
                    @Override
                    public Object queryFrom(TemporalAccessor temporal) {
                        return Reflects.invoke(method, null, new Object[]{temporal}, false, true);
                    }
                };
                holder.set(query);
            }
            return holder;
        }
    });

    public static ZoneId localZoneId() {
        return ZoneId.systemDefault();
    }

    private static ZoneOffset _getLocalZoneOffset() {
        return ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);
    }

    public static ZoneOffset localZoneOffset() {
        return LOCAL_ZONE_OFFSET;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return new Date(toOffsetDateTime(localDateTime).toInstant().toEpochMilli());
    }

    public static String format(TemporalAccessor temporal, String pattern) {
        return format(temporal, pattern, null);
    }

    public static String format(TemporalAccessor temporal, String pattern, ZoneId zoneId) {
        return format(temporal, pattern, zoneId, null);
    }

    public static String format(TemporalAccessor temporal, @NotEmpty String pattern, @Nullable ZoneId zoneId, @Nullable Locale locale) {

        if (temporal instanceof LocalDate || temporal instanceof LocalDateTime || temporal instanceof OffsetDateTime) {
            boolean formatZone = Strings.containsAny(pattern, 'Z', 'z', 'O', 'X', 'x');
            if (formatZone) {
                // 转换成有时区的 日期时间
                if (temporal instanceof LocalDate) {
                    temporal = ZonedDateTime.of((LocalDate) temporal, ZERO_TIME, zoneId == null ? localZoneId() : zoneId);
                } else if (temporal instanceof LocalDateTime) {
                    temporal = toZonedDateTime((LocalDateTime) temporal, zoneId);
                } else if (temporal instanceof OffsetDateTime) {
                    temporal = ((OffsetDateTime) temporal).toZonedDateTime();
                }
            }
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        if (temporal instanceof Instant){
            formatter.withZone(ZoneId.systemDefault());
        }

        return formatter.format(temporal);
    }

    public static <T> T parse(String dt, String pattern, Class<T> tClass) {
        T t = null;
        if (Reflects.isSubClassOrEquals(TemporalAccessor.class, tClass)) {
            TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern(pattern).parse(dt);
            Holder<TemporalQuery<?>> queryHolder = temporalQueryMap.get(tClass);
            if (!queryHolder.isEmpty()) {
                t = (T) temporalAccessor.query(queryHolder.get());
            }

        } else if (tClass == Date.class) {
            t = (T) Dates.parse(dt, pattern);
        }
        return t;
    }

    public static Date parseDate(String dt, TimeZone tz, Locale locale, List<String> candidatePatterns) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateTimeParsedResult dateTimeParsedResult = new Java8CandidatePatternsDateTimeParser(candidatePatterns)
                .addLocale(locale)
                .addTimeZone(tz)
                .parse(dt);
        if (dateTimeParsedResult != null) {
            return new Date(dateTimeParsedResult.getTimestamp());
        }
        return null;
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return OffsetDateTime.of(localDateTime, localZoneOffset());
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return toZonedDateTime(localDateTime, null);
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, zoneId == null ? localZoneId() : zoneId);
    }

    static {
        LOCAL_ZONE_OFFSET = _getLocalZoneOffset();
    }

    private Dates8(){

    }
}
