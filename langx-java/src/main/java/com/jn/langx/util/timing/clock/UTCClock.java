package com.jn.langx.util.timing.clock;

import java.util.Calendar;
import java.util.TimeZone;

public class UTCClock implements Clock {

    private final TimeZone utcZone = TimeZone.getTimeZone("UTC");

    @Override
    public long getTick() {
        return getTime() * 10 ^ 6;
    }

    @Override
    public long getTime() {
        return Calendar.getInstance(utcZone).getTimeInMillis();
    }

}
