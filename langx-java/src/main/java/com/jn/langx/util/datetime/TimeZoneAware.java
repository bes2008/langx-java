package com.jn.langx.util.datetime;

import java.util.TimeZone;

/**
 * @since 4.5.2
 */
public interface TimeZoneAware {
    TimeZone getTimeZone();

    void setTimeZone(TimeZone tz);
}
