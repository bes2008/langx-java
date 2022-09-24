/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.util.datetime.graceful;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.datetime.DateFormatCacheKey;

import java.text.Format;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * FormatCache is a cache and factory for {@link Format}s.
 *
 * @since 5.0.1
 */
abstract class FormatCache<F extends Format> {

    /**
     * No date or no time.  Used in same parameters as DateFormat.SHORT or DateFormat.LONG
     */
    static final int NONE = -1;

    private final ConcurrentMap<DateFormatCacheKey, F> cInstanceCache = new ConcurrentHashMap<DateFormatCacheKey, F>(7);

    private static final ConcurrentMap<DateFormatCacheKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<DateFormatCacheKey, String>(7);


    /**
     * Gets a formatter instance using the specified pattern, time zone
     * and locale.
     *
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *                 pattern, non-null
     * @param timeZone the time zone, null means use the default TimeZone
     * @param locale   the locale, null means use the default Locale
     * @return a pattern based date/time formatter
     * @throws NullPointerException     if pattern is {@code null}
     * @throws IllegalArgumentException if pattern is invalid
     */
    public F getInstance(final String pattern, TimeZone timeZone, Locale locale) {
        Preconditions.checkNotNullArgument(pattern, "pattern");
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        locale = Objs.useValueIfNull(locale, Locale.getDefault());
        final DateFormatCacheKey key = new DateFormatCacheKey(pattern, timeZone, locale);
        F format = cInstanceCache.get(key);
        if (format == null) {
            format = createInstance(pattern, timeZone, locale);
            final F previousValue = cInstanceCache.putIfAbsent(key, format);
            if (previousValue != null) {
                // another thread snuck in and did the same work
                // we should return the instance that is in ConcurrentMap
                format = previousValue;
            }
        }
        return format;
    }

    /**
     * Create a format instance using the specified pattern, time zone
     * and locale.
     *
     * @param pattern  {@link java.text.SimpleDateFormat} compatible pattern, this will not be null.
     * @param timeZone time zone, this will not be null.
     * @param locale   locale, this will not be null.
     * @return a pattern based date/time formatter
     * @throws IllegalArgumentException if pattern is invalid
     *                                  or {@code null}
     */
    protected abstract F createInstance(String pattern, TimeZone timeZone, Locale locale);


}
