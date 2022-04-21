package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.datetime.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

public class CalendarFormatter extends AbstractDateTimeFormatter<Calendar> {

    @Override
    public String format(Calendar calendar) {
        DateFormatter delegate = new DateFormatter(this.getPattern(), calendar.getTimeZone(), this.getLocale());
        return delegate.format(calendar.getTime());
    }

    private static final List<Class> SUPPORTED = Collects.<Class>unmodifiableArrayList(Calendar.class);
    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }

    @Override
    public DateTimeFormatter<Calendar> get() {
        return new CalendarFormatter();
    }
}
