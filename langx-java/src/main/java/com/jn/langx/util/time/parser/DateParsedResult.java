package com.jn.langx.util.time.parser;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParsedResult extends AbstractDateTimeParsedResult {
    private Date date;
    private TimeZone tz;
    private Locale locale;

    DateParsedResult(Date date, TimeZone tz, Locale locale){
        this.date = date;
        this.tz = tz;
        this.locale = locale;
    }

    @Override
    public long getTimestamp() {
        return date.getTime();
    }

    @Override
    public TimeZone getTimeZone() {
        return this.tz;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }
}
