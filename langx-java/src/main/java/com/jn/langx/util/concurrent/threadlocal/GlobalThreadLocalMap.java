package com.jn.langx.util.concurrent.threadlocal;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.datetime.DateFormatCacheKey;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.random.IRandom;
import com.jn.langx.util.random.PooledBytesRandom;
import com.jn.langx.util.random.RandomProxy;
import com.jn.langx.util.random.ThreadLocalRandom;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.SimpleDateFormat;
import java.util.*;

public class GlobalThreadLocalMap {
    private static final ThreadLocal<GlobalThreadLocalMap> CACHE = new ThreadLocal<GlobalThreadLocalMap>() {
        @Override
        protected GlobalThreadLocalMap initialValue() {
            return new GlobalThreadLocalMap();
        }
    };

    private static GlobalThreadLocalMap get() {
        return CACHE.get();
    }

    /**
     * encoder, decoder
     */
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
     * date formatter
     * key: pattern
     */
    private final Map<DateFormatCacheKey, SimpleDateFormat> simpleDateFormatMap = new NonAbsentHashMap<DateFormatCacheKey, SimpleDateFormat>(new Supplier<DateFormatCacheKey, SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get(DateFormatCacheKey key) {
            SimpleDateFormat df = new SimpleDateFormat(key.pattern, key.locale);
            df.setTimeZone(TimeZone.getTimeZone(key.timeZoneId));
            return df;
        }
    });


    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable Locale locale) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern, locale));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern, timeZone));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern, timeZoneId));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable TimeZone timeZone, @Nullable Locale locale) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern, timeZone, locale));
    }

    public static SimpleDateFormat getSimpleDateFormat(@NonNull String pattern, @Nullable String timeZoneId, @Nullable Locale locale) {
        return getSimpleDateFormat(new DateFormatCacheKey(pattern, timeZoneId, locale));
    }

    private static SimpleDateFormat getSimpleDateFormat(DateFormatCacheKey key) {
        return get().simpleDateFormatMap.get(key);
    }

    private final char[] charBuffer = new char[1024];

    public static char[] getCharBuffer() {
        return get().charBuffer;
    }


    public void clear() {
        CACHE.remove();
    }

    public static IRandom getRandom() {
        return new RandomProxy(ThreadLocalRandom.current());
    }

    private static final PooledBytesRandom pooledBytesRandom = new PooledBytesRandom();

    public static PooledBytesRandom pooledBytesRandom() {
        return pooledBytesRandom;
    }
}
