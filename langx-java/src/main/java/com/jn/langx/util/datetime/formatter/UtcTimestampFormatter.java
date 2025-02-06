package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.datetime.DateTimeFormatter;
import com.jn.langx.util.datetime.TimeZoneAware;


/**
 * The UtcTimestampFormatter interface extends DateTimeFormatter and TimeZoneAware, providing formatting capabilities for timestamps in UTC time zone.
 * This interface exists for the purpose of standardizing the formatting of date and time in the UTC time zone, building upon the capabilities of formatting dates and times and being aware of time zones.
 */
public interface UtcTimestampFormatter<Date> extends DateTimeFormatter<Date>, TimeZoneAware {
    // No methods are defined in this interface, as its primary function is to combine the functionalities of DateTimeFormatter and TimeZoneAware.
}
