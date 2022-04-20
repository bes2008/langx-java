package com.jn.langx.util.datetime.time;

import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultTimeParser implements TimeParser {
    private String pattern;
    private Locale locale;

    public DefaultTimeParser(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public DefaultTimeParser(String pattern, Locale locale) {
        this.pattern = pattern;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }


    public String getPattern() {
        return pattern;
    }

    @Override
    public TimeParsedResult parse(String time) {
        SimpleDateFormat format = GlobalThreadLocalMap.getSimpleDateFormat(this.pattern, this.locale);
        try {
            Date date = format.parse(time);
            TimeParsedResult timeParsedResult = new TimeParsedResult();
            timeParsedResult.setHours(date.getHours());
            timeParsedResult.setMinutes(date.getMinutes());
            timeParsedResult.setSeconds(date.getSeconds());
            String longTime = "" + date.getTime();
            String mills = longTime.substring(longTime.length() - 3);
            timeParsedResult.setMills(Integer.parseInt(mills));
            return timeParsedResult;
        } catch (ParseException ex) {
            return null;
        }
    }
}
