package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.DateTimeParser;

import java.util.*;

public class CandidatePatternsDateTimeParser implements DateTimeParser {
    private Set<String> patterns = Collects.newLinkedHashSet();
    private Set<TimeZone> timeZones = Collects.newLinkedHashSet(TimeZone.getDefault());
    private Set<Locale> locales = Collects.newLinkedHashSet(Locale.US, Locale.getDefault());

    public CandidatePatternsDateTimeParser(List<String> patterns, List<TimeZone> timeZones, List<Locale> locales) {
        if (patterns != null) {
            this.patterns.addAll(patterns);
        }
        if (timeZones != null) {
            this.timeZones.addAll(timeZones);
        }
        if (locales != null) {
            this.locales.addAll(locales);
        }
    }

    @Override
    public DateTimeParsedResult parse(final String datetimeString) {
        final Holder<DateTimeParsedResult> resultHolder = new Holder<DateTimeParsedResult>();

        final Predicate breakPredicate = new Predicate() {
            @Override
            public boolean test(Object value) {
                return !resultHolder.isNull();
            }
        };
        Collects.forEach(patterns, new Consumer<String>() {
            @Override
            public void accept(final String pattern) {
                Collects.forEach(timeZones, new Consumer<TimeZone>() {
                    @Override
                    public void accept(final TimeZone timeZone) {
                        Collects.forEach(locales, new Consumer<Locale>() {
                            @Override
                            public void accept(Locale locale) {
                                DateTimeParsedResult r = new SimpleDateParser(pattern, timeZone, locale).parse(datetimeString);
                                if (r != null) {
                                    resultHolder.set(r);
                                }
                            }
                        }, breakPredicate);
                    }
                }, breakPredicate);
            }
        }, breakPredicate);
        return resultHolder.get();
    }
}
