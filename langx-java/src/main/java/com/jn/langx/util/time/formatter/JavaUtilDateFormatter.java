package com.jn.langx.util.time.formatter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JavaUtilDateFormatter extends AbstractUtcTimestampFormatter<Date> {
    @Override
    public String format(Date date) {
        Preconditions.checkNotNullArgument(date, "date");
        SimpleDateFormat simpleDateFormat = GlobalThreadLocalMap.getSimpleDateFormat(this.getFormat(), this.getTimeZone(), this.getLocale());
        return simpleDateFormat.format(date);
    }

    private static final List<Class> SUPPORTED = Collects.<Class>unmodifiableArrayList(Date.class);

    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }
}
