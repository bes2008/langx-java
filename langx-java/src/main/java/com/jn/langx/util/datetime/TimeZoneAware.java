package com.jn.langx.util.datetime;

import java.util.TimeZone;

public interface TimeZoneAware {
    TimeZone getTimeZone();

    void setTimeZone(TimeZone tz);
}
