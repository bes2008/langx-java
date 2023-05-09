package com.jn.langx.java8.util.concurrent;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.datetime.DateFormatCacheKey;
import com.jn.langx.util.function.Supplier;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Java8GlobalThreadLocalMap extends GlobalThreadLocalMap {
    private final Map<DateFormatCacheKey, DateTimeFormatter> dateFormatMap = new NonAbsentHashMap<>(new Supplier<DateFormatCacheKey, DateTimeFormatter>() {
        @Override
        public DateTimeFormatter get(DateFormatCacheKey key) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(key.pattern);
            if (key.locale != null) {
                df = df.withLocale(key.locale);
            }
            if (Strings.isNotEmpty(key.timeZoneId)) {
                df = df.withZone(ZoneId.of(key.timeZoneId));
            }
            return df;
        }
    });

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern));
    }

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern, @Nullable Locale locale) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern, locale));
    }

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern, @Nullable TimeZone timeZone) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern, timeZone));
    }

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern, @Nullable String timeZoneId) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern, timeZoneId));
    }

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern, timeZone, locale));
    }

    public static DateTimeFormatter getDateTimeFormatter(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        return getDateTimeFormatter(new DateFormatCacheKey(pattern, timeZoneId, locale));
    }

    private static DateTimeFormatter getDateTimeFormatter(DateFormatCacheKey key) {
        return get().dateFormatMap.get(key);
    }

    private static final ThreadLocal<Java8GlobalThreadLocalMap> JAVA8_CACHE = new ThreadLocal<Java8GlobalThreadLocalMap>() {
        @Override
        protected Java8GlobalThreadLocalMap initialValue() {
            return new Java8GlobalThreadLocalMap();
        }
    };

    private static Java8GlobalThreadLocalMap get() {
        return JAVA8_CACHE.get();
    }
}
