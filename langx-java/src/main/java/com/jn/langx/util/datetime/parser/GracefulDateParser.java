package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.graceful.GracefulDateFormat;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GracefulDateParser extends SimpleDateParser {
    private static final Logger logger = Loggers.getLogger(GracefulDateParser.class);

    public GracefulDateParser(String pattern, Locale locale) {
        super(pattern, locale);
    }

    public GracefulDateParser(String pattern, TimeZone timeZone) {
        super(pattern, timeZone);
    }

    public GracefulDateParser(String pattern, TimeZone timeZone, Locale locale) {
        super(pattern, timeZone, locale);
    }

    @Override
    public DateTimeParsedResult parse(String datetimeString) {
        DateTimeParsedResult r = parseWithFastDateFormat(datetimeString);
        if (r == null) {
            r = parseWithSimpleDateFormat(datetimeString);
        }
        return r;
    }

    private DateTimeParsedResult parseWithFastDateFormat(String datetimeString) {
        GracefulDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = GracefulDateFormat.getInstance(this.pattern, this.timeZone, this.locale);
            Date date = simpleDateFormat.parse(datetimeString);
            DateParsedResult result = new DateParsedResult(date, this.timeZone, this.locale);
            result.setPattern(pattern);
            result.setOriginText(datetimeString);
            return result;
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
            return null;
        } catch (ParseException ex) {
            return null;
        }
    }

    private DateTimeParsedResult parseWithSimpleDateFormat(String datetimeString) {
        return super.parse(datetimeString);
    }
}
