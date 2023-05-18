package com.jn.langx.util.datetime;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.util.Locale;
import java.util.TimeZone;

public class DateFormatCacheKey {
    public String pattern;
    public String timeZoneId;
    public Locale locale;

    public DateFormatCacheKey(@NonNull String pattern) {
        this(pattern, (String) null, null);
    }

    public DateFormatCacheKey(@NonNull String pattern, @Nullable String timeZoneId) {
        this(pattern, timeZoneId, null);
    }

    public DateFormatCacheKey(@NonNull String pattern, @Nullable TimeZone timeZone) {
        this(pattern, timeZone, null);
    }

    public DateFormatCacheKey(@NonNull String pattern, @Nullable Locale locale) {
        this(pattern, (String) null, locale);
    }

    public DateFormatCacheKey(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        this(pattern, timeZone == null ? null : timeZone.getID(), locale);
    }

    public DateFormatCacheKey(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        Preconditions.checkNotNull(pattern);
        this.locale = locale == null ? Locale.getDefault() : locale;
        this.timeZoneId = Strings.isEmpty(timeZoneId) ? TimeZone.getDefault().getID() : timeZoneId;
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DateFormatCacheKey that = (DateFormatCacheKey) o;

        if (!pattern.equals(that.pattern)) {
            return false;
        }
        if (!timeZoneId.equals(that.timeZoneId)) {
            return false;
        }
        return locale.equals(that.locale);
    }

    @Override
    public int hashCode() {
        return Objs.hash(this.pattern, this.timeZoneId, this.locale);
    }
}
