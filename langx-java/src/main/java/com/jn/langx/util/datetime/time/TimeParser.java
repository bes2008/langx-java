package com.jn.langx.util.datetime.time;

import com.jn.langx.Parser;

/**
 * @since 4.5.2
 */
public interface TimeParser extends Parser<String, TimeParsedResult> {
    @Override
    TimeParsedResult parse(String time);
}
