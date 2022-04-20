package com.jn.langx.util.datetime.time;

import com.jn.langx.Parser;

public interface TimeParser extends Parser<String, TimeParsedResult> {
    @Override
    TimeParsedResult parse(String time);
}
