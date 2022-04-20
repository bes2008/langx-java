package com.jn.langx.util.datetime.formatter;

import java.util.TimeZone;

public abstract class AbstractUtcTimestampFormatter<Date> extends AbstractDateTimeFormatter<Date> implements UtcTimestampFormatter<Date> {
    private TimeZone tz;

    @Override
    public TimeZone getTimeZone() {
        return this.tz;
    }

    @Override
    public void setTimeZone(TimeZone tz) {
        this.tz = tz;
    }


    @Override
    public abstract String format(Date date);
}
