package com.jn.langx.util.datetime.grok;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;

import java.util.List;

class GrokDateTimeParser implements DateTimeParser {
    private static final Regexp TIMESTAMP_LONG = Regexps.createRegexp("<?<dtmills>^\\d{10,13}$");

    public static final String GROUP_YEAR = "year";
    public static final String GROUP_MONTH = "month";
    public static final String GROUP_DAY_OF_MONTH = "dayOfMonth";
    public static final String GROUP_HOUR = "hour";
    public static final String GROUP_MINUTE = "minute";
    public static final String GROUP_SECOND = "second";
    public static final String GROUP_MILLS = "mills";

    public static final String GROUP_TIME_ZONE = "timezone";

    private static final List<String> BASIC_GROUP_NAMES = Collects.immutableArrayList(
            GROUP_YEAR,
            GROUP_MONTH,
            GROUP_DAY_OF_MONTH,
            GROUP_HOUR,
            GROUP_MINUTE,
            GROUP_SECOND,
            GROUP_MILLS
    );

    /**
     * 正则中 可用的组名：year, month, dayOfMonth, hour, minute, second, mills
     */
    private List<Regexp> regexps;

    @Override
    public DateTimeParsedResult parse(String dateTimeText) {
        dateTimeText = Strings.trim(dateTimeText);

        return null;
    }
}
