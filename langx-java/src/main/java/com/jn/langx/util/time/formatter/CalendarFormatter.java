package com.jn.langx.util.time.formatter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.time.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

public class CalendarFormatter implements DateTimeFormatter<Calendar> {
    private String pattern;
    @Override
    public String getFormat() {
        return pattern;
    }

    @Override
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(Calendar calendar) {
        JavaUtilDateFormatter delegate = new JavaUtilDateFormatter();
        delegate.setFormat(this.getFormat());
        delegate.setTimeZone(calendar.getTimeZone());
        return delegate.format(calendar.getTime());
    }

    private static final List<Class> SUPPORTED = Collects.<Class>unmodifiableArrayList(Calendar.class);
    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }
}
