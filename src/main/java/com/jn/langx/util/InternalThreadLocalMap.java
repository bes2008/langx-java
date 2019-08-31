package com.jn.langx.util;

import com.jn.langx.util.collection.NonAbsentHashMap;
import com.jn.langx.util.function.Supplier;

import java.text.SimpleDateFormat;
import java.util.Map;

public class InternalThreadLocalMap {

    // key: pattern
    private final Map<String, SimpleDateFormat> dateFormatMap = new NonAbsentHashMap<String, SimpleDateFormat>(new Supplier<String, SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get(String pattern) {
            return new SimpleDateFormat(pattern);
        }
    });

    public static SimpleDateFormat getDateFormat(String pattern) {
        return getSimpleDateFormatCache().get(pattern);
    }

    private static final ThreadLocal<InternalThreadLocalMap> cache = new ThreadLocal<InternalThreadLocalMap>(){
        @Override
        protected InternalThreadLocalMap initialValue() {
            return new InternalThreadLocalMap();
        }
    };

    public static InternalThreadLocalMap get() {
        return cache.get();
    }

    public static Map<String, SimpleDateFormat> getSimpleDateFormatCache() {
        return get().dateFormatMap;
    }
}
