package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.datetime.DateTimeFormatter;

import java.util.Locale;

public abstract class AbstractDateTimeFormatter<DATE_TIME> implements DateTimeFormatter<DATE_TIME> {
    private Locale locale = Locale.getDefault();
    private String pattern;

    @Override
    public String getPattern() {
        return this.pattern;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setLocal(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public abstract DateTimeFormatter<DATE_TIME> get();
}
