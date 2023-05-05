package com.jn.langx.util.datetime.parser;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.ArrayKey;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 5.1.3
 */
public abstract class AbstractCandidateDateTimeParseService implements CandidateDateTimeParseService {
    private ConcurrentHashMap<ArrayKey, DateTimeParser> cache = new ConcurrentHashMap<ArrayKey, DateTimeParser>();

    @Override
    public DateTimeParsedResult parse(String dt, List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales) {
        ArrayKey key = new ArrayKey(candidatePatterns, candidateTimeZones, candidateLocales);
        DateTimeParser parser = cache.get(key);

        if (parser == null) {
            parser = newParser(candidatePatterns, candidateTimeZones, candidateLocales);
            if (parser != null) {
                cache.put(key, parser);
            }
        }
        return parser.parse(dt);
    }

    @NonNull
    protected abstract DateTimeParser newParser(List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales);
}
