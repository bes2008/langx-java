package com.jn.langx.util;

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
import java.util.Map;

public class InternalThreadLocalMap {
    private static final ThreadLocal<InternalThreadLocalMap> cache = new ThreadLocal<InternalThreadLocalMap>() {
        @Override
        protected InternalThreadLocalMap initialValue() {
            return new InternalThreadLocalMap();
        }
    };

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

    /**
     * key: pattern
     */
    private final Map<String, SimpleDateFormat> simpleDateFormatMap = new NonAbsentHashMap<String, SimpleDateFormat>(new Supplier<String, SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get(String pattern) {
            return new SimpleDateFormat(pattern);
        }
    });

    public static InternalThreadLocalMap get() {
        return cache.get();
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return get().simpleDateFormatMap.get(pattern);
    }

    public static CharsetDecoder getDecoder(Charset charset) {
        return get().decoderMap.get(charset);
    }

    public static CharsetEncoder getEncoder(Charset charset) {
        return get().encoderMap.get(charset);
    }

}
