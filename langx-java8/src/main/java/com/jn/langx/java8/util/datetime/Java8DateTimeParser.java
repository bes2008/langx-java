package com.jn.langx.java8.util.datetime;

import com.jn.langx.java8.util.concurrent.Java8GlobalThreadLocalMap;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.datetime.parser.DateParsedResult;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Java8DateTimeParser implements DateTimeParser {
    private String pattern;
    private TimeZone timeZone;
    private Locale locale;

    public Java8DateTimeParser(String pattern, Locale locale) {
        this(pattern, TimeZone.getDefault(), locale);
    }

    public Java8DateTimeParser(String pattern, TimeZone timeZone) {
        this(pattern, timeZone, Locale.getDefault());
    }

    public Java8DateTimeParser(String pattern, TimeZone timeZone, Locale locale) {
        this.pattern = pattern;
        this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public DateTimeParsedResult parse(String datetimeString) {
        DateTimeFormatter formatter = Java8GlobalThreadLocalMap.getDateTimeFormatter(this.pattern, this.timeZone, this.locale);
        try {
            TemporalAccessor temporalAccessor  = formatter.parse(datetimeString);
            ZonedDateTime zonedDateTime = (ZonedDateTime) temporalAccessor.query(Dates8.temporalQueryMap.get(ZonedDateTime.class).get());
            Date date = new Date(zonedDateTime.toInstant().toEpochMilli());
            DateParsedResult result = new DateParsedResult(date, this.timeZone, this.locale);
            result.setPattern(pattern);
            result.setOriginText(datetimeString);
            return result;
        } catch (Throwable ex) {
            return null;
        }
    }
}
