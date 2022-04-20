package com.jn.langx.util.datetime.formatter;

import java.util.Locale;
import java.util.TimeZone;

public abstract class AbstractUtcTimestampFormatter<Date> implements UtcTimestampFormatter<Date> {
    private TimeZone tz;
    private Locale locale;
    private String pattern;

    @Override
    public TimeZone getTimeZone() {
        return this.tz;
    }

    @Override
    public void setTimeZone(TimeZone tz) {
        this.tz = tz;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getFormat() {
        return this.pattern;
    }

    @Override
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public abstract String format(Date date);
}
