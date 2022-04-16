package com.jn.langx.util.time;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SimpleDateTimeFormatter implements DateTimeFormatter<Calendar> {
    @Override
    public TimeZone getTimeZone() {
        return null;
    }

    @Override
    public void setTimeZone() {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public void setFormat() {

    }

    @Override
    public String format(Calendar calendar) {
        return null;
    }
}
