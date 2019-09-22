package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.Charsets;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.SimpleDateFormat;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class InternalThreadLocalMap {
    private static final ThreadLocal<InternalThreadLocalMap> cache = new ThreadLocal<InternalThreadLocalMap>() {
        @Override
        protected InternalThreadLocalMap initialValue() {
            return new InternalThreadLocalMap();
        }
    };

    public static InternalThreadLocalMap get() {
        return cache.get();
    }


    private final Map<Charset, CharsetEncoder> encoderMap = WrappedNonAbsentMap.wrap(new IdentityHashMap<Charset, CharsetEncoder>(), new Supplier<Charset, CharsetEncoder>() {
        @Override
        public CharsetEncoder get(Charset charset) {
            return Charsets.encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        }
    });

    private final Map<Charset, CharsetDecoder> decoderMap = WrappedNonAbsentMap.wrap(new IdentityHashMap<Charset, CharsetDecoder>(), new Supplier<Charset, CharsetDecoder>() {
        @Override
        public CharsetDecoder get(Charset charset) {
            return Charsets.decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        }
    });

    public static CharsetDecoder getDecoder(Charset charset) {
        return get().decoderMap.get(charset);
    }

    public static CharsetEncoder getEncoder(Charset charset) {
        return get().encoderMap.get(charset);
    }


    /**
     * key: pattern
     */
    private final Map<SimpleDateFormatCacheKey, SimpleDateFormat> simpleDateFormatMap = new NonAbsentHashMap<SimpleDateFormatCacheKey, SimpleDateFormat>(new Supplier<SimpleDateFormatCacheKey, SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get(SimpleDateFormatCacheKey key) {
            SimpleDateFormat df = new SimpleDateFormat(key.pattern, key.locale);
            df.setTimeZone(TimeZone.getTimeZone(key.timeZoneId));
            return df;
        }
    });


    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable Locale locale) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern, locale));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern, timeZone));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern, timeZoneId));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern, timeZone, locale));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        return getSimpleDateFormat(new SimpleDateFormatCacheKey(pattern, timeZoneId, locale));
    }

    private static SimpleDateFormat getSimpleDateFormat(SimpleDateFormatCacheKey key) {
        return get().simpleDateFormatMap.get(key);
    }

    private static class SimpleDateFormatCacheKey {
        private String pattern;
        private String timeZoneId;
        private Locale locale;

        SimpleDateFormatCacheKey(@NonNull String pattern) {
            this(pattern, (String) null, null);
        }

        SimpleDateFormatCacheKey(@NonNull String pattern, @Nullable String timeZoneId) {
            this(pattern, timeZoneId, null);
        }

        SimpleDateFormatCacheKey(@NonNull String pattern, @Nullable TimeZone timeZone) {
            this(pattern, timeZone, null);
        }

        SimpleDateFormatCacheKey(@NonNull String pattern, @Nullable Locale locale) {
            this(pattern, (String) null, locale);
        }

        SimpleDateFormatCacheKey(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
            this(pattern, timeZone == null ? (String) null : timeZone.getID(), locale);
        }

        SimpleDateFormatCacheKey(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
            Preconditions.checkNotNull(pattern);
            this.locale = locale == null ? Locale.getDefault() : locale;
            this.timeZoneId = Strings.isEmpty(timeZoneId) ? TimeZone.getDefault().getID() : timeZoneId;
            this.pattern = pattern;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SimpleDateFormatCacheKey that = (SimpleDateFormatCacheKey) o;

            if (!pattern.equals(that.pattern)) return false;
            if (!timeZoneId.equals(that.timeZoneId)) return false;
            return locale.equals(that.locale);
        }

        @Override
        public int hashCode() {
            int result = pattern.hashCode();
            result = 31 * result + timeZoneId.hashCode();
            result = 31 * result + locale.hashCode();
            return result;
        }
    }
}
