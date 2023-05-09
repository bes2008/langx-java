package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SimpleDateParser implements DateTimeParser {
    private static final Logger logger = Loggers.getLogger(SimpleDateParser.class);
    protected String pattern;
    protected TimeZone timeZone;
    protected Locale locale;

    public SimpleDateParser(String pattern, Locale locale) {
        this(pattern, TimeZone.getDefault(), locale);
    }

    public SimpleDateParser(String pattern, TimeZone timeZone) {
        this(pattern, timeZone, Locale.getDefault());
    }

    public SimpleDateParser(String pattern, TimeZone timeZone, Locale locale) {
        this.pattern = pattern;
        this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public DateTimeParsedResult parse(String datetimeString) {
        SimpleDateFormat simpleDateFormat;
        try {
            simpleDateFormat = GlobalThreadLocalMap.getSimpleDateFormat(this.pattern, this.timeZone, this.locale);
            Date date = simpleDateFormat.parse(datetimeString);
            DateParsedResult result = new DateParsedResult(date, this.timeZone, this.locale);
            result.setPattern(pattern);
            result.setOriginText(datetimeString);
            return result;
        }catch (IllegalArgumentException e){
            logger.warn(e.getMessage(), e);
            return null;
        } catch (ParseException ex) {
            return null;
        }
    }
}
