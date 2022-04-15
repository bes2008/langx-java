package com.jn.langx.util.time;

import java.util.TimeZone;

public interface DateTimeParsedResult {
    /**
     * 代表了 年，月，日，时，分，秒， 毫秒
     *
     * 且是 UTC 时间
     */
    long getDateTime();
    void setDateTime(long date);

    TimeZone getTimeZone();
    void setTimeZone(TimeZone timeZone);

    String getFormatPattern();
    void setFormatPattern(String formatPattern);

    String getOriginText();
    void setOriginText(String originText);
}
