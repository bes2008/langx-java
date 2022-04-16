package com.jn.langx.util.time.formatter;

import com.jn.langx.util.time.DateTimeFormatter;

import java.util.Locale;
import java.util.TimeZone;

public interface UtcTimestampFormatter<Date> extends DateTimeFormatter<Date> {
    TimeZone getTimeZone();
    void setTimeZone(TimeZone timeZone);
    Locale getLocale();
    void setLocale(Locale locale);
}
