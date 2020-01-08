package com.jn.langx.util.timing.clock;

import java.util.Calendar;
import java.util.TimeZone;

public class UTCClock implements Clock {

    private final TimeZone utcZone = TimeZone.getTimeZone("UTC");

    @Override
    public long getTime() {
        return Calendar.getInstance(utcZone).getTimeInMillis();
    }

}
