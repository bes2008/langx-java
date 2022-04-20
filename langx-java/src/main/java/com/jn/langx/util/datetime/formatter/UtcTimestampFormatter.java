package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.datetime.DateTimeFormatter;

import java.util.TimeZone;

public interface UtcTimestampFormatter<Date> extends DateTimeFormatter<Date> {
    TimeZone getTimeZone();

    void setTimeZone(TimeZone timeZone);
}
