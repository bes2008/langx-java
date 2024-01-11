package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter extends AbstractUtcTimestampFormatter<Date> {
    public DateFormatter(){

    }

    public DateFormatter(String pattern, TimeZone timeZone, Locale locale){
        setPattern(pattern);
        setLocal(locale);
        setTimeZone(timeZone);
    }

    @Override
    public String format(Date date) {
        Preconditions.checkNotNullArgument(date, "date");
        SimpleDateFormat simpleDateFormat = GlobalThreadLocalMap.getSimpleDateFormat(this.getPattern(), Objs.useValueIfNull(this.getTimeZone(), TimeZone.getDefault()), this.getLocale());
        return simpleDateFormat.format(date);
    }

    private static final List<Class> SUPPORTED = Collects.<Class>immutableArrayList(Date.class);

    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }

    @Override
    public DateFormatter get() {
        return new DateFormatter();
    }
}
