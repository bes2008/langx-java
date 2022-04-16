package com.jn.langx.util.time.parser;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.time.DateTimeParsedResult;
import com.jn.langx.util.time.DateTimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LoopTestDateTimeParser implements DateTimeParser {
    private List<String> patterns;
    private TimeZone timeZone = TimeZone.getDefault();
    private Locale locale = Locale.getDefault();

    public LoopTestDateTimeParser(List<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public DateTimeParsedResult parse(final String datetimeString) {
        final Holder<Date> dateHolder = new Holder<Date>();
        Pipeline.of(patterns)
                .forEach(new Consumer2<Integer, String>() {
                    @Override
                    public void accept(Integer idx, String pattern) {
                        SimpleDateFormat formatter = GlobalThreadLocalMap.getSimpleDateFormat(pattern, timeZone, locale);
                        Date date = null;
                        try {
                            date = formatter.parse(datetimeString);
                            dateHolder.set(date);
                        } catch (ParseException ex) {
                            // ignore it
                        }
                    }
                }, new Predicate2<Integer, String>() {
                    @Override
                    public boolean test(Integer idx, String pattern) {
                        return !dateHolder.isEmpty();
                    }
                });
        if (dateHolder.get() != null) {
            DateTimeParsedResult r = new DateParsedResult(dateHolder.get(), this.timeZone, this.locale);
            return r;
        }
        return null;
    }
}
