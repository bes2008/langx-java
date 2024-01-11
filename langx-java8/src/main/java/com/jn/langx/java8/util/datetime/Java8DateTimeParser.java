package com.jn.langx.java8.util.datetime;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.java8.util.concurrent.Java8GlobalThreadLocalMap;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.datetime.parser.DateParsedResult;
import com.jn.langx.util.os.Platform;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.*;

public class Java8DateTimeParser implements DateTimeParser {
    private String pattern;
    // 要么不传真，如果传值，就必须是对的，不然会解析成错误的时间。要么 就不传该值，解析出 是真实的时区
    @Nullable
    private TimeZone timeZone;
    @NonNull
    private Locale locale;

    public Java8DateTimeParser(String pattern, Locale locale) {
        this(pattern, TimeZone.getDefault(), locale);
    }

    public Java8DateTimeParser(String pattern, TimeZone timeZone) {
        this(pattern, timeZone, Locale.getDefault());
    }

    public Java8DateTimeParser(String pattern, TimeZone timeZone, Locale locale) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public DateTimeParsedResult parse(String datetimeString) {
        DateTimeFormatter formatter;
        // jdk 8 上 在pattern 中使用 O 时解析 GMT 时间时是有问题的， jdk 9 中修复了，所以解析GMT时，不会走这里
        if (!Platform.is9VMOrGreater() && datetimeString.contains("GMT")) {
            return Dates.getSimpleCandidateDateTimeParseService().parse(datetimeString, Lists.newArrayList(pattern), timeZone == null ? null : Lists.newArrayList(timeZone), Lists.newArrayList(locale));
        }

        try {
            formatter = Java8GlobalThreadLocalMap.getDateTimeFormatter(this.pattern, this.timeZone, this.locale);

            /*
             * 使用Java 8 的 formatter 解析完毕之后，再使用ZonedDateTime.from 转换为ZonedDateTime的过程中，
             * 取值时区时，会先取 formatter 中的zone，如果为 null，则看看是否解析出了时区，如果解析出了时区，则使用解析出来的时区
             */
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(datetimeString, formatter);
            //parseZonedDateTime(formatter,datetimeString);

            long timestamp = zonedDateTime.toInstant().toEpochMilli();
            ZoneId zoneId = zonedDateTime.getZone();
            TimeZone tz = TimeZone.getTimeZone(zoneId);

            if (this.timeZone != null) {
                // 解析出来 的时区与实际期望的时区有差异时
                if (TimeZone.getTimeZone(zonedDateTime.getZone()).getRawOffset() != this.timeZone.getRawOffset()) {
                    tz = this.timeZone;
                    timestamp = zonedDateTime.toInstant().toEpochMilli() - (this.timeZone.getRawOffset() - zonedDateTime.getOffset().getTotalSeconds() * 1000L);
                }
            }
            Date date = new Date(timestamp);
            DateParsedResult result = new DateParsedResult(date, tz, this.locale);
            result.setPattern(pattern);
            result.setOriginText(datetimeString);
            return result;
        } catch (Throwable ex) {
            return null;
        }
    }

    /*
     * 使用Java 8 的 formatter 解析完毕之后，再使用ZonedDateTime.from 转换为ZonedDateTime的过程中，
     * 取值时区时，会先取 formatter 中的zone，如果为 null，则看看是否解析出了时区，如果解析出了时区，则使用解析出来的时区
     */
    private ZonedDateTime parseZonedDateTime(DateTimeFormatter formatter, String datetimeString) {
        TemporalAccessor temporal = formatter.parse(datetimeString);
        if (temporal instanceof ZonedDateTime) {
            return (ZonedDateTime) temporal;
        }
        try {
            ZoneId zone = queryZoneId(temporal);
            if (temporal.isSupported(INSTANT_SECONDS)) {
                long epochSecond = temporal.getLong(INSTANT_SECONDS);
                int nanoOfSecond = temporal.get(NANO_OF_SECOND);
                return create(epochSecond, nanoOfSecond, zone);
            } else {
                LocalDate date = LocalDate.from(temporal);
                LocalTime time = LocalTime.from(temporal);
                return ZonedDateTime.of(date, time, zone);
            }
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain ZonedDateTime from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    private ZoneId queryZoneId(TemporalAccessor temporal) {
        return temporal.query(new TemporalQuery<ZoneId>() {
            @Override
            public ZoneId queryFrom(TemporalAccessor temporal) {
                if (temporal.isSupported(OFFSET_SECONDS)) {
                    return ZoneOffset.ofTotalSeconds(temporal.get(OFFSET_SECONDS));
                }
                return ZoneId.from(temporal);
            }
        });
    }

    /**
     * 等同于 ZonedDateTime.create
     */
    private static ZonedDateTime create(long epochSecond, int nanoOfSecond, ZoneId zone) {
        ZoneRules rules = zone.getRules();
        Instant instant = Instant.ofEpochSecond(epochSecond, nanoOfSecond);
        ZoneOffset offset = rules.getOffset(instant);
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset);
        return ZonedDateTime.ofLocal(ldt, zone, offset);
    }
}
