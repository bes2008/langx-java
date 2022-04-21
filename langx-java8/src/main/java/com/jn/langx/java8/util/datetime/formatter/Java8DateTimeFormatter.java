package com.jn.langx.java8.util.datetime.formatter;

import com.jn.langx.java8.util.datetime.Dates8;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.datetime.DateTimeFormatter;
import com.jn.langx.util.datetime.TimeZoneAware;
import com.jn.langx.util.datetime.formatter.AbstractDateTimeFormatter;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.TimeZone;

public class Java8DateTimeFormatter extends AbstractDateTimeFormatter<TemporalAccessor> implements TimeZoneAware {
    private TimeZone timeZone;

    @Override
    public String format(TemporalAccessor temporalAccessor) {
        ZoneId zoneId = null;
        if (timeZone != null) {
            zoneId = timeZone.toZoneId();
        }
        return Dates8.format(temporalAccessor, getPattern(), zoneId, getLocale());
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    private static List<Class> SUPPORTED = Collects.newArrayList(TemporalAccessor.class);

    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }

    @Override
    public DateTimeFormatter<TemporalAccessor> get() {
        return new Java8DateTimeFormatter();
    }
}
