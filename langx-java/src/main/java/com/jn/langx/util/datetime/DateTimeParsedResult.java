package com.jn.langx.util.datetime;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @since 4.5.2
 */
public interface DateTimeParsedResult {
    /**
     * 代表了 年，月，日，时，分，秒， 毫秒
     * <p>
     * 且是 UTC 时间
     */
    long getTimestamp();

    TimeZone getTimeZone();

    Locale getLocale();

    String getPattern();

    String getOriginText();
}
