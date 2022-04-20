package com.jn.langx.util.datetime.formatter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.Date;
import java.util.List;

public class LongTimestampFormatter extends AbstractUtcTimestampFormatter<Long> {

    private long min13 = 1000000000000L;
    private long min10 = 1000000000L;

    @Override
    public String format(Long d) {
        // 如果到 s 是10位，到毫秒是 13位
        Preconditions.checkNotNull(d);
        Preconditions.checkNotNull(d.longValue() >= min10);
        if (d < min13) {
            d = d * 1000;
        }
        Date date = new Date(d);
        DateFormatter dateFormatter = new DateFormatter(getPattern(), getTimeZone(), getLocale());
        return dateFormatter.format(date);
    }

    private static final List<Class> SUPPORTED = Collects.<Class>unmodifiableArrayList(long.class, Long.class);

    @Override
    public List<Class> supported() {
        return SUPPORTED;
    }
}
